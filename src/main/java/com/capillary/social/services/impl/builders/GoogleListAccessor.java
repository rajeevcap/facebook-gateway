package com.capillary.social.services.impl.builders;

import com.capillary.social.CustomAudienceList;
import com.capillary.social.commons.model.SocialAudienceList;
import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.utils.v201710.SelectorBuilder;
import com.google.api.ads.adwords.axis.v201710.cm.*;
import com.google.api.ads.adwords.axis.v201710.rm.*;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.selectorfields.v201710.cm.AdwordsUserListField;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.*;
import java.util.Date;

/**
 * Created by rajeev on 6/11/17.
 */
public class GoogleListAccessor extends SocialListAccessor {

    private static final Logger logger = LoggerFactory.getLogger(GoogleListAccessor.class);

    private static final String GOOGLE = "GOOGLE";

    private AdwordsUserListServiceInterface userListService;

    @Override
    protected void generateAuthentication() throws ConfigurationLoadException, ValidationException, OAuthException {
        PropertiesConfiguration config = getPropertiesConfiguration();
        Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(OfflineCredentials.Api.ADWORDS).from(config).build().generateCredential();
        AdWordsSession session = new AdWordsSession.Builder().from(config).withOAuth2Credential(oAuth2Credential).build();
        AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();
        userListService = adWordsServices.get(session, AdwordsUserListServiceInterface.class);
    }

    @Override
    protected List<CustomAudienceList> getAllSocialList() throws RemoteException {
        List<SocialAudienceList> audienceLists = getExistingListFromDatabase(getAdAccountId(), getOrgId(), GOOGLE);
        logger.info("exisiting lists size from db : {}", audienceLists.size());
        if(audienceLists == null || audienceLists.isEmpty()) {
            return Collections.emptyList();
        } else if(!fetchListFromAPICall()) {
            return SocialAudienceList.toCustomAudienceListLists(audienceLists);
        }
        Map<String, SocialAudienceList> remoteLocalListMap = getRemoteLocalListMap(audienceLists);
        List<SocialAudienceList> updatedAudienceLists = new ArrayList<>();
        Selector selector = buildSelector();
        UserListPage userLists = userListService.get(selector);
        logger.info("user lists count obtained from google api call : {}", userLists.getEntries().length);
        if(userLists.getEntries() != null){
            for(UserList userList : userLists) {
                String remoteUserListId = userList.getId().toString();
                if(!remoteLocalListMap.containsKey(remoteUserListId)) {
                    logger.info("ignoring unknown list {}",userList.getId());
                    continue;
                }
                SocialAudienceList oldSocialAudienceList = remoteLocalListMap.get(remoteUserListId);
                updatedAudienceLists.add(SocialAudienceList.toSocialAudienceList(userList, getAdAccountId(), getOrgId(), oldSocialAudienceList.getCampaignReceipientListId(), oldSocialAudienceList.getCreatedOn(), new Date(), SocialAudienceList.Type.GOOGLE));
                logger.info("user list obtained with id {} name {} status {} and size range {}", new Object[]{userList.getId(), userList.getName(), userList.getStatus(), userList.getSizeRange()});
            }
        }
        getSocialAudienceListDao().updateBatch(updatedAudienceLists);
        return SocialAudienceList.toCustomAudienceListLists(updatedAudienceLists);
    }

    private Selector buildSelector() {
        SelectorBuilder selectorBuilder = new SelectorBuilder();
        return selectorBuilder.fields(AdwordsUserListField.Id, AdwordsUserListField.Status, AdwordsUserListField.Description, AdwordsUserListField.Size, AdwordsUserListField.SizeRange).build();
    }

}
