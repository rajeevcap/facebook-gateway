package com.capillary.social.services.impl.builders;

import com.capillary.social.CustomAudienceList;
import com.capillary.social.commons.model.SocialAudienceList;
import com.google.api.ads.adwords.axis.utils.v201710.SelectorBuilder;
import com.google.api.ads.adwords.axis.v201710.cm.*;
import com.google.api.ads.adwords.axis.v201710.rm.*;
import com.google.api.ads.adwords.lib.selectorfields.v201710.cm.AdwordsUserListField;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.*;
import java.util.Date;

import static com.capillary.social.services.impl.builders.GoogleProcessorHelper.GoogleAPIKeys.*;
import static com.capillary.social.services.impl.builders.SocialProcessorHelper.getRemoteLocalListMap;

/**
 * Created by rajeev on 6/11/17.
 */
public class GoogleListAccessor extends SocialListAccessor {

    private static final Logger logger = LoggerFactory.getLogger(GoogleListAccessor.class);

    private static final String GOOGLE = "GOOGLE";

    private GoogleProcessorHelper googleHelper;

    @Override
    protected void prepareAPICallContext() throws ConfigurationLoadException, OAuthException, ConfigurationException, ValidationException {
        googleHelper = new GoogleProcessorHelper(orgId);
        googleHelper.authenticate();
        googleHelper.generateServices();
    }

    @Override
    protected List<CustomAudienceList> getAllSocialList() throws RemoteException {
        List<SocialAudienceList> audienceLists = getExistingAudienceLists(adAccountId, orgId, GOOGLE);
        logger.info("exisiting lists size from db : {}", audienceLists.size());
        if(audienceLists.isEmpty()) {
            return Collections.emptyList();
        } else if(!clearCache) {
            return SocialAudienceList.toCustomAudienceListLists(audienceLists);
        }
        Map<String, SocialAudienceList> remoteLocalListMap = getRemoteLocalListMap(audienceLists);
        Selector selector = buildSelector();
        UserListPage userLists = googleHelper.userListService.get(selector);
        logger.info("user lists count obtained from google api call : {}", userLists.getEntries().length);
        List<SocialAudienceList> updatedAudiences = getUpdatedAudiences(remoteLocalListMap, userLists);
        SocialProcessorHelper.socialAudienceListDao.updateBatch(updatedAudiences);
        return SocialAudienceList.toCustomAudienceListLists(updatedAudiences);
    }

    @Override
    protected void fetchAdAccountId() {
        adAccountId = googleHelper.keyValueMap.get(GOOGLE_ADS_CLIENT_CUSTOMER_ID.name());
    }

    private List<SocialAudienceList> getUpdatedAudiences(Map<String, SocialAudienceList> remoteLocalListMap, UserListPage userLists) {
        List<SocialAudienceList> updatedAudiences = new ArrayList<>();
        if(userLists.getEntries() != null){
            for(UserList userList : userLists) {
                String remoteUserListId = userList.getId().toString();
                if(!remoteLocalListMap.containsKey(remoteUserListId)) {
                    logger.info("ignoring unknown list {}",userList.getId());
                    continue;
                }
                SocialAudienceList updatedAudience = getUpdatedAudience(remoteLocalListMap, userList, remoteUserListId);
                updatedAudiences.add(updatedAudience);
                logger.info("user list obtained with id {} name {} status {} size range {} and size for search {}", new Object[]{userList.getId(), userList.getName(), userList.getStatus(), userList.getSizeRange(), userList.getSizeForSearch()});
            }
        }
        return updatedAudiences;
    }

    private SocialAudienceList getUpdatedAudience(Map<String, SocialAudienceList> remoteLocalListMap, UserList userList, String remoteUserListId) {
        SocialAudienceList oldList = remoteLocalListMap.get(remoteUserListId);
        return SocialAudienceList.toSocialAudienceList(userList, adAccountId, orgId, oldList.getCampaignReceipientListId(), oldList.getCreatedOn(), new Date(), SocialAudienceList.Type.GOOGLE);
    }

    private Selector buildSelector() {
        SelectorBuilder selectorBuilder = new SelectorBuilder();
        return selectorBuilder.fields(AdwordsUserListField.Id, AdwordsUserListField.Status, AdwordsUserListField.Description, AdwordsUserListField.Size, AdwordsUserListField.SizeRange, AdwordsUserListField.SizeForSearch).build();
    }

}
