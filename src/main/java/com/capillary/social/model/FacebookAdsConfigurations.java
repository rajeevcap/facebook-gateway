package com.capillary.social.model;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 8/9/17
 */
public class FacebookAdsConfigurations {
	long orgId;
	boolean dataloaded;

	public String getAdsAccountId() {
		return adsAccountId;
	}

	public void setAdsAccountId(String adsAccountId) {
		this.adsAccountId = adsAccountId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	String adsAccountId;
	String accessToken;
	public FacebookAdsConfigurations(long orgId){
		this.orgId = orgId;
	}


}
