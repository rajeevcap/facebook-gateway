package com.capillary.social.services.api.builders;

import com.capillary.social.CustomAudienceList;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import org.apache.commons.configuration.ConfigurationException;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Interface for getting list of created social groups
 * Created by rajeev on 6/11/17.
 */
public interface ISocialListAccessor {

    /**
     * fetch all list belonging to an org
     * @param orgId
     * @param clearCache
     * @return
     */
    public List<CustomAudienceList> getAll(long orgId, boolean clearCache) throws ConfigurationLoadException, OAuthException, ValidationException, RemoteException, ConfigurationException;

    /**
     * fetch specific list with given social list id
     * @param orgId
     * @param remoteListId id of social list
     * @return
     */
    public CustomAudienceList getList(long orgId, String remoteListId) throws ConfigurationLoadException, OAuthException, RemoteException, ValidationException, ConfigurationException;

}
