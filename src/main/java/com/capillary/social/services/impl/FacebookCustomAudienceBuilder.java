package com.capillary.social.services.impl;

import com.capillary.social.CreateCustomAudienceListResponse;
import com.capillary.social.UserDetails;
import com.capillary.social.library.api.OrgConfigurations;
import com.capillary.social.model.FBAudienceList;
import com.capillary.social.model.FacebookAdsConfigrations;
import com.capillary.social.services.api.CustomAudienceListBuider;
import com.capillary.social.utils.Guard;
import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.AdAccount;
import com.facebook.ads.sdk.CustomAudience;
import com.facebook.ads.sdk.CustomAudienceStatus;
import com.facebook.ads.sdk.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 4/9/17
 */
public class FacebookCustomAudienceBuilder implements CustomAudienceListBuider {
	private static Logger logger = LoggerFactory.getLogger(FacebookCustomAudienceBuilder.class);

	@Override
	public String build(List<UserDetails> userDetailsList, String listName, String listDescription, long orgId, String adsAccountId) throws APIException {
		logger.info("received calls to build custom audience list");
		FacebookAdsConfigrations facebookAdsConfigrations = OrgConfigurations.getFacebookConfigrations(orgId);
		Guard.notNullOrEmpty(facebookAdsConfigrations.getAccessToken(), "facebook access token");
		Guard.notNullOrEmpty(facebookAdsConfigrations.getAdsAccountId(), "facebook ads account id");
		APIContext context = new APIContext(facebookAdsConfigrations.getAccessToken()).enableDebug(true);
		logger.info("trying to create empty custom audience list with name :{} and description \"{}\"", listName, listDescription);
		CustomAudience customAudience = new AdAccount(facebookAdsConfigrations.getAdsAccountId(), context).createCustomAudience()
				.setName(listName)
				.setSubtype(CustomAudience.EnumSubtype.VALUE_CUSTOM)
				.setDescription(listDescription)
				.execute();
		logger.info("created empty custom audience list with id \"{}\"", customAudience.getId());
		FBAudienceList fbAudienceList = new FBAudienceList();
		fbAudienceList.addAll(userDetailsList);
		logger.info("trying to add {} users to the list", userDetailsList.size());
		logger.info("facebook userdetails before sending",fbAudienceList.toString());
		User users = customAudience.createUser().setPayload(fbAudienceList.toString()).execute();
		logger.debug("response from facebook api :{}", users.getRawResponseAsJsonObject());
		return customAudience.getId();
	}

	@Override
	public void remove(List<UserDetails> userDetailsList, String audienceListId, long orgId, String adsAccountId) throws APIException {
		FacebookAdsConfigrations facebookAdsConfigrations = OrgConfigurations.getFacebookConfigrations(orgId);
		Guard.notNullOrEmpty(facebookAdsConfigrations.getAccessToken(), "facebook access token");
		Guard.notNullOrEmpty(facebookAdsConfigrations.getAdsAccountId(), "facebook ads account id");
		APIContext context = new APIContext(facebookAdsConfigrations.getAccessToken()).enableDebug(true);
		logger.info("trying to remove {} users from list : {} from account: {}", new Object[]{userDetailsList.size(), audienceListId, adsAccountId});
		FBAudienceList fbAudienceList = new FBAudienceList();
		fbAudienceList.addAll(userDetailsList);
		new CustomAudience(audienceListId, context).deleteUsers().setPayload(fbAudienceList.toString()).execute();
		logger.info("removed users from list");
	}
}
