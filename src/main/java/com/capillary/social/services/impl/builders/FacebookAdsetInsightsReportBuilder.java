package com.capillary.social.services.impl.builders;

import com.capillary.social.AdInsight;
import com.capillary.social.SocialChannel;
import com.capillary.social.commons.dao.api.CommunicationDetailsDao;
import com.capillary.social.commons.dao.api.FacebookAdsetInsightsDao;
import com.capillary.social.commons.dao.api.MessageAdsetMappingDao;
import com.capillary.social.commons.model.CommunicationDetails;
import com.capillary.social.handler.ApplicationContextAwareHandler;
import com.capillary.social.library.api.OrgConfigurations;
import com.capillary.social.model.FBFilter;
import com.capillary.social.model.FacebookAdsConfigurations;
import com.capillary.social.services.api.builders.AdsetInsightsReportBuilder;
import com.capillary.social.utils.Guard;
import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.APINodeList;
import com.facebook.ads.sdk.AdAccount;
import com.facebook.ads.sdk.AdsInsights;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.List;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 26/10/17
 */
public class FacebookAdsetInsightsReportBuilder implements AdsetInsightsReportBuilder {

	private static Logger logger = LoggerFactory.getLogger(FacebookAdsetInsightsReportBuilder.class);
	private static final List<String> fields = Arrays.asList(new String[]{
			"account_currency",
			"account_id",
			"account_name",
			"action_values",
			"actions",
			"ad_id",
			"ad_name",
			"adset_id",
			"adset_name",
			"buying_type",
			"call_to_action_clicks",
			"campaign_id",
			"campaign_name",
			"canvas_avg_view_percent",
			"canvas_avg_view_time",
			"canvas_component_avg_pct_view",
			"clicks",
			"cost_per_10_sec_video_view",
			"cost_per_action_type",
			"cost_per_estimated_ad_recallers",
			"cost_per_inline_link_click",
			"cost_per_inline_post_engagement",
			"cost_per_outbound_click",
			"cost_per_total_action",
			"cost_per_unique_action_type",
			"cost_per_unique_click",
			"cost_per_unique_inline_link_click",
			"cost_per_unique_outbound_click",
			"cpc",
			"cpm",
			"cpp",
			"ctr",
			"date_start",
			"date_stop",
			"estimated_ad_recall_rate",
			"estimated_ad_recallers",
			"frequency",
			"impressions",
			"inline_link_click_ctr",
			"inline_link_clicks",
			"inline_post_engagement",
			"mobile_app_purchase_roas",
			"objective",
			"outbound_clicks",
			"outbound_clicks_ctr",
			"place_page_name",
			"reach",
			"relevance_score",
			"social_clicks",
			"social_impressions",
			"social_reach",
			"social_spend",
			"spend",
			"total_action_value",
			"total_actions",
			"total_unique_actions",
			"unique_actions",
			"unique_clicks",
			"unique_ctr",
			"unique_inline_link_click_ctr",
			"unique_inline_link_clicks",
			"unique_link_clicks_ctr",
			"unique_outbound_clicks",
			"unique_outbound_clicks_ctr",
			"unique_social_clicks",
			"video_10_sec_watched_actions",
			"video_15_sec_watched_actions",
			"video_30_sec_watched_actions",
			"video_avg_percent_watched_actions",
			"video_avg_time_watched_actions",
			"video_p100_watched_actions",
			"video_p25_watched_actions",
			"video_p50_watched_actions",
			"video_p75_watched_actions",
			"video_p95_watched_actions",
			"website_ctr",
			"website_purchase_roas"
	});

