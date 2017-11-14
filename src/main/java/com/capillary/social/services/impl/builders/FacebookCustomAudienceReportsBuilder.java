package com.capillary.social.services.impl.builders;
import com.capillary.social.CustomAudienceList;
import com.capillary.social.commons.dao.api.SocialAudienceListDao;
import com.capillary.social.commons.model.SocialAudienceList;
import com.capillary.social.handler.ApplicationContextAwareHandler;
import com.capillary.social.library.api.OrgConfigurations;
import com.capillary.social.model.FacebookAdsConfigurations;
import com.capillary.social.services.api.builders.CustomAudienceReportsBuilder;
import com.capillary.social.utils.Guard;
import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.APINodeList;
import com.facebook.ads.sdk.AdAccount;
import com.facebook.ads.sdk.CustomAudience;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 20/9/17
 */
public class FacebookCustomAudienceReportsBuilder extends FacebookCustomAudienceBuilderBase implements CustomAudienceReportsBuilder {
	private static Logger logger = LoggerFactory.getLogger(FacebookCustomAudienceReportsBuilder.class);

	@Override
	public List<CustomAudienceList> buildAll(long orgId,boolean fetch) {
		logger.info("received call for building custom all audience report for org {}", orgId);
		FacebookAdsConfigurations facebookAdsConfigurations = OrgConfigurations.getFacebookConfigrations(orgId);
		Guard.notNullOrEmpty(facebookAdsConfigurations.getAdsAccountId(), "facebook ads account id");
		ApplicationContext applicationContext = ApplicationContextAwareHandler.getApplicationContext();
		SocialAudienceListDao socialAudienceListDao = (SocialAudienceListDao) applicationContext.getBean("socialAudienceListDaoImpl");
		Guard.notNull(socialAudienceListDao,"socialAudienceListDao");
		List<CustomAudienceList> customAudienceLists = new ArrayList<>();
		List<SocialAudienceList> socialAudienceLists;
		logger.info("fetching from local cache for account id {}", facebookAdsConfigurations.getAdsAccountId());
		socialAudienceLists= socialAudienceListDao.findByAccountIdOrgId(facebookAdsConfigurations.getAdsAccountId(),orgId,"FACEBOOK");
		logger.info("found {} social audience lists");
		if(!socialAudienceLists.isEmpty() && fetch) {
			Map<String,String> remoteLocalMap = getRemoteLocalListMap(socialAudienceLists);
			Guard.notNullOrEmpty(facebookAdsConfigurations.getAccessToken(), "facebook access token");
			APIContext context = new APIContext(facebookAdsConfigurations.getAccessToken()).enableDebug(true);
			logger.info("trying to fetch all the custom audience list for  \"{}\"", orgId);
			APINodeList<CustomAudience> customAudiences;
			try {
				customAudiences = new AdAccount(facebookAdsConfigurations.getAdsAccountId(), context)
						.getCustomAudiences()
						.requestFields(customAudienceListFields)
						.execute();
			} catch (APIException e) {
				logger.error("error while fetching custom audience list from facebook", e);
				throw new RuntimeException(e);
			}
			if (customAudiences.isEmpty()) {
				logger.info("facebook returned empty list");
			} else {
				socialAudienceLists= new ArrayList<>();
				for (CustomAudience customAudience : customAudiences) {
					if(!remoteLocalMap.containsKey(customAudience.getId())){
						logger.info("ignoring unknown list {}",customAudience.getId());
						continue;
					}
					socialAudienceLists.add(convertToSocialAudienceList(customAudience,remoteLocalMap.get(customAudience.getId()),orgId));
				}
				logger.info("saving social audience list to local db");
				socialAudienceListDao.updateBatch(socialAudienceLists);
				logger.info("saved social audience lists");
			}
		}
		for (SocialAudienceList socialAudienceList:socialAudienceLists ) {
			customAudienceLists.add(convertToThriftObject(socialAudienceList));
		}
		return customAudienceLists;
	}

	@Override
	public CustomAudienceList build(long orgId, String listId,boolean fetch) {
		logger.info("build called with arguments orgId:{},listId{}:fetch{}",new Object[]{orgId,listId,fetch});
		List<CustomAudienceList> customAudiences = this.buildAll(orgId,fetch);
		for (CustomAudienceList customAudienceList:customAudiences) {
			if(customAudienceList.getRemoteListId().equals(listId)){
				logger.info("found custom audience with id {}",listId);
				return customAudienceList;
			}
		}
		logger.warn("could not find the list with id {}",listId);
		return null;
	}
}
