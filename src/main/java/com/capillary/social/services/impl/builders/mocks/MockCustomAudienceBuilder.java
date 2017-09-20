package com.capillary.social.services.impl.builders.mocks;

import com.capillary.social.UserDetails;
import com.capillary.social.services.api.builders.CustomAudienceListBuider;
import com.facebook.ads.sdk.APIException;

import java.util.List;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 19/9/17
 */
public class MockCustomAudienceBuilder implements CustomAudienceListBuider{

	@Override
	public String build(List<UserDetails> userDetailsList, String listName, String listDescription, long orgId) throws APIException {
		return "success";
	}
}