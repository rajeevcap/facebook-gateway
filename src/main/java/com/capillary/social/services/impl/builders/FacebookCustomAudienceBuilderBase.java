package com.capillary.social.services.impl.builders;
import com.capillary.social.CustomAudienceList;
import com.capillary.social.SocialChannel;
import com.capillary.social.commons.dao.api.SocialAudienceListDao;
import com.capillary.social.commons.model.SocialAudienceList;
import com.capillary.social.handler.ApplicationContextAwareHandler;
import com.capillary.social.services.api.builders.CustomAudienceListBuilderBase;
import com.capillary.social.utils.Guard;
import com.facebook.ads.sdk.CustomAudience;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 23/10/17
 */
public class FacebookCustomAudienceBuilderBase implements CustomAudienceListBuilderBase{
	private static Logger logger = LoggerFactory.getLogger(FacebookCustomAudienceBuilderBase.class);

	protected static List<String> customAudienceListFields = Arrays.asList(
			new String[]{
					CustomAudience.EnumFields.VALUE_ACCOUNT_ID.toString(),
					CustomAudience.EnumFields.VALUE_ID.toString(),
					CustomAudience.EnumFields.VALUE_APPROXIMATE_COUNT.toString(),
					CustomAudience.EnumFields.VALUE_DELIVERY_STATUS.toString(),
					CustomAudience.EnumFields.VALUE_DESCRIPTION.toString(),
					CustomAudience.EnumFields.VALUE_NAME.toString(),
					CustomAudience.EnumFields.VALUE_TIME_CONTENT_UPDATED.toString(),
					CustomAudience.EnumFields.VALUE_TIME_CREATED.toString(),
					CustomAudience.EnumFields.VALUE_OPERATION_STATUS.toString()
			});
	protected SocialAudienceList convertToSocialAudienceList(CustomAudience customAudience, String recipentListID, long orgId) {
		SocialAudienceList socialAudienceList = new SocialAudienceList();
		socialAudienceList.setCampaignReceipientListId(recipentListID);
		socialAudienceList.setRemoteListId(customAudience.getId());
		socialAudienceList.setOrgId((int) orgId);
		socialAudienceList.setType(SocialAudienceList.Type.FACEBOOK);
		socialAudienceList.setAccuntId(customAudience.getFieldAccountId());
		socialAudienceList.setName(customAudience.getFieldName());
		socialAudienceList.setDescription(customAudience.getFieldDescription());
		socialAudienceList.setApproximateCount(customAudience.getFieldApproximateCount());
		socialAudienceList.setRemoteUpdatedOn(customAudience.getFieldTimeContentUpdated() == 0?new Date():new Date(customAudience.getFieldTimeContentUpdated()* 1000));
		socialAudienceList.setCreatedOn(customAudience.getFieldTimeCreated()==0?new Date():new Date(customAudience.getFieldTimeCreated() * 100));
		socialAudienceList.setCachedOn(new Date());
		return socialAudienceList;
	}

	protected static CustomAudienceList convertToThriftObject(SocialAudienceList socialAudienceList) {
		Guard.notNull(socialAudienceList, "social audience list");
		CustomAudienceList thriftObj = new CustomAudienceList();
		thriftObj.setOrgId(socialAudienceList.getOrgId());
		thriftObj.setSocialChannel(SocialChannel.valueOf(socialAudienceList.getType().name().toLowerCase()));
		thriftObj.setAdsAccountId(socialAudienceList.getAccuntId());
		thriftObj.setRecepientlistId(socialAudienceList.getCampaignReceipientListId());
		thriftObj.setRemoteListId(socialAudienceList.getRemoteListId());
		thriftObj.setName(socialAudienceList.getName());
		thriftObj.setDescription(socialAudienceList.getDescription());
		thriftObj.setApproximateCount(socialAudienceList.getApproximateCount());
		thriftObj.setContentUpdatedTime(socialAudienceList.getRemoteUpdatedOn().getTime());
		thriftObj.setCreatedTime(socialAudienceList.getCreatedOn().getTime());
		thriftObj.setCachedOn(socialAudienceList.getCachedOn().getTime());
		return thriftObj;
	}

	protected static Map<String,String> getRemoteLocalListMap(List<SocialAudienceList> socialAudienceLists){
		Guard.notNull(socialAudienceLists,"socialAudienceList");
		Map<String,String> localRemoteListMap = new HashMap<>();
		for (SocialAudienceList socialAudienceList: socialAudienceLists) {
			localRemoteListMap.put(socialAudienceList.getCampaignReceipientListId(),socialAudienceList.getRemoteListId());
		}
		return localRemoteListMap;
	}
	@Override
	public boolean save(List<SocialAudienceList> socialAudienceLists) {
		Guard.notNull(socialAudienceLists,"socialAudienceList");
		logger.info("saving social audience list of size {}",socialAudienceLists.size());
		ApplicationContext applicationContext = ApplicationContextAwareHandler.getApplicationContext();
		SocialAudienceListDao socialAudienceListDao = (SocialAudienceListDao) applicationContext.getBean("socialAudienceListDaoImpl");
		return socialAudienceListDao.updateBatch(socialAudienceLists);
	}
}
