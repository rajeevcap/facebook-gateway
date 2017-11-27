package com.capillary.social.services.impl.builders;

import com.capillary.social.commons.model.SocialAudienceList;
import com.capillary.social.commons.model.SocialAudienceList.Type;
import com.google.api.ads.adwords.axis.v201710.cm.Operator;
import com.google.api.ads.adwords.axis.v201710.rm.*;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import static com.capillary.social.commons.model.SocialAudienceList.toSocialAudienceLists;
import static com.capillary.social.services.impl.builders.GoogleProcessorHelper.GoogleAPIKeys.GOOGLE_ADS_CLIENT_CUSTOMER_ID;
import static com.capillary.social.services.impl.builders.GoogleProcessorHelper.getMemberList;

/**
 * Created by rajeev on 3/11/17.
 */
public class GoogleListBuilder extends SocialListBuilder {

    private static final Logger logger = LoggerFactory.getLogger(GoogleListBuilder.class);

    private GoogleProcessorHelper googleHelper;

    @Override
    protected void prepareAPICallContext() throws ConfigurationLoadException, ValidationException, OAuthException, ConfigurationException {
        googleHelper = new GoogleProcessorHelper(orgId);
        googleHelper.authenticate();
        googleHelper.generateServices();
    }

    @Override
    protected void createNewList() throws Exception {
        logger.info("create new list call with list name {} and description {}", listName, listDescription);
        CrmBasedUserList userList = getCrmBasedUserList();
        UserListOperation operation = getUserListOperation(userList);
        UserListReturnValue result = googleHelper.userListService.mutate(new UserListOperation[]{operation});
        remoteListId = result.getValue(0).getId();
        logger.info("created empty list with name {} recipient list id {} and remote list id {}", new Object[]{listName, recipientListId, remoteListId});
    }

    @Override
    protected void getExistingList(Long remoteListId) {
        this.remoteListId = remoteListId;
    }

    @Override
    protected void appendMembersToList() throws UnsupportedEncodingException, RemoteException {
        logger.info("received call to append members to the list {}", recipientListId);
        MutateMembersOperand operand = getMutateMembersOperand();
        MutateMembersOperation[] mutateMembersOperations = getMutateMembersOperations(operand);
        MutateMembersReturnValue mutateMembersReturnValue = googleHelper.userListService.mutateMembers(mutateMembersOperations);
        UserList[] userLists = mutateMembersReturnValue.getUserLists();
        List<SocialAudienceList> socialAudienceLists = getSocialAudienceLists(userLists);
        saveToDatabase(socialAudienceLists);
        for(UserList userList : mutateMembersReturnValue.getUserLists()) {
            logger.info("{} users were uploaded to google list with id {}", userDetails.size(), userList.getId());
        }
    }

    @Override
    protected void fetchAdAccountId() {
        adAccountId = googleHelper.keyValueMap.get(GOOGLE_ADS_CLIENT_CUSTOMER_ID.name());
    }

    private CrmBasedUserList getCrmBasedUserList() {
        CrmBasedUserList userList = new CrmBasedUserList();
        userList.setName(listName);
        userList.setDescription(listDescription);
        userList.setMembershipLifeSpan(10000L); // never expiring
        return userList;
    }

    private UserListOperation getUserListOperation(CrmBasedUserList userList) {
        UserListOperation operation = new UserListOperation();
        operation.setOperand(userList);
        operation.setOperator(Operator.ADD);
        return operation;
    }

    private List<SocialAudienceList> getSocialAudienceLists(UserList[] userLists) {
        // google user List doesn't have last updated or auto update time so using own updated time
        Date now = new Date();
        Date createdOn = getCreatedOn(now);
        Date remoteUpdatedOn = new Date(now.getTime());
        return toSocialAudienceLists(userLists, adAccountId, orgId, recipientListId, createdOn, remoteUpdatedOn, Type.GOOGLE);
    }

    private MutateMembersOperand getMutateMembersOperand() throws UnsupportedEncodingException {
        MutateMembersOperand operand = new MutateMembersOperand();
        operand.setUserListId(remoteListId);
        operand.setMembersList(getMemberList(userDetails));
        return operand;
    }

    private MutateMembersOperation[] getMutateMembersOperations(MutateMembersOperand operand) {
        MutateMembersOperation mutateMembersOperation = new MutateMembersOperation();
        mutateMembersOperation.setOperand(operand);
        mutateMembersOperation.setOperator(Operator.ADD);
        return new MutateMembersOperation[]{mutateMembersOperation};
    }

    private Date getCreatedOn(Date now) {
        SocialAudienceList existingList = getExistingAudienceList();
        if(existingList != null)
            return existingList.getCreatedOn();
        else return new Date(now.getTime());
    }

}
