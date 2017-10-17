package com.capillary.social.model;

import com.capillary.social.UserDetails;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 6/9/17
 */
public class FBAudienceListTest {
	FBAudienceList fbAudienceList;
	@Before
	public void setUp(){
		fbAudienceList = new FBAudienceList();
	}

	@Test
	public void testEmptyList(){
		String result = fbAudienceList.toString();
		assertEquals("{\"schema\":[\"EMAIL\",\"PHONE\"],\"data\":[]}",result);
	}

	@Test
	public void testNonEmptyList(){
		UserDetails userDetails = new UserDetails();
		userDetails.email="user1@example.com";
		userDetails.mobile = "1234567890";
		fbAudienceList.add(userDetails);
		String result = fbAudienceList.toString();
		assertEquals("{\"schema\":[\"EMAIL\",\"PHONE\"],\"data\":[[\"b36a83701f1c3191e19722d6f90274bc1b5501fe69ebf33313e440fe4b0fe210\",\"c775e7b757ede630cd0aa1113bd102661ab38829ca52a6422ab782862f268646\"]]}",result);
	}

	@Test
	public void testNonEmptyListMultipleItems() {
		UserDetails userDetails1 = new UserDetails();
		userDetails1.email = "user1@example.com";
		userDetails1.mobile = "1234567890";

		UserDetails userDetails2 = new UserDetails();
		userDetails2.email = "user2@example.com";
		userDetails2.mobile = "1234567891";
		fbAudienceList.add(userDetails1);
		fbAudienceList.add(userDetails2);
		String result = fbAudienceList.toString();
		assertEquals("{\"schema\":[\"EMAIL\",\"PHONE\"],\"data\":[[\"b36a83701f1c3191e19722d6f90274bc1b5501fe69ebf33313e440fe4b0fe210\",\"c775e7b757ede630cd0aa1113bd102661ab38829ca52a6422ab782862f268646\"],[\"2b3b2b9ce842ab8b6a6c614cb1f9604bb8a0d502d1af49c526b72b10894e95b5\",\"523aa18ecb892c51fbdbe28c57e10a92419e0a73e8931e578b98baffccf99cdd\"]]}", result);
	}
}