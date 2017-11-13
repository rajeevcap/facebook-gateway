package com.capillary.social.services.impl.factories;

import com.capillary.social.SocialChannel;
import com.capillary.social.services.api.builders.CustomAudienceListBuider;
import com.capillary.social.services.api.factories.BuilderFactory;
import com.capillary.social.services.impl.builders.FacebookCustomAudienceBuilder;
import com.capillary.social.services.impl.builders.GoogleListBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 5/9/17
 */
@Component
public class CustomAudienceListBuilderFactory extends BuilderFactory<CustomAudienceListBuider> {
	@Override
	protected Map<SocialChannel,CustomAudienceListBuider> buildersList() {
		Map<SocialChannel,CustomAudienceListBuider> buiderMap = new HashMap<>();
		buiderMap.put(SocialChannel.facebook, new FacebookCustomAudienceBuilder());
//		buiderMap.put(SocialChannel.google, new GoogleListBuilder());
		return buiderMap;
	}
}
