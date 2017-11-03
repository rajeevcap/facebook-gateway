package com.capillary.social.services.impl.builders.mocks;

import com.capillary.social.CustomAudienceList;
import com.capillary.social.commons.model.SocialAudienceList;
import com.capillary.social.services.api.builders.CustomAudienceReportsBuilder;

import java.util.List;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 20/9/17
 */
public class MockCustomAudienceReportsBuilder implements CustomAudienceReportsBuilder{
	@Override
	public List<CustomAudienceList> buildAll(long orgId, boolean fetch) {
		return null;
	}

	@Override
	public CustomAudienceList build(long orgId, String listId, boolean fetch) {
		return null;
	}

	@Override
	public boolean save(List<SocialAudienceList> socialAudienceLists) {
		return false;
	}
}
