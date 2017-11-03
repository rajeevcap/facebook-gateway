package com.capillary.social.library.api;
import com.capillary.social.commons.dao.api.ConfigKeyValuesDao;
import com.capillary.social.handler.ApplicationContextAwareHandler;
import com.capillary.social.model.FacebookAdsConfigurations;
import org.springframework.context.ApplicationContext;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 8/9/17
 */
public class OrgConfigurations {
	public static FacebookAdsConfigurations getFacebookConfigrations(long orgId){
		FacebookAdsConfigurations facebookAdsConfigurations = new FacebookAdsConfigurations(orgId);
		ApplicationContext applicationContext = ApplicationContextAwareHandler.getApplicationContext();
		ConfigKeyValuesDao configKeyValueDao = (ConfigKeyValuesDao) applicationContext.getBean("configKeyValuesDaoImpl");
		String fbAdsAccountId = configKeyValueDao.findValueByName(OrgConfigrationKeys.FB_ADS_ACCOUNT_ID.name(),orgId);
		String fbAccessToken = configKeyValueDao.findValueByName(OrgConfigrationKeys.FB_ADS_ACCESS_TOKEN.name(),orgId);
		facebookAdsConfigurations.setAccessToken(fbAccessToken);
		facebookAdsConfigurations.setAdsAccountId(fbAdsAccountId);
		return facebookAdsConfigurations;
	}
}
