package com.capillary.social.utils;

import com.google.common.collect.Lists;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 5/9/17
 */
public class SHA256EncoderTest {
	private final SHA256Encoder sha256Encoder = SHA256Encoder.getInstance();

	@Test
	public void testSingleString() {
		assertEquals("9b431636bd164765d63c573c346708846af4f68fe3701a77a3bdd7e7e5166254", sha256Encoder.encode("test1@example.com"));
		assertEquals("8cc62c145cd0c6dc444168eaeb1b61b351f9b1809a579cc9b4c9e9d7213a39ee", sha256Encoder.encode("test2@example.com"));
		assertEquals("4eaf70b1f7a797962b9d2a533f122c8039012b31e0a52b34a426729319cb792a", sha256Encoder.encode("test3@example.com"));
		String s = null;
		assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", sha256Encoder.encode(s));
	}

	@Test
	public void testList() {
		List<String> emailList = Arrays.asList(new String[]{"test1@example.com", "test2@example.com", "test3@example.com"});
		List<String> expectedResult = Arrays.asList(new String[]{"9b431636bd164765d63c573c346708846af4f68fe3701a77a3bdd7e7e5166254",
				"8cc62c145cd0c6dc444168eaeb1b61b351f9b1809a579cc9b4c9e9d7213a39ee",
				"4eaf70b1f7a797962b9d2a533f122c8039012b31e0a52b34a426729319cb792a"});
		List<String> actualResult = sha256Encoder.encode(emailList);
		assertArrayEquals(expectedResult.toArray(new String[expectedResult.size()]), actualResult.toArray(new String[actualResult.size()]));
	}

}