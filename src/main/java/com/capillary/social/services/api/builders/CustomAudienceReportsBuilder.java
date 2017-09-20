package com.capillary.social.services.api.builders;

import com.capillary.social.CustomAudienceList;

import java.util.List;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 20/9/17
 */
public interface CustomAudienceReportsBuilder {
	public List<CustomAudienceList> buildAll(long orgId);
	public CustomAudienceList build(long orgId, String listId);
}
