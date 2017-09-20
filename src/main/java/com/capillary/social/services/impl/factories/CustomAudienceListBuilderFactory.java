package com.capillary.social.services.impl.factories;

import com.capillary.social.SocialChannel;
import com.capillary.social.services.api.builders.CustomAudienceListBuider;
import com.capillary.social.services.api.factories.BuilderFactory;
import com.capillary.social.services.impl.builders.mocks.MockCustomAudienceBuilder;
import com.capillary.social.services.impl.builders.FacebookCustomAudienceBuilder;
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
public class CustomAudienceListBuilderFactory implements BuilderFactory<CustomAudienceListBuider>{
	private static Logger logger = LoggerFactory.getLogger(CustomAudienceListBuider.class);
	Map<SocialChannel, CustomAudienceListBuider> audienceListBuiderMap;
	private static CustomAudienceListBuilderFactory instance;
	private static boolean isTestMode;

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
	public static  CustomAudienceListBuilderFactory getInstance(boolean isTestMode) {
		CustomAudienceListBuilderFactory.isTestMode = isTestMode;
		return getInstance();
	}

	@Override
	public CustomAudienceListBuider getBulder(SocialChannel socialChannel) {
		if(isTestMode){
			return new MockCustomAudienceBuilder();
		}
		if (!audienceListBuiderMap.containsKey(socialChannel)) {
			logger.error("could not find a custom audience builder for class " + socialChannel.name());
			throw new RuntimeException("could not find a custom audience builder for class " + socialChannel.name());
		}
		return audienceListBuiderMap.get(socialChannel);
	}
}
