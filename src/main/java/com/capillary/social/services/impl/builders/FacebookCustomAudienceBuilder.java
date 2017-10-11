package com.capillary.social.services.impl.builders;

import com.capillary.social.CustomAudienceListDetails;
import com.capillary.social.UserDetails;
import com.capillary.social.commons.dao.api.ConfigKeyValuesDao;
import com.capillary.social.commons.dao.api.SocialAudienceListDao;
import com.capillary.social.commons.model.SocialAudienceList;
import com.capillary.social.handler.ApplicationContextAwareHandler;
import com.capillary.social.library.api.OrgConfigurations;
import com.capillary.social.model.FBAudienceList;
import com.capillary.social.model.FacebookAdsConfigrations;
import com.capillary.social.services.api.builders.CustomAudienceListBuider;
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
public class FacebookCustomAudienceBuilder implements CustomAudienceListBuider {
	private static Logger logger = LoggerFactory.getLogger(FacebookCustomAudienceBuilder.class);

	@Override
	public String build(List<UserDetails> userDetailsList, String listName, String listDescription,String recipientListId, long orgId) throws APIException {
		logger.info("received calls to build custom audience list");
		SocialAudienceList socialAudienceList = getSocialAudienceList(recipientListId);
		FacebookAdsConfigrations facebookAdsConfigrations = OrgConfigurations.getFacebookConfigrations(orgId);
		Guard.notNullOrEmpty(facebookAdsConfigrations.getAccessToken(), "facebook access token");
		Guard.notNullOrEmpty(facebookAdsConfigrations.getAdsAccountId(), "facebook ads account id");
		APIContext context = new APIContext(facebookAdsConfigrations.getAccessToken()).enableDebug(true);
		CustomAudience customAudience;
		if(socialAudienceList!=null){
			logger.info("facebook list has been created already adding customers to existing list");
			customAudience = new CustomAudience(Long.parseLong(socialAudienceList.getRemoteListId()),context).fetch();
		}
		else {
			logger.info("trying to create empty custom audience list with name :{} and description \"{}\"", listName, listDescription);
			customAudience=new AdAccount(facebookAdsConfigrations.getAdsAccountId(), context).createCustomAudience()
					.setName(listName)
					.setSubtype(CustomAudience.EnumSubtype.VALUE_CUSTOM)
					.setDescription(listDescription)
					.requestAllFields()
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
		updateCustomAudienceList(customAudience,recipientListId,orgId);
		return customAudience.getId();
	}

	private SocialAudienceList getSocialAudienceList(String recipientListId){
		logger.info("getting the social audience list with recipient list id {}",recipientListId);
		ApplicationContext applicationContext = ApplicationContextAwareHandler.getApplicationContext();
		SocialAudienceListDao socialAudienceListDao = (SocialAudienceListDao) applicationContext.getBean("socialAudienceListDaoImpl");
		SocialAudienceList socialAudienceList = socialAudienceListDao.findByRecepientListId(recipientListId);
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

	private boolean updateCustomAudienceList(CustomAudience customAudience,String recipentListID,long orgId){
		logger.info("updating customAudiencelist with id {}",customAudience.getId());
		SocialAudienceList socialAudienceList =convertToSocialAudienceList(customAudience,recipentListID,orgId);
		ApplicationContext applicationContext = ApplicationContextAwareHandler.getApplicationContext();
		SocialAudienceListDao socialAudienceListDao = (SocialAudienceListDao) applicationContext.getBean("socialAudienceListDaoImpl");
		logger.info("updated custom audience list");
		return socialAudienceListDao.update(socialAudienceList);
	}

	private SocialAudienceList convertToSocialAudienceList(CustomAudience customAudience,String recipentListID, long orgId){
		SocialAudienceList socialAudienceList = new SocialAudienceList();
		socialAudienceList.setCampaignReceipientListId(recipentListID);
		socialAudienceList.setRemoteListId(customAudience.getId());
		socialAudienceList.setOrgId((int)orgId);
		socialAudienceList.setType(SocialAudienceList.Type.FACEBOOK);
		socialAudienceList.setAccuntId(customAudience.getFieldAccountId());
		socialAudienceList.setName(customAudience.getFieldName());
		socialAudienceList.setDescription(customAudience.getFieldDescription());
		socialAudienceList.setApproximateCount(customAudience.getFieldApproximateCount());
		socialAudienceList.setRemoteUpdatedOn(new Date(customAudience.getFieldTimeUpdated()*1000));
		socialAudienceList.setCreatedOn(new Date(customAudience.getFieldTimeCreated()*100));
		socialAudienceList.setCachedOn(new Date());
		return  socialAudienceList;
	}
}
