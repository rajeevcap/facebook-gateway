package com.capillary.social;

import com.capillary.registrarsdk.CapillaryRegistrarStub;
import com.capillary.social.external.impl.FacebookServiceListener;
import com.facebook.ads.sdk.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jdk.nashorn.api.scripting.JSObject;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 4/9/17
 */
public class FbAdsAPITest {
	@Test
	@Ignore
	public void getAccess() throws APIException, NoSuchAlgorithmException {
		String access_token = "EAACARaJySOoBAJieBZBvjur0weIRGoMgE77hSMOjQsR7Mdy2cZADbHrJlbSkIceOlbp6ivnUX7QT8Pc4joJRTXmLZBkcBuj92j7smwD8Ye17qAF0ZBC4FUx4mwKOKqBMzMaBmAC8xBbmxD849sOaZBbNRHruSZACehfUZCBQx7I8qUSfZA3ynNtLEWkSzul9ilXEnbKkbsKre6CqPdt6FeiM6zPa2e5rqkwZD";
		String app_secret = "80a271770c807bd5c531fe76cb74b422";
		String ad_account_id = "118772362192973";
		String audience_name = "testaudience";
		String audience_retention_days = "30";
		String pixel_id = "1695804217395702";
		APIContext context = new APIContext(access_token).enableDebug(true);

		Campaign campaign = new AdAccount(ad_account_id, context).createCampaign()
				.setName("My Campaign")
				.setBuyingType("AUCTION")
				.setObjective(Campaign.EnumObjective.VALUE_LINK_CLICKS)
				.setStatus(Campaign.EnumStatus.VALUE_PAUSED)
				.execute();
		String campaign_id = campaign.getId();
		CustomAudience customAudience = new AdAccount(ad_account_id, context).createCustomAudience()
				.setName("My new CA")
				.setSubtype(CustomAudience.EnumSubtype.VALUE_CUSTOM)
				.setDescription("People who bought from my website")
				.execute();
		String custom_audience_id = customAudience.getId();
		customAudience.createUser().setPayload("{\"schema\":\"EMAIL_SHA256\",\"data\":"+this.createDemoUserDetails()+"}")
				.execute();
	}

	public String  createDemoUserDetails() throws NoSuchAlgorithmException {

		List<String> emailAddress = new ArrayList<String>();
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		emailAddress.add(new String(bytesToHex(digest.digest("ablelive.in".getBytes()))));
		emailAddress.add(new String(bytesToHex(digest.digest("test1@test.com".getBytes()))));
		emailAddress.add(new String(bytesToHex(digest.digest("able.johnson@gmail.com".getBytes()))));
		Gson gson = new Gson();
		return gson.toJson(emailAddress);

	}
	public static String bytesToHex(byte[] bytes) {
		StringBuffer result = new StringBuffer();
		for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
		return result.toString();
	}
}
