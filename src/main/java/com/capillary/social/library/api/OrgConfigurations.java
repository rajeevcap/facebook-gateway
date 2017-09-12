package com.capillary.social.library.api;
import com.capillary.social.commons.dao.api.ConfigKeyValuesDao;
import com.capillary.social.commons.model.ConfigKeyValues;
import com.capillary.social.handler.ApplicationContextAwareHandler;
import com.capillary.social.model.FacebookAdsConfigrations;
import org.springframework.context.ApplicationContext;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 8/9/17
 */
public class OrgConfigurations {
	public static FacebookAdsConfigrations getFacebookConfigrations(long orgId){
		FacebookAdsConfigrations facebookAdsConfigrations = new FacebookAdsConfigrations(orgId);
		ApplicationContext applicationContext = ApplicationContextAwareHandler.getApplicationContext();
		ConfigKeyValuesDao configKeyValueDao = (ConfigKeyValuesDao) applicationContext.getBean("configKeyValuesDaoImpl");
		String fbAdsAccountId = configKeyValueDao.findValueByName(OrgConfigrationKeys.FB_ADS_ACCOUNT_ID.name(),orgId);
		String fbAccessToken = configKeyValueDao.findValueByName(OrgConfigrationKeys.FB_ADS_ACCESS_TOKEN.name(),orgId);
		facebookAdsConfigrations.setAccessToken(fbAccessToken);
		facebookAdsConfigrations.setAdsAccountId(fbAdsAccountId);
		return facebookAdsConfigrations;
	}
}
