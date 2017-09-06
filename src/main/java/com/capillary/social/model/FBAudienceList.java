package com.capillary.social.model;

import com.capillary.social.UserDetails;
import com.capillary.social.utils.SHA256Encoder;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 6/9/17
 */
public class FBAudienceList extends LinkedList<UserDetails> {
	private static final ArrayList<String> schema = new ArrayList<>();
	public FBAudienceList(){
		schema.add("EMAIL");
		schema.add("PHONE");
	}

	@Override
	public String toString(){
		HashMap<String,Object> payLoad = new HashMap<>();
		LinkedList<List<String>> data = new LinkedList<>();
		StringBuilder sb = new StringBuilder();
		this.forEach((userDetails -> {
			String emailHash = SHA256Encoder.getInstance().encode(userDetails.email);
			String mobileHash  = SHA256Encoder.getInstance().encode(userDetails.mobile);
			data.add(Arrays.asList(new String[]{emailHash,mobileHash}));
		}));
		payLoad.put("schema",schema);
		payLoad.put("data",data);
		return new Gson().toJson(payLoad);
	}
}
