package com.capillary.social.services.impl.builders;

import com.capillary.social.SocialAdSet;
import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.v201710.rm.AdwordsUserListServiceInterface;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.client.reporting.ReportingConfiguration;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.jaxb.v201710.*;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponse;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponseException;
import com.google.api.ads.adwords.lib.utils.ReportException;
import com.google.api.ads.adwords.lib.utils.v201710.ReportDownloaderInterface;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rajeev on 10/11/17.
 */
public class GoogleAdReportAccessor extends SocialAdReportAccessor {

    private static final Logger logger = LoggerFactory.getLogger(GoogleListAccessor.class);

    private ReportDownloaderInterface reportDownloader;
    private AdWordsSession session;

    @Override
    protected void generateAuthentication() throws ConfigurationLoadException, ValidationException, OAuthException {
        Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(OfflineCredentials.Api.ADWORDS).fromFile().build().generateCredential();
        session = new AdWordsSession.Builder().fromFile().withOAuth2Credential(oAuth2Credential).build();
        AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();
        reportDownloader = adWordsServices.getUtility(session, ReportDownloaderInterface.class);
    }

    @Override
    protected void generateReport() throws IOException, ReportDownloadResponseException, ReportException {
        logger.info("received call for generate report");
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.YESTERDAY);
        reportDefinition.setReportType(ReportDefinitionReportType.CRITERIA_PERFORMANCE_REPORT);
        reportDefinition.setDownloadFormat(DownloadFormat.CSV);
        logger.debug("report definition created {}", reportDefinition);
        ReportingConfiguration reportingConfiguration = new ReportingConfiguration.Builder().skipReportHeader(false).skipColumnHeader(false).skipReportSummary(false).includeZeroImpressions(false).build();
        session.setReportingConfiguration(reportingConfiguration);
        Selector selector = new Selector();
        selector.getFields().addAll(Arrays.asList("CampaignId","AdGroupId","Id","CriteriaType","Criteria","FinalUrls","Impressions","Clicks","Cost"));
        reportDefinition.setSelector(selector);
        ReportDownloadResponse reportDownloadResponse = reportDownloader.downloadReport(reportDefinition);
        reportDownloadResponse.saveToFile(getReportFilePath());
        logger.info("Report successfully downloaded to {}", getReportFilePath());
    }

}
