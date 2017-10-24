package com.capillary.social.services.api.builders;

import com.capillary.social.CustomAudienceList;
import com.capillary.social.commons.model.SocialAudienceList;

import java.util.List;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 23/10/17
 */
public interface CustomAudienceListBuilderBase {
	public boolean save(List<SocialAudienceList> socialAudienceLists);
}
