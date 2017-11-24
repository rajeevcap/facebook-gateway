package com.capillary.social.services.impl.builders;

import com.capillary.social.commons.model.CommunicationDetails;
import com.capillary.social.services.api.builders.ISocialAdReportAccessor;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.LoggerFactory;
import com.capillary.social.AdInsight;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponseException;
import com.google.api.ads.adwords.lib.utils.ReportException;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * Created by rajeev on 10/11/17.
 */
public abstract class SocialAdReportAccessor implements ISocialAdReportAccessor {

    private static final Logger logger = LoggerFactory.getLogger(SocialListAccessor.class);

    long orgId;
    String adSetId;
    boolean clearCache;
    String adAccountId;
    CommunicationDetails message;

    protected abstract void fetchAdAccountId();

    protected abstract void prepareAPICallContext() throws ConfigurationLoadException, ValidationException, OAuthException, ConfigurationException;

    protected abstract AdInsight generateReport() throws IOException, ReportDownloadResponseException, ReportException;

    protected abstract void fetchCommunicationDetails();

    private void setFields(long orgId, String adSetId, boolean clearCache) {
        this.orgId = orgId;
        this.adSetId = adSetId;
        this.clearCache = clearCache;
        fetchCommunicationDetails();
    }

    @Override
    public AdInsight getAll(long orgId, String adSetId, boolean clearCache) throws ConfigurationLoadException, OAuthException, ValidationException, IOException, ReportDownloadResponseException, ReportException, ConfigurationException {
        setFields(orgId, adSetId, clearCache);
        prepareAPICallContext();
        fetchAdAccountId();
        return generateReport();
    }

}
