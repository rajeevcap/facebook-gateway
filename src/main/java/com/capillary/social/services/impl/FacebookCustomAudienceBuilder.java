package com.capillary.social.services.impl;

import com.capillary.social.CreateCustomAudienceListResponse;
import com.capillary.social.UserDetails;
import com.capillary.social.model.FBAudienceList;
import com.capillary.social.services.api.CustomAudienceListBuider;
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

	//TODO get the access token from db
	private final String access_token = "EAACARaJySOoBAJieBZBvjur0weIRGoMgE77hSMOjQsR7Mdy2cZADbHrJlbSkIceOlbp6ivnUX7QT8Pc4joJRTXmLZBkcBuj92j7smwD8Ye17qAF0ZBC4FUx4mwKOKqBMzMaBmAC8xBbmxD849sOaZBbNRHruSZACehfUZCBQx7I8qUSfZA3ynNtLEWkSzul9ilXEnbKkbsKre6CqPdt6FeiM6zPa2e5rqkwZD";
	String ad_account_id = "118772362192973";

	@Override
	public String build(List<UserDetails> userDetailsList, String listName, String listDescription, long orgId, String adsAccountId) throws APIException {
		APIContext context = new APIContext(access_token).enableDebug(true);
		logger.info("trying to create empty custom audience list with name :{} and description \"{}\"", listName, listDescription);
		CustomAudience customAudience = new AdAccount(ad_account_id, context).createCustomAudience()
				.setName(listName)
				.setSubtype(CustomAudience.EnumSubtype.VALUE_CUSTOM)
				.setDescription(listDescription)
				.execute();
		logger.info("created empty custom audience list with id \"{}\"",customAudience.getId());
		FBAudienceList fbAudienceList = new FBAudienceList();
		fbAudienceList.addAll(userDetailsList);
		logger.info("trying to add {} users to the list",userDetailsList.size());
		User users = customAudience.createUser().setPayload(fbAudienceList.toString()).execute();
		logger.debug("response from facebook api :{}",users.getRawResponseAsJsonObject());
		return customAudience.getId();
	}

	@Override
	public void remove(List<UserDetails> userDetailsList, String audienceListId, long orgId, String adsAccountId) throws APIException {
		APIContext context = new APIContext(access_token).enableDebug(true);
		logger.info("trying to remove {} users from list : {} from account: {}",new Object[]{userDetailsList.size(),audienceListId, adsAccountId});
		FBAudienceList fbAudienceList = new FBAudienceList();
		fbAudienceList.addAll(userDetailsList);
		new CustomAudience(audienceListId,context).deleteUsers().setPayload(fbAudienceList.toString()).execute();
		logger.info("removed users from list");
	}
}
