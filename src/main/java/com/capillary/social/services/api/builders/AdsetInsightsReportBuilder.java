package com.capillary.social.services.api.builders;

import com.capillary.social.AdInsight;
import com.facebook.ads.sdk.APIException;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 26/10/17
 */
public interface AdsetInsightsReportBuilder {
	public AdInsight build(long orgId, String adsetId, boolean clearCache) throws APIException;
}
