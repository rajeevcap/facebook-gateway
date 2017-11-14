package com.capillary.social.external.impl;

import com.capillary.social.CreateCustomAudienceListResponse;
import com.capillary.social.CustomAudienceListDetails;
import com.capillary.social.FacebookException;
import com.capillary.social.GatewayResponseType;
import com.capillary.social.SocialAccountDetails;
import com.capillary.social.SocialChannel;
import com.capillary.social.UserDetails;
import com.capillary.social.commons.dao.impl.ConfigKeyValuesDaoImpl;
import com.capillary.social.handler.ApplicationContextAwareHandler;
import com.capillary.social.library.api.OrgConfigrationKeys;
import com.capillary.social.model.FBFilter;
import com.capillary.social.services.impl.factories.CustomAudienceListBuilderFactory;
import com.facebook.ads.sdk.*;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.apache.thrift.TException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 6/9/17
 */
@RunWith(MockitoJUnitRunner.class)
public class FacebookServiceListenerTest {
	@Mock
	ApplicationContext applicationContext;
	@Mock
	ConfigKeyValuesDaoImpl configKeyValuesDao;
	FacebookServiceListener facebookServiceListener;
	private static String accessToken = "EAACARaJySOoBAKpGkk5EsSZB2RgRNXnlzaATZBBIOfzaf9pzKL4IpLHguiZBf8cAF56Df0i2SutrZAeMgbIup8pdu42lM3yfDxSWsq7QwmPCNgFhAgpMqjRuJxEvpJSeUsVgtRCBvA5VWX2IysLM7ZBB3bW7vfcE6WIaeSZAigKCdmJoavXRkxfivMVt4ZB2GksHHoLbxrbYbZAZAdA9iHI0un2labbxddmMZD";
	private static String adsAccountId= "118772362192973";
	private static final String adaset="6079720416296";

	@Before
	public void setUp(){
		ApplicationContextAwareHandler applicationContextAwareHandler = new ApplicationContextAwareHandler();
		applicationContextAwareHandler.setApplicationContext(applicationContext);
		when(applicationContext.getBean("configKeyValuesDaoImpl")).thenReturn(configKeyValuesDao);
		when(configKeyValuesDao.findValueByName(OrgConfigrationKeys.FB_ADS_ACCOUNT_ID.name(),anyInt())).thenReturn(adsAccountId);
		when(configKeyValuesDao.findValueByName(OrgConfigrationKeys.FB_ADS_ACCESS_TOKEN.name(),anyInt())).thenReturn(accessToken);
		facebookServiceListener = new FacebookServiceListener();
	}

	@Test
	@Ignore
	public void createCustomAudienceListTest() throws TException, FacebookException {
		List<UserDetails> userDetailsList = new ArrayList<>();
		UserDetails userDetail1 = new UserDetails();
		userDetail1.setEmail("user1@example.com");
		userDetail1.setMobile("1234567890");
		UserDetails userDetail2 = new UserDetails();
		userDetail2.setEmail("user2@example.com");
		userDetail2.setMobile("1234567891");
		userDetailsList.add(userDetail1);
		userDetailsList.add(userDetail2);
		SocialAccountDetails socialAccountDetails = new SocialAccountDetails();
		socialAccountDetails.setChannel(SocialChannel.facebook);
		CustomAudienceListDetails customAudienceListDetails = new CustomAudienceListDetails();
		customAudienceListDetails.setDescription("testDescription");
		customAudienceListDetails.setName("testList");
		CreateCustomAudienceListResponse response = facebookServiceListener.createCustomList(userDetailsList, customAudienceListDetails, socialAccountDetails, 0,"test1", "thread1");
		assertEquals(GatewayResponseType.success, response.response);
	}

	@Test
	@Ignore
	public void getCustomAudiences() throws APIException {
		APIContext context = new APIContext(accessToken).enableDebug(true);
		List<String> customAudienceFields = new ArrayList<>();
		for (CustomAudience.EnumFields fieldsds: CustomAudience.EnumFields.values()) {
			customAudienceFields.add(fieldsds.toString());
		}
		APINodeList<CustomAudience> adSets = new AdAccount(adsAccountId, context).getCustomAudiences().requestFields(customAudienceFields).execute();
		System.out.println(adSets.toString());
	}
	@Test
	@Ignore
	public void getAdsets() throws APIException {
		APIContext context = new APIContext(accessToken).enableDebug(true);
		List<String> customAudienceFields = new ArrayList<>();
		for (CustomAudience.EnumFields fieldsds: CustomAudience.EnumFields.values()) {
			customAudienceFields.add(fieldsds.toString());
		}
		APINodeList<AdSet> adSets = new AdAccount(adsAccountId, context).getAdSets().requestAllFields().execute();
		System.out.println(adSets.toString());
	}
	@Ignore
	@Test
	public void createAdsets() throws APIException {
		APIContext context = new APIContext(accessToken).enableDebug(true);
		List<String> customAudienceFields = new ArrayList<>();
		for (CustomAudience.EnumFields fieldsds: CustomAudience.EnumFields.values()) {
			customAudienceFields.add(fieldsds.toString());
		}
		Targeting targeting = new Targeting();
		IDName idName = new IDName();
		idName.setFieldId("120330000010103703");
		targeting.setFieldCustomAudiences(Arrays.asList(new IDName[]{idName}));
		AdSet testAccount = new AdAccount(adsAccountId, context).createAdSet()
				.setName("testAccount")
				.setBillingEvent(AdSet.EnumBillingEvent.VALUE_IMPRESSIONS)
				.setLifetimeBudget("1000000")
				.setOptimizationGoal(AdSet.EnumOptimizationGoal.VALUE_POST_ENGAGEMENT)
				.setBidAmount("1500")
				.setTargeting(targeting)
				.setCampaignId("120330000011209503")
				.setEndTime("1511568000")
				.execute();
		System.out.println(testAccount.toString());
	}
	@Test
	@Ignore
	public void createCampaign() throws APIException {
		APIContext context = new APIContext(accessToken).enableDebug(true);
		List<String> customAudienceFields = new ArrayList<>();
		for (CustomAudience.EnumFields fieldsds: CustomAudience.EnumFields.values()) {
			customAudienceFields.add(fieldsds.toString());
		}
		Targeting targeting = new Targeting();
		IDName idName = new IDName();
		idName.setFieldId("120330000010103703");
		targeting.setFieldCustomAudiences(Arrays.asList(new IDName[]{idName}));
		Campaign testAccount = new AdAccount(adsAccountId, context)
				.createCampaign()
				.setName("testName")
				.setObjective(Campaign.EnumObjective.VALUE_LINK_CLICKS)
				.execute();
		System.out.println(testAccount.toString());
	}
	@Test
	@Ignore
	public  void fetchFbAdsInsights() throws APIException {
		APIContext context = new APIContext(accessToken).enableDebug(true);
		FBFilter fbFilter = new FBFilter();
		fbFilter.addFilter("adset.id", FBFilter.Operator.EQUAL, adaset);
		APINodeList<AdsInsights> adsInsightsList = new AdAccount(adsAccountId, context).
				getInsights()
				.setLevel(AdsInsights.EnumLevel.VALUE_ADSET).setFiltering(fbFilter.toString()).setParam("date_preset", "lifetime").execute();

	}

}