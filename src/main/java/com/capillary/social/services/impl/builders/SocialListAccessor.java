package com.capillary.social.services.impl.builders;

import com.capillary.social.CustomAudienceList;
import com.capillary.social.commons.dao.api.SocialAudienceListDao;
import com.capillary.social.commons.model.SocialAudienceList;
import com.capillary.social.handler.ApplicationContextAwareHandler;
import com.capillary.social.services.api.builders.ISocialListAccessor;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rajeev on 6/11/17.
 */
public abstract class SocialListAccessor implements ISocialListAccessor {

    private static final Logger logger = LoggerFactory.getLogger(SocialListAccessor.class);

    private long orgId;
    private String remoteListId;
    private String adAccountId;
    private static SocialAudienceListDao socialAudienceListDao;

    protected abstract void generateAuthentication() throws ConfigurationLoadException, ValidationException, OAuthException;

    protected abstract List<CustomAudienceList> getAllSocialList() throws RemoteException;

    private void setFields(int orgId) {
        this.orgId = orgId;
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
    public List<CustomAudienceList> getAll(int orgId) throws ConfigurationLoadException, OAuthException, ValidationException, RemoteException {
        setFields(orgId);
        generateAuthentication();
        return getAllSocialList();
    }

    @Override
    public CustomAudienceList getList(int orgId, String remoteListId) throws ConfigurationLoadException, OAuthException, RemoteException, ValidationException {
        List<CustomAudienceList> audienceLists = getAll(orgId);
        for(CustomAudienceList audienceList : audienceLists) {
            if(audienceList.getRemoteListId().equalsIgnoreCase(remoteListId)) {
                return audienceList;
            }
        }
        logger.info("could not find audience list with id {}", remoteListId);
        return null;
    }

    List<SocialAudienceList> getExistingListFromDatabase(String adAccountId, long orgId, String type) {
        return socialAudienceListDao.findByAccountIdOrgId(adAccountId, orgId, type);
    }

    //// helper static functions ////

    static Map<String, String> getLocalRemoteListMap (List<SocialAudienceList> audienceLists) {
        Map<String, String> localRemoteListMap = new HashMap<>();
        for(SocialAudienceList audienceList : audienceLists) {
            localRemoteListMap.put(audienceList.getCampaignReceipientListId(), audienceList.getRemoteListId());
        }
        return localRemoteListMap;
    }

    //// getters ////

    String getAdAccountId() {
        return adAccountId;
    }

    long getOrgId() {
        return orgId;
    }

    public static SocialAudienceListDao getSocialAudienceListDao() {
        return socialAudienceListDao;
    }

}