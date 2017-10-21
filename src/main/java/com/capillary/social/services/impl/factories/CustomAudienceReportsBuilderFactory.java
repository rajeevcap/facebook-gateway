package com.capillary.social.services.impl.factories;

import com.capillary.social.SocialChannel;
import com.capillary.social.services.api.builders.CustomAudienceReportsBuilder;
import com.capillary.social.services.api.factories.BuilderFactory;
import com.capillary.social.services.impl.builders.FacebookCustomAudienceReportsBuilder;
import com.capillary.social.services.impl.builders.mocks.MockCustomAudienceReportsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 20/9/17
 */
@Component
public class CustomAudienceReportsBuilderFactory extends BuilderFactory<CustomAudienceReportsBuilder> {
	@Override
	protected Map<SocialChannel, CustomAudienceReportsBuilder> buildersList() {
		Map<SocialChannel,CustomAudienceReportsBuilder> builderMap = new HashMap<>();
		builderMap.put(SocialChannel.facebook, new FacebookCustomAudienceReportsBuilder());
		return builderMap;
	}
}
