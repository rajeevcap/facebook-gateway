package com.capillary.social.services.impl.builders;

import com.capillary.social.SocialAdSet;
import com.capillary.social.services.api.builders.ISocialAdBatchAccessor;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponseException;
import com.google.api.ads.adwords.lib.utils.ReportException;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by rajeev on 10/11/17.
 */
public abstract class SocialAdBatchAccessor implements ISocialAdBatchAccessor {

    private static final Logger logger = LoggerFactory.getLogger(SocialListAccessor.class);

    long orgId;

    protected abstract void prepareAPICallContext() throws ConfigurationLoadException, ValidationException, OAuthException, ConfigurationException;

    protected abstract List<SocialAdSet> getAllAdBatch() throws RemoteException, ReportDownloadResponseException, ReportException;

    private void setFields(long orgId) {
        this.orgId = orgId;
    }

    @Override
    public List<SocialAdSet> getAll(long orgId) throws RemoteException, ConfigurationLoadException, OAuthException, ValidationException, ReportDownloadResponseException, ReportException, ConfigurationException {
        setFields(orgId);
        prepareAPICallContext();
        return getAllAdBatch();
    }

}
