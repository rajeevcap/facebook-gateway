package com.capillary.social.services.impl.builders;

import com.capillary.social.commons.model.CommunicationDetails;
import com.capillary.social.utils.GoogleReportParameter;
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
import static com.capillary.social.services.impl.builders.SocialProcessorHelper.*;
import static com.capillary.social.utils.FacebookGatewayUtils.merge;

/**
 * Created by rajeev on 10/11/17.
 */
public class GoogleAdReportAccessor extends SocialAdReportAccessor {

    private static final Logger logger = LoggerFactory.getLogger(GoogleAdGroupAccessor.class);

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
        AdsInsights report = facebookAdsetInsightsDao.findByAdsetId(orgId, AdsInsights.Type.GOOGLE, adAccountId, adSetId);
        if(report == null) {
            logger.info("no existing report found for org {} type {} ad account id {} and sd set id {}", new Object[]{orgId, AdsInsights.Type.GOOGLE, adAccountId, adSetId});
        }
        if(report == null || clearCache) {
            ReportingConfiguration reportingConfiguration = new ReportingConfiguration.Builder().skipReportHeader(false).skipColumnHeader(false).skipReportSummary(false).includeZeroImpressions(false).build();
            googleHelper.session.setReportingConfiguration(reportingConfiguration);
            Selector selector = getSelector();
            ReportDefinition reportDefinition = getReportDefinition();
            reportDefinition.setSelector(selector);
            ReportDownloadResponse reportDownloadResponse = googleHelper.reportDownloader.downloadReport(reportDefinition);
            String reportXML = reportDownloadResponse.getAsString();
            String reportJson = xmlToJsonParser(reportXML);
            logger.info("report download response json : {}", reportJson);
            report = getAdInsightFromDownloadResponse(reportJson);
            facebookAdsetInsightsDao.create(report);
        }
        if(report == null) {
            return null;
        }
        return googleHelper.convertToThriftObject(report);
    }

    @Override
    protected void fetchAdAccountId() {
        adAccountId = googleHelper.keyValueMap.get(GOOGLE_ADS_CLIENT_CUSTOMER_ID.name());
        logger.debug("adset account id fetched : {}", adAccountId);
    }

    @Override
    protected void fetchCommunicationDetails() {
        String guid = messageAdsetMappingDao.getGUIDfromAdsetMapping(orgId, adSetId);
        message = communicationDetailsDao.findByGuid(orgId, guid);
        if(message ==null){
            logger.error("could not fetch communication details for orgid {} adset id {}",new Object[]{orgId, adSetId});
            throw new RuntimeException("could not fetch communication details");
        }
    }

    private Selector getSelector() {
        Selector selector = new Selector();
        selector.getFields().addAll(Arrays.asList(merge(GoogleReportParameter.getAttributes(), GoogleReportParameter.getMetric(), GoogleReportParameter.getSegments())));
        return selector;
    }

    private ReportDefinition getReportDefinition() {
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setReportName("Campaign performance report #" + System.currentTimeMillis());
        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.ALL_TIME);
        reportDefinition.setReportType(ReportDefinitionReportType.CAMPAIGN_PERFORMANCE_REPORT);
        reportDefinition.setDownloadFormat(DownloadFormat.XML);
        return reportDefinition;
    }

    private AdsInsights getAdInsightFromDownloadResponse(String report) {
        AdsInsights adsInsights = new AdsInsights();
        adsInsights.setGuid(message.getGuid());
        adsInsights.setCommunicationDetailsId(message.getId());
        adsInsights.setOrgId(orgId);
        adsInsights.setType(AdsInsights.Type.GOOGLE);
        adsInsights.setAdsAccountId(adAccountId);
        adsInsights.setAdsetId(adSetId);
        adsInsights.setInsights(report);
        adsInsights.setCachedOn(new Date());
        adsInsights.setAutoUpdateTime(new Date());
        logger.debug("ad insight created {}", adsInsights.toString());
        return adsInsights;
    }

}
