package com.capillary.social.external.impl;

import com.capillary.social.CreateCustomAudienceListResponse;
import com.capillary.social.CustomAudienceListDetails;
import com.capillary.social.FacebookException;
import com.capillary.social.GatewayResponseType;
import com.capillary.social.SocialAccountDetails;
import com.capillary.social.SocialChannel;
import com.capillary.social.UserDetails;
import com.capillary.social.handler.ApplicationContextAwareHandler;
import com.facebook.ads.sdk.CustomAudience;
import org.apache.thrift.TException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 6/9/17
 */
public class FacebookServiceListenerTest {
	@Mock
	ApplicationContext applicationContext;
	FacebookServiceListener facebookServiceListener;

	@Before
	public void setUp(){
		ApplicationContextAwareHandler applicationContextAwareHandler = new ApplicationContextAwareHandler();
		applicationContextAwareHandler.setApplicationContext();
		facebookServiceListener = new FacebookServiceListener();
	}

	@Test
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
		CreateCustomAudienceListResponse response = facebookServiceListener.createCustomList(userDetailsList, customAudienceListDetails, socialAccountDetails, 0, "thread1");
		assertEquals(GatewayResponseType.failed, response.response);
	}


}