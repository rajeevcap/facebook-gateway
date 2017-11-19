package com.capillary.social.services.impl.builders;

import com.capillary.social.services.api.builders.ISocialAdReportAccessor;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponseException;
import com.google.api.ads.adwords.lib.utils.ReportException;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by rajeev on 10/11/17.
 */
public abstract class SocialAdReportAccessor extends SocialProcessor implements ISocialAdReportAccessor {

    private static final Logger logger = LoggerFactory.getLogger(SocialListAccessor.class);

    private long orgId;
    private String adSetId;
    private boolean clearCache;
    private String reportFilePath;

    protected abstract void generateAuthentication() throws ConfigurationLoadException, ValidationException, OAuthException;

    protected abstract void generateReport() throws IOException, ReportDownloadResponseException, ReportException;

    private void setFields(long orgId, String adSetId, boolean clearCache) {
        this.orgId = orgId;
        this.adSetId = adSetId;
        this.clearCache = clearCache;
        generateReportFilePath();
    }

    private void generateReportFilePath() {
        reportFilePath = System.getProperty("user.home") + File.separatorChar + "report123.csv";
        System.out.println("report file path " + reportFilePath);
    }

    @Override
    public void getAll(long orgId, String adSetId, boolean clearCache) throws ConfigurationLoadException, OAuthException, ValidationException, IOException, ReportDownloadResponseException, ReportException {
        setFields(orgId, adSetId, clearCache);
        generateAuthentication();
        generateReport();
    }

    //// getters ////

    String getReportFilePath() {
        return reportFilePath;
    }

    boolean fetchListFromAPICall() {
        return clearCache;
    }

}
