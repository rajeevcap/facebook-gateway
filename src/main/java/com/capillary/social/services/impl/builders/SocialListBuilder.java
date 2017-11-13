package com.capillary.social.services.impl.builders;

import com.capillary.social.UserDetails;
import com.capillary.social.commons.dao.api.SocialAudienceListDao;
import com.capillary.social.commons.model.SocialAudienceList;
import com.capillary.social.handler.ApplicationContextAwareHandler;
import com.capillary.social.services.api.builders.ISocialListBuilder;
import com.google.api.ads.adwords.axis.v201710.cm.ApiException;
import com.google.api.ads.adwords.axis.v201710.rm.Member;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajeev on 3/11/17.
 */

public abstract class SocialListBuilder implements ISocialListBuilder {

    private static final Logger logger = LoggerFactory.getLogger(SocialListBuilder.class);

    private long orgId;
    private String recipientListId;
    private String listName;
    private String listDescription;
    private List<UserDetails> userDetails;
    private String adAccountId;
    private Long remoteListId;
    private static SocialAudienceListDao socialAudienceListDao;
    private static final MessageDigest digest = getSHA256MessageDigest();

    protected abstract void generateAuthentication() throws ConfigurationLoadException, ValidationException, OAuthException;

    protected abstract void createNewList() throws RemoteException, Exception, ApiException;

    protected abstract void getExistingList(Long remoteListId);

    protected abstract void appendMembersToList() throws UnsupportedEncodingException, RemoteException;

    private void setFields(long orgId, String recipientListId, String listName, String listDescription, List<UserDetails> userDetails) {
        this.orgId = orgId;
        this.recipientListId = recipientListId;
        this.listName = listName;
        this.listDescription = listDescription;
        this.userDetails = userDetails;
        fetchAdAccountId();
        getBeans();
    }

    private void fetchAdAccountId() {
        this.adAccountId = "118772362192973";
    }

    private static void getBeans() {
        ApplicationContext applicationContext = ApplicationContextAwareHandler.getApplicationContext();
        socialAudienceListDao = (SocialAudienceListDao) applicationContext.getBean("socialAudienceListDaoImpl");
    }

    @Override
    public String build(List<UserDetails> userDetails, String listName, String listDescription, long orgId, String recipientListId) throws RemoteException, ConfigurationLoadException, OAuthException, ValidationException, UnsupportedEncodingException, Exception, ApiException {
        setFields(orgId, recipientListId, listName, listDescription, userDetails);
        generateAuthentication();
        createNewOrGetExistingList();
        appendMembersToList();
        return remoteListId.toString();
    }

    private void createNewOrGetExistingList() throws RemoteException, Exception, ApiException {
        SocialAudienceList audienceList = getExistingListFromDatabase();
        if(audienceList == null) {
            createNewList();
        } else {
            getExistingList(Long.valueOf(audienceList.getRemoteListId()));
        }
    }

    private SocialAudienceList getExistingListFromDatabase() {
        return socialAudienceListDao.findByRecepientListId(recipientListId, adAccountId);
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

    //// helper static functions ////

    static List<Member> getMemberList(List<UserDetails> userDetails) throws UnsupportedEncodingException {
        List<Member> members = new ArrayList<>();
        for(UserDetails userDetail : userDetails) {
            Member member = new Member();
            String normalizedEmail = toNormalizedString(userDetail.getEmail());
            String normalizedMobile = toNormalizedString(userDetail.getMobile());
            member.setHashedEmail(toSHA256String(normalizedEmail));
            member.setHashedPhoneNumber(toSHA256String(normalizedMobile));
            members.add(member);
        }
        return members;
    }

    private static String toSHA256String(String value) throws UnsupportedEncodingException {
        byte[] hash = digest.digest(value.getBytes("UTF-8"));
        StringBuilder result = new StringBuilder();
        for (byte b : hash) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    private static String toNormalizedString(String value) {
        return value.trim().toLowerCase();
    }

    private static MessageDigest getSHA256MessageDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Missing SHA-256 algorithm implementation.", e);
        }
    }

    //// setters ////

    void setRemoteListId(Long remoteListId) {
        this.remoteListId = remoteListId;
    }

    //// getters ////

    Long getRemoteListId() {
        return remoteListId;
    }

    String getListName() {
        return listName;
    }

    String getListDescription() {
        return listDescription;
    }

    String getRecipientListId() {
        return recipientListId;
    }

    List<UserDetails> getUserDetails() {
        return userDetails;
    }

    String getAdAccountId() {
        return adAccountId;
    }

    long getOrgId() {
        return orgId;
    }
}
