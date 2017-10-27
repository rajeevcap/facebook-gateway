package com.capillary.social.services.impl.factories;

import com.capillary.social.SocialChannel;
import com.capillary.social.services.api.builders.AdsetInsightsReportBuilder;
import com.capillary.social.services.api.factories.BuilderFactory;
import com.capillary.social.services.impl.builders.FacebookAdsetInsightsReportBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 26/10/17
 */
@Component
public class AdsetInsightsReportBuilderFactory extends BuilderFactory<AdsetInsightsReportBuilder> {
	@Override
	protected Map<SocialChannel, AdsetInsightsReportBuilder> buildersList() {
		Map<SocialChannel,AdsetInsightsReportBuilder> reportBuilderMap = new HashMap<>();
		reportBuilderMap.put(SocialChannel.facebook,new FacebookAdsetInsightsReportBuilder());
		return reportBuilderMap;
	}
}