	@Override
	public AdInsight build(long orgId, String adsetId, boolean clearCache) throws APIException {
		logger.info("building adset insights for org {},adsetId: {} , clearcache {}", new Object[]{orgId, adsetId, clearCache});
		FacebookAdsConfigurations facebookAdsConfigurations = OrgConfigurations.getFacebookConfigrations(orgId);
		Guard.notNullOrEmpty(facebookAdsConfigurations.getAdsAccountId(), "facebookadsAccountId");
		ApplicationContext applicationContext = ApplicationContextAwareHandler.getApplicationContext();
		FacebookAdsetInsightsDao facebookAdsetInsightsDao = (FacebookAdsetInsightsDao) applicationContext.getBean("facebookAdsetInsightsDaoImpl");
		com.capillary.social.commons.model.AdsInsights adsInsights = facebookAdsetInsightsDao.findByAdsetId(orgId, com.capillary.social.commons.model.AdsInsights.Type.FACEBOOK, facebookAdsConfigurations.getAdsAccountId(), adsetId);
		if (adsInsights == null) {
			logger.info("no ads insights found in the local");
		}
		if (adsInsights == null || clearCache) {
			logger.info("fetching adsInsights from facebook");
			Guard.notNullOrEmpty(facebookAdsConfigurations.getAccessToken(), "facebookAccessToken");
			APIContext context = new APIContext(facebookAdsConfigurations.getAccessToken()).enableDebug(true);
			FBFilter fbFilter = new FBFilter();
			fbFilter.addFilter("adset.id", FBFilter.Operator.EQUAL, adsetId);
			APINodeList<AdsInsights> adsInsightsList = new AdAccount(facebookAdsConfigurations.getAdsAccountId(), context).
					getInsights()
					.setLevel(AdsInsights.EnumLevel.VALUE_ADSET).setFiltering(fbFilter.toString()).requestFields(fields).setParam("date_preset", "lifetime").execute();
			logger.debug("adsInsightslist from facebook " + adsInsightsList.toString());
			for (AdsInsights adsInsight : adsInsightsList) {
				if (adsetId.equals(adsInsight.getFieldAdsetId())) {
					adsInsights = converttoDbModel(adsInsight, orgId);
					CommunicationDetails communicationDetails = getCommunicationDetails(orgId,adsetId);
					if(communicationDetails==null){
						logger.error("could not find a cd entry linked with adset id {} ",adsetId);
					}
					else{
						adsInsights.setGuid(communicationDetails.getGuid());
						adsInsights.setCommunicationDetailsId(communicationDetails.getId());
					}
					facebookAdsetInsightsDao.create(adsInsights);
					break;
				}
				logger.info("skipped insight of adset number" + adsInsight.getFieldAdsetId());
			}
		}
		if (adsInsights == null) {
			return null;
		}
		return convertToThriftObject(adsInsights);
	}
	private CommunicationDetails getCommunicationDetails(long orgId,String adsetId){
		ApplicationContext applicationContext = ApplicationContextAwareHandler.getApplicationContext();
		MessageAdsetMappingDao messageAdsetMappingDao = (MessageAdsetMappingDao) applicationContext.getBean("messageAdsetMappingDaoImpl");
		CommunicationDetailsDao communicationDetailsDao = (CommunicationDetailsDao) applicationContext.getBean("communicationDetailsDaoImpl");
		String guid = messageAdsetMappingDao.getGUIDfromAdsetMapping(orgId, adsetId);
		CommunicationDetails communicationDetails = communicationDetailsDao.findByGuid(orgId, guid);
		if(communicationDetails==null){
			logger.error("could not fetch communication details for orgid {} adsetis {}",new Object[]{orgId,adsetId});
			throw new RuntimeException("could not fetch communication details");
		}
		return communicationDetails;
	}

	private static com.capillary.social.commons.model.AdsInsights converttoDbModel(AdsInsights adsInsights, long orgId) {
		com.capillary.social.commons.model.AdsInsights dbObject = new com.capillary.social.commons.model.AdsInsights();
		dbObject.setOrgId(orgId);
		dbObject.setType(com.capillary.social.commons.model.AdsInsights.Type.FACEBOOK);
		dbObject.setAdsAccountId(adsInsights.getFieldAccountId());
		dbObject.setAdsetId(adsInsights.getFieldAdsetId());
		dbObject.setInsights(adsInsights.getRawResponse());
		dbObject.setActive(true);
		dbObject.setCachedOn(new Date());
		dbObject.setAutoUpdateTime(new Date());
		return dbObject;
	}

	private static AdInsight convertToThriftObject(com.capillary.social.commons.model.AdsInsights dbObject) {
		AdInsight adInsight = new AdInsight();
		adInsight.setOrgId(dbObject.getOrgId());
		adInsight.setSocialChannel(SocialChannel.valueOf(dbObject.getType().name().toLowerCase()));
		adInsight.setAdsetId(dbObject.getAdsetId());
		adInsight.setInsights(dbObject.getInsights());
		adInsight.setCachedon(dbObject.getCachedOn().getTime());
		return adInsight;
	}
}
