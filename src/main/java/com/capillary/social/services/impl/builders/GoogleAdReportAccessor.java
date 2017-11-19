package com.capillary.social.services.impl.builders;

import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.capillary.social.AdInsight;
import com.capillary.social.commons.model.AdsInsights;
import com.google.api.ads.adwords.lib.client.reporting.ReportingConfiguration;
import com.google.api.ads.adwords.lib.jaxb.v201710.*;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponse;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponseException;
import com.google.api.ads.adwords.lib.utils.ReportException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import static com.capillary.social.services.impl.builders.GoogleProcessorHelper.GoogleAPIKeys.GOOGLE_ADS_CLIENT_CUSTOMER_ID;

/**
 * Created by rajeev on 10/11/17.
 */
public class GoogleAdReportAccessor extends SocialAdReportAccessor {

    private static final Logger logger = LoggerFactory.getLogger(GoogleListAccessor.class);

    private GoogleProcessorHelper googleHelper;

    @Override
    protected void prepareAPICallContext() throws ConfigurationLoadException, ValidationException, OAuthException, ConfigurationException {
        googleHelper = new GoogleProcessorHelper(orgId);
        googleHelper.authenticate();
        googleHelper.generateServices();
    }

    @Override
    protected AdInsight generateReport() throws IOException, ReportDownloadResponseException, ReportException {
        logger.info("received call for generate report");
        ReportingConfiguration reportingConfiguration = new ReportingConfiguration.Builder().skipReportHeader(false).skipColumnHeader(false).skipReportSummary(false).includeZeroImpressions(false).build();
        googleHelper.session.setReportingConfiguration(reportingConfiguration);
        Selector selector = getSelector();
        ReportDefinition reportDefinition = getReportDefinition();
        reportDefinition.setSelector(selector);
        ReportDownloadResponse reportDownloadResponse = googleHelper.reportDownloader.downloadReport(reportDefinition);
        String reportString = reportDownloadResponse.getAsString();
        logger.info("report download response : {}", reportString);
        AdsInsights adsInsights = getAdInsightFromDownloadResponse(reportString);
        SocialProcessorHelper.facebookAdsetInsightsDao.create(adsInsights);
        return googleHelper.convertToThriftObject(adsInsights);
    }

    @Override
    protected void fetchAdAccountId() {
        adAccountId = googleHelper.keyValueMap.get(GOOGLE_ADS_CLIENT_CUSTOMER_ID.name());
    }

    private Selector getSelector() {
        Selector selector = new Selector();
        selector.getFields().addAll(Arrays.asList("CampaignId","AdGroupId","Id","CriteriaType","Criteria","FinalUrls","Impressions","Clicks","Cost"));
        return selector;
    }

    private ReportDefinition getReportDefinition() {
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.ALL_TIME);
        reportDefinition.setReportType(ReportDefinitionReportType.CRITERIA_PERFORMANCE_REPORT);
        reportDefinition.setDownloadFormat(DownloadFormat.XML);
        return reportDefinition;
    }

    private AdsInsights getAdInsightFromDownloadResponse(String report) {
        AdsInsights adsInsights = new AdsInsights();
        adsInsights.setOrgId(orgId);
        adsInsights.setType(AdsInsights.Type.GOOGLE);
        adsInsights.setAdsAccountId(adAccountId);
        adsInsights.setAdsetId(adSetId);
        adsInsights.setInsights(report);
        adsInsights.setCachedOn(new Date());
        adsInsights.setAutoUpdateTime(new Date());
        return adsInsights;
    }

}
