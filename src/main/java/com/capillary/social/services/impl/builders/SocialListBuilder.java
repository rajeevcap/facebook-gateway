package com.capillary.social.services.impl.builders;

import com.capillary.social.UserDetails;
import com.capillary.social.commons.model.SocialAudienceList;
import com.capillary.social.services.api.builders.ISocialListBuilder;
import com.google.api.ads.adwords.axis.v201710.cm.ApiException;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.List;

import static com.capillary.social.services.impl.builders.SocialProcessorHelper.socialAudienceListDao;

/**
 * Created by rajeev on 3/11/17.
 */

public abstract class SocialListBuilder implements ISocialListBuilder {

    private static final Logger logger = LoggerFactory.getLogger(SocialListBuilder.class);

    long orgId;
    String recipientListId;
    String listName;
    String listDescription;
    List<UserDetails> userDetails;

    String adAccountId;
    Long remoteListId;

    private SocialAudienceList socialAudienceList;

    protected abstract void prepareAPICallContext() throws ConfigurationLoadException, ValidationException, OAuthException, ConfigurationException;

    protected abstract void createNewList() throws RemoteException, Exception, ApiException;

    protected abstract void getExistingList(Long remoteListId);

    protected abstract void appendMembersToList() throws UnsupportedEncodingException, RemoteException;

    protected abstract void fetchAdAccountId();

    private void setFields(long orgId, String recipientListId, String listName, String listDescription, List<UserDetails> userDetails) {
        this.orgId = orgId;
        this.recipientListId = recipientListId;
        this.listName = listName;
        this.listDescription = listDescription;
        this.userDetails = userDetails;
    }

    @Override
    public String build(List<UserDetails> userDetails, String listName, String listDescription, long orgId, String recipientListId) throws Exception {
        setFields(orgId, recipientListId, listName, listDescription, userDetails);
        prepareAPICallContext();
        fetchAdAccountId();
        createNewOrGetExistingList();
        appendMembersToList();
        return remoteListId.toString();
    }

    private void createNewOrGetExistingList() throws Exception {
        if(getExistingAudienceList() == null) {
            createNewList();
        } else {
            getExistingList(Long.valueOf(socialAudienceList.getRemoteListId()));
        }
    }

    SocialAudienceList getExistingAudienceList() {
        if(socialAudienceList == null) {
            socialAudienceList = socialAudienceListDao.findByRecepientListId(recipientListId, adAccountId);
        }
        return socialAudienceList;
    }

    void saveToDatabase(List<SocialAudienceList> socialAudienceLists) {
        try {
            for (SocialAudienceList socialAudienceList : socialAudienceLists) {
                socialAudienceListDao.create(socialAudienceList);
            }
        } catch (Exception e) {
            logger.error("exception occured while saving audience list " + e);
        }
    }
}
