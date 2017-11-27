package com.capillary.social.services.impl.builders;

import com.capillary.social.CustomAudienceList;
import com.capillary.social.commons.model.SocialAudienceList;
import com.capillary.social.services.api.builders.ISocialListAccessor;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.List;

import static com.capillary.social.services.impl.builders.SocialProcessorHelper.socialAudienceListDao;

/**
 * Created by rajeev on 6/11/17.
 */
public abstract class SocialListAccessor implements ISocialListAccessor {

    private static final Logger logger = LoggerFactory.getLogger(SocialListAccessor.class);

    long orgId;
    boolean clearCache;

    String adAccountId;

    private List<SocialAudienceList> socialAudienceLists;

    protected abstract void prepareAPICallContext() throws ConfigurationLoadException, OAuthException, ConfigurationException, ValidationException;

    protected abstract List<CustomAudienceList> getAllSocialList() throws RemoteException;

    protected abstract void fetchAdAccountId();

    private void setFields(long orgId, boolean clearCache) {
        this.orgId = orgId;
        this.clearCache = clearCache;
    }

    @Override
    public List<CustomAudienceList> getAll(long orgId, boolean clearCache) throws ConfigurationLoadException, OAuthException, ValidationException, RemoteException, ConfigurationException {
        setFields(orgId, clearCache);
        prepareAPICallContext();
        fetchAdAccountId();
        return getAllSocialList();
    }

    @Override
    public CustomAudienceList getList(long orgId, String remoteListId) throws ConfigurationLoadException, OAuthException, RemoteException, ValidationException, ConfigurationException {
        List<CustomAudienceList> audienceLists = getAll(orgId, false);
        for(CustomAudienceList audienceList : audienceLists) {
            if(audienceList.getRemoteListId().equalsIgnoreCase(remoteListId)) {
                return audienceList;
            }
        }
        logger.info("could not find audience list with id {}", remoteListId);
        return null;
    }

    List<SocialAudienceList> getExistingAudienceLists(String adAccountId, long orgId, String type) {
        if(socialAudienceLists == null) {
            socialAudienceLists = socialAudienceListDao.findByAccountIdOrgId(adAccountId, orgId, type);
        }
        return socialAudienceLists;
    }

}
