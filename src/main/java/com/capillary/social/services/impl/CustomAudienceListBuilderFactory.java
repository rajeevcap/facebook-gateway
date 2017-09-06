package com.capillary.social.services.impl;

import com.capillary.social.SocialChannel;
import com.capillary.social.services.api.CustomAudienceListBuider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 5/9/17
 */
public class CustomAudienceListBuilderFactory {
	private static Logger logger = LoggerFactory.getLogger(CustomAudienceListBuider.class);
	Map<SocialChannel, CustomAudienceListBuider> audienceListBuiderMap;
	private static CustomAudienceListBuilderFactory instance;

	private CustomAudienceListBuilderFactory() {
		audienceListBuiderMap = new HashMap<>();
		//put google, twitter audience builder when it is created
		audienceListBuiderMap.put(SocialChannel.facebook, new FacebookCustomAudienceBuilder());
	}

	public static  CustomAudienceListBuilderFactory getInstance() {
		if (instance == null) {
			instance = new CustomAudienceListBuilderFactory();
		}
		return instance;
	}

	public CustomAudienceListBuider getBulder(SocialChannel socialChannel) {
		if (!audienceListBuiderMap.containsKey(socialChannel)) {
			logger.error("could not find a custom audience builder for class " + socialChannel.name());
			throw new RuntimeException("could not find a custom audience builder for class " + socialChannel.name());
		}
		return audienceListBuiderMap.get(socialChannel);
	}
}
