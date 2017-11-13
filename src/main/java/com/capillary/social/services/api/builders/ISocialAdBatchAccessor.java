package com.capillary.social.services.api.builders;

import com.capillary.social.SocialAdSet;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponseException;
import com.google.api.ads.adwords.lib.utils.ReportException;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by rajeev on 10/11/17.
 * Inteface for building and saving social groups
 */
public interface ISocialAdBatchAccessor {

    /**
     * @param orgId
     * @return list of social ad set
     */
    public List<SocialAdSet> getAll(long orgId) throws RemoteException, ConfigurationLoadException, OAuthException, ValidationException, ReportDownloadResponseException, ReportException;

}
