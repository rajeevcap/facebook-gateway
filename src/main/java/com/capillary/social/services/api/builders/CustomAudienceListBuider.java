package com.capillary.social.services.api.builders;

import com.capillary.social.CreateCustomAudienceListResponse;
import com.capillary.social.UserDetails;
import com.facebook.ads.sdk.APIException;

import java.util.List;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 4/9/17
 */
public interface CustomAudienceListBuider {
	public String build(List<UserDetails> userDetailsList, String listName, String listDescription, long orgId) throws APIException;
}
