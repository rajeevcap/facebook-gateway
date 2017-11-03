package com.capillary.social.services.api.builders;

import com.capillary.social.SocialAdSet;
import com.facebook.ads.sdk.AdSet;

import java.util.List;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 18/10/17
 */
public interface AdsetsReportsBuilder {
	List<SocialAdSet> buildAll(long orgId);
	SocialAdSet build(long orgId,String listId);
}
