package com.capillary.social.services.impl.factories;

import com.capillary.social.SocialChannel;
import com.capillary.social.services.api.builders.CustomAudienceReportsBuilder;
import com.capillary.social.services.api.factories.BuilderFactory;
import com.capillary.social.services.impl.builders.FacebookCustomAudienceReportsBuilder;
import com.capillary.social.services.impl.builders.mocks.MockCustomAudienceReportsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 20/9/17
 */
public class CustomAudienceReportsBuilderFactory implements BuilderFactory<CustomAudienceReportsBuilder> {
	private static Logger logger = LoggerFactory.getLogger(CustomAudienceReportsBuilderFactory.class);
	Map<SocialChannel, CustomAudienceReportsBuilder> audienceReportsBuiderMap;
	private static CustomAudienceReportsBuilderFactory instance;
	private static boolean isTestMode;

	private CustomAudienceReportsBuilderFactory() {
		audienceReportsBuiderMap = new HashMap<>();
		//put google, twitter audience builder when it is created
		audienceReportsBuiderMap.put(SocialChannel.facebook, new FacebookCustomAudienceReportsBuilder());
	}

	public static  CustomAudienceReportsBuilderFactory getInstance() {
		if (instance == null) {
			instance = new CustomAudienceReportsBuilderFactory();
		}
		return instance;
	}
	public static  CustomAudienceReportsBuilderFactory getInstance(boolean isTestMode) {
		CustomAudienceReportsBuilderFactory.isTestMode = isTestMode;
		return getInstance();
	}

	@Override
	public CustomAudienceReportsBuilder getBulder(SocialChannel socialChannel) {
		if(isTestMode){
			return new MockCustomAudienceReportsBuilder();
		}
		if (!audienceReportsBuiderMap.containsKey(socialChannel)) {
			logger.error("could not find a custom audience reports builder for class " + socialChannel.name());
			throw new RuntimeException("could not find a custom audience builder for class " + socialChannel.name());
		}
		return audienceReportsBuiderMap.get(socialChannel);
	}
}
