package com.capillary.social.services.impl.builders;

import com.capillary.social.UserDetails;
import com.capillary.social.commons.dao.api.SocialAudienceListDao;
import com.capillary.social.commons.model.SocialAudienceList;
import com.capillary.social.handler.ApplicationContextAwareHandler;
import com.capillary.social.library.api.OrgConfigurations;
import com.capillary.social.model.FBAudienceList;
import com.capillary.social.model.FacebookAdsConfigurations;
import com.capillary.social.services.api.builders.CustomAudienceListBuider;
import com.capillary.social.systems.config.LockHolder;
import com.capillary.social.utils.Guard;
import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.AdAccount;
import com.facebook.ads.sdk.CustomAudience;
import com.facebook.ads.sdk.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.List;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 4/9/17
 */
public class FacebookCustomAudienceBuilder extends FacebookCustomAudienceBuilderBase implements CustomAudienceListBuider {
	private static Logger logger = LoggerFactory.getLogger(FacebookCustomAudienceBuilder.class);

	@Override
	public String build(List<UserDetails> userDetailsList, String listName, String listDescription,String recipientListId, long orgId) throws APIException {
		logger.info("received calls to build custom audience list");
		FacebookAdsConfigurations facebookAdsConfigurations = OrgConfigurations.getFacebookConfigrations(orgId);
		Guard.notNullOrEmpty(facebookAdsConfigurations.getAccessToken(), "facebook access token");
		Guard.notNullOrEmpty(facebookAdsConfigurations.getAdsAccountId(), "facebook ads account id");
		APIContext context = new APIContext(facebookAdsConfigurations.getAccessToken()).enableDebug(true);
		SocialAudienceList socialAudienceList = getSocialAudienceList(recipientListId, facebookAdsConfigurations.getAdsAccountId());
		CustomAudience customAudience;
		if(socialAudienceList!=null){
			String lockKey = facebookAdsConfigurations.getAdsAccountId()+"_"+recipientListId;
			LockHolder.lock(lockKey);
			try {
				logger.info("facebook list has been created already adding customers to existing list");
				customAudience = new CustomAudience(Long.parseLong(socialAudienceList.getRemoteListId()), context).fetch();
			}
			finally {
				LockHolder.release(lockKey);
			}
		}
		else {
			logger.info("trying to create empty custom audience list with name :{} and description \"{}\"", listName, listDescription);

			customAudience=new AdAccount(facebookAdsConfigurations.getAdsAccountId(), context).createCustomAudience()
					.setName(listName)
					.setSubtype(CustomAudience.EnumSubtype.VALUE_CUSTOM)
					.setDescription(listDescription)
					.requestFields(customAudienceListFields)
					.execute().fetch();
			logger.info("created empty custom audience list with id \"{}\"", customAudience.getId());
			boolean saved = saveCustomAudienceList(customAudience,recipientListId,orgId);
			Guard.notFalse(saved," saving status");
		}
		FBAudienceList fbAudienceList = new FBAudienceList();
		fbAudienceList.addAll(userDetailsList);
		logger.info("trying to add {} users to the list", userDetailsList.size());
		logger.info("facebook userdetails before sending"+fbAudienceList.toString());
		User users = customAudience.createUser().setPayload(fbAudienceList.toString()).execute();
		logger.debug("response from facebook api :{}", users.getRawResponseAsJsonObject());
		customAudience.fetch();
		saveCustomAudienceList(customAudience,recipientListId,orgId);
		return customAudience.getId();
	}

	private SocialAudienceList getSocialAudienceList(String recipientListId,String adsAccountId){
		logger.info("getting the social audience list with recipient list id {}",recipientListId);
		ApplicationContext applicationContext = ApplicationContextAwareHandler.getApplicationContext();
		SocialAudienceListDao socialAudienceListDao = (SocialAudienceListDao) applicationContext.getBean("socialAudienceListDaoImpl");
		SocialAudienceList socialAudienceList = socialAudienceListDao.findByRecepientListId(recipientListId,adsAccountId);
		if(socialAudienceList==null){
			logger.info("could not find a social audience list with recipient id {}", recipientListId);
		}
		else {
			logger.info("found social audienceList with remote id {} ",socialAudienceList.getRemoteListId());
		}
		return socialAudienceList;
	}
	private boolean saveCustomAudienceList(CustomAudience customAudience,String recipentListID,long orgId){
		logger.info("saving customAudiencelist with id {}",customAudience.getId());
		SocialAudienceList socialAudienceList =convertToSocialAudienceList(customAudience,recipentListID,orgId);
		ApplicationContext applicationContext = ApplicationContextAwareHandler.getApplicationContext();
		SocialAudienceListDao socialAudienceListDao = (SocialAudienceListDao) applicationContext.getBean("socialAudienceListDaoImpl");
		logger.info("social customAudienceList");
		return socialAudienceListDao.create(socialAudienceList);
	}

}
