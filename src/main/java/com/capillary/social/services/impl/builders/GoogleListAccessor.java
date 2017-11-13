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
import com.google.api.ads.adwords.lib.selectorfields.v201710.cm.CampaignField;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;
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
        Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(OfflineCredentials.Api.ADWORDS).fromFile().build().generateCredential();
        AdWordsSession session = new AdWordsSession.Builder().fromFile().withOAuth2Credential(oAuth2Credential).build();
        AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();
        userListService = adWordsServices.get(session, AdwordsUserListServiceInterface.class);
    }

    @Override
    protected List<CustomAudienceList> getAllSocialList() throws RemoteException {
        List<SocialAudienceList> audienceLists = getExistingListFromDatabase(getAdAccountId(), getOrgId(), GOOGLE);
        if(audienceLists == null || audienceLists.isEmpty()) {
            return Collections.emptyList();
        }
        // TODO - match with result from db with that from API result
        SelectorBuilder selectorBuilder = new SelectorBuilder();
        Selector selector = selectorBuilder.fields(AdwordsUserListField.Id, AdwordsUserListField.Status, AdwordsUserListField.Description, AdwordsUserListField.Size, AdwordsUserListField.SizeRange).build();
        UserListPage userLists = userListService.get(selector);
        if(userLists.getEntries() != null){
            for(UserList userList : userLists) {
                logger.info("user list obtained with id {} status {} and size range {}", new Object[]{userList.getId(), userList.getStatus(), userList.getSizeRange()});
            }
        }
        List<SocialAudienceList> socialAudienceLists = SocialAudienceList.toSocialAudienceLists(userLists.getEntries(), getAdAccountId(), getOrgId(), "12342", new Date());
        getSocialAudienceListDao().updateBatch(socialAudienceLists);
        return SocialAudienceList.toCustomAudienceListLists(socialAudienceLists);
    }

}
