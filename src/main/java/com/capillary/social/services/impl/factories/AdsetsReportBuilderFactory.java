package com.capillary.social.services.impl.factories;

import com.capillary.social.SocialChannel;
import com.capillary.social.services.api.builders.AdsetsReportsBuilder;
import com.capillary.social.services.api.factories.BuilderFactory;
import com.capillary.social.services.impl.builders.FacebookAdsetsReportBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 18/10/17
 */
@Component
public class AdsetsReportBuilderFactory extends BuilderFactory<AdsetsReportsBuilder>{
	public AdsetsReportBuilderFactory(){

	}
	@Override
	protected Map<SocialChannel, AdsetsReportsBuilder> buildersList() {
		Map<SocialChannel,AdsetsReportsBuilder> builderMap = new HashMap<>();
		builderMap.put(SocialChannel.facebook, new FacebookAdsetsReportBuilder());
		return builderMap;
	}
}
