package com.capillary.social.utils;

import com.sun.org.apache.xml.internal.security.algorithms.MessageDigestAlgorithm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 5/9/17
 */
public class SHA256Encoder {
	private static SHA256Encoder instance;
	private static MessageDigest messageDigest;
	private static final String ENCRYPTION_ALGORITHM = "SHA-256";
	private SHA256Encoder(){
		try {
			messageDigest = MessageDigest.getInstance(ENCRYPTION_ALGORITHM);
		}
		catch(NoSuchAlgorithmException e){
			throw new RuntimeException(e);
		}
	}

	public static SHA256Encoder getInstance(){
		if(instance==null){
			instance = new SHA256Encoder();
		}
		return instance;
	}

	public List<String> encode(List<String> strings){
		List<String> outputList = new LinkedList<String>();
		for (String s:strings) {
			outputList.add(encode(s));
		}
		return outputList;
	}

	public String encode(String s){
		if (s == null) {
			s = "";
		}
		byte[] digest = messageDigest.digest(s.getBytes());
		return bytesToHex(digest);
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuffer result = new StringBuffer();
		for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
		return result.toString();
	}
}
