package com.capillary.social.services.impl.builders;

import com.capillary.social.AdSetStatus;
import com.capillary.social.SocialAdSet;
import com.capillary.social.library.api.OrgConfigurations;
import com.capillary.social.model.FacebookAdsConfigurations;
import com.capillary.social.services.api.builders.AdsetsReportsBuilder;
import com.capillary.social.utils.Guard;
import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.APINodeList;
import com.facebook.ads.sdk.AdAccount;
import com.facebook.ads.sdk.AdSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 18/10/17
 */
public class FacebookAdsetsReportBuilder implements AdsetsReportsBuilder{
	private static Logger logger= LoggerFactory.getLogger(FacebookAdsetsReportBuilder.class);
	@Override
	public List<SocialAdSet> buildAll(long orgId) {
		logger.info("received calls to build report of social adsets");
		FacebookAdsConfigurations facebookAdsConfigurations = OrgConfigurations.getFacebookConfigrations(orgId);
		Guard.notNullOrEmpty(facebookAdsConfigurations.getAccessToken(), "facebook access token");
		Guard.notNullOrEmpty(facebookAdsConfigurations.getAdsAccountId(), "facebook ads account id");
		List<SocialAdSet> socialAdSets = fetchSocialAds(facebookAdsConfigurations.getAdsAccountId(), facebookAdsConfigurations.getAccessToken());
		return socialAdSets;
	}

	@Override
	public SocialAdSet build(long orgId, String listId) {
		return null;
	}

	private List<SocialAdSet> fetchSocialAds(String advertAccountId,String accessToken){
		List<SocialAdSet> socialAdSets = new ArrayList<>();
		try {
			APIContext context = new APIContext(accessToken);
			APINodeList<AdSet> adSets = new AdAccount(advertAccountId, context).getAdSets().setParam("date_format", "U").requestAllFields().execute();
			for (AdSet adSet:adSets){
				SocialAdSet socialAdSet = new SocialAdSet();
				socialAdSet.setId(adSet.getId());
				socialAdSet.setCampaignId(adSet.getFieldCampaignId());
				socialAdSet.setStartTime(Long.valueOf(adSet.getFieldStartTime()));
				socialAdSet.setName(adSet.getFieldName());
				socialAdSet.setEndTime(Long.valueOf(adSet.getFieldEndTime()));
				socialAdSet.setStatus(AdSetStatus.valueOf(adSet.getFieldStatus().toString()));
				socialAdSets.add(socialAdSet);
			}
		}
		catch(APIException e){
			logger.info("error occurred while fetching social adset from faceook",e);
		}
		return socialAdSets;
	}
}
