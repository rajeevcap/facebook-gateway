package com.capillary.social.external.impl;

import com.capillary.social.FacebookException;
import com.capillary.social.SocialChannel;
import com.capillary.social.UserDetails;
import org.apache.thrift.TException;
import org.junit.Before;
import org.junit.Test;

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
	FacebookServiceListener facebookServiceListener;
	@Before
	public void setUp(){
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
		facebookServiceListener.createCustomList(userDetailsList, SocialChannel.facebook,"testList","testDescription",0,"unittest","thread1");
	}

	@Test
	public void removeCustomAudienceListTest() throws TException, FacebookException {
		List<UserDetails> userDetailsList = new ArrayList<>();
		UserDetails userDetail1 = new UserDetails();
		userDetail1.setEmail("user1@example.com");
		userDetail1.setMobile("1234567890");
		userDetailsList.add(userDetail1);
		facebookServiceListener.removeFromCustomList(userDetailsList, SocialChannel.facebook,"120330000010103503",0,"118772362192973","thread1");
	}


}