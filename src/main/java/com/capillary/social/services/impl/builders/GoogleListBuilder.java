package com.capillary.social.services.impl.builders;

import com.capillary.social.commons.model.SocialAudienceList;
import com.capillary.social.commons.model.SocialAudienceList.Type;
import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.v201710.cm.Operator;
import com.google.api.ads.adwords.axis.v201710.rm.*;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import static com.capillary.social.commons.model.SocialAudienceList.toSocialAudienceLists;

/**
 * Created by rajeev on 3/11/17.
 */
public class GoogleListBuilder extends SocialListBuilder {

    private static final Logger logger = LoggerFactory.getLogger(GoogleListBuilder.class);

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
    protected void createNewList() throws Exception {
        CrmBasedUserList userList = new CrmBasedUserList();
        userList.setName(getListName());
        userList.setDescription(getListDescription());
        userList.setMembershipLifeSpan(10000L); // never expiring

        UserListOperation operation = new UserListOperation();
        operation.setOperand(userList);
        operation.setOperator(Operator.ADD);

        UserListReturnValue result = userListService.mutate(new UserListOperation[]{operation});
        setRemoteListId(result.getValue(0).getId());
        logger.info("created empty list with name {} recipient list id {} and remote list id {}", new Object[]{getListName(), getRecipientListId(), getRemoteListId()});
    }

    @Override
    protected void getExistingList(Long remoteListId) {
        setRemoteListId(remoteListId);
    }

    @Override
    protected void appendMembersToList() throws UnsupportedEncodingException, RemoteException {
        logger.info("received call to append members to the list {}", getRecipientListId());
        MutateMembersOperand operand = new MutateMembersOperand();
        operand.setUserListId(getRemoteListId());
        List<Member> members = getMemberList(getUserDetails());
        operand.setMembersList(members.toArray(new Member[members.size()]));
        MutateMembersOperation mutateMembersOperation = new MutateMembersOperation();
        mutateMembersOperation.setOperand(operand);
        mutateMembersOperation.setOperator(Operator.ADD);
        MutateMembersReturnValue mutateMembersReturnValue = userListService.mutateMembers(new MutateMembersOperation[]{mutateMembersOperation});
        // google user List doesn't have last updated or auto update time so using own updated time
        Date remoteUpdatedOn = new Date();
        saveToDatabase(toSocialAudienceLists(mutateMembersReturnValue.getUserLists(), getAdAccountId(), getOrgId(), getRecipientListId(), remoteUpdatedOn, Type.GOOGLE));
        for(UserList userList : mutateMembersReturnValue.getUserLists()) {
            logger.info("{} users were uploaded to google list with id {}", getUserDetails().size(), userList.getId());
        }
    }

}
