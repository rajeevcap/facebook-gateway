package com.capillary.social.services.impl.builders;

import com.capillary.social.library.api.OrgConfigurations;
import com.capillary.social.model.FacebookAdsConfigurations;
import com.capillary.social.services.api.builders.AdsetInsightsReportBuilder;
import com.capillary.social.services.impl.FacebookConstants;
import com.capillary.social.utils.Guard;
import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.APINodeList;
import com.facebook.ads.sdk.AdAccount;
import com.facebook.ads.sdk.AdsInsights;
import edu.emory.mathcs.backport.java.util.Arrays;

import java.util.List;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 26/10/17
 */
public class FacebookAdsetInsightsReportBuilder implements AdsetInsightsReportBuilder {
	private static final List<String> fields= Arrays.asList(new String[]{
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
	public String build(long orgId, String adsetId) throws APIException {
		FacebookAdsConfigurations facebookAdsConfigurations = OrgConfigurations.getFacebookConfigrations(orgId);
		Guard.notNullOrEmpty(facebookAdsConfigurations.getAdsAccountId(),"facebookadsAccountId");
		Guard.notNullOrEmpty(facebookAdsConfigurations.getAccessToken(),"facebookAccessToken");
		APIContext context = new APIContext(facebookAdsConfigurations.getAccessToken()).enableDebug(true);
		APINodeList<AdsInsights> adsInsights = new AdAccount(facebookAdsConfigurations.getAdsAccountId(), context).
				getInsights()
				.setLevel(AdsInsights.EnumLevel.VALUE_ADSET).requestFields(fields).execute();
		for (AdsInsights adsInsight:adsInsights) {
			if(adsetId.equals(adsInsight.getFieldAdsetId())){
				return adsInsight.toString();
			}
		}
		return null;
	}
}
