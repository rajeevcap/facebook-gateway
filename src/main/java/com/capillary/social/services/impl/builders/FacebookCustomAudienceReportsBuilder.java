package com.capillary.social.services.impl.builders;

import com.capillary.social.CustomAudienceDeliveryStatus;
import com.capillary.social.CustomAudienceList;
import com.capillary.social.CustomAudienceOperationStatus;
import com.capillary.social.library.api.OrgConfigurations;
import com.capillary.social.model.FacebookAdsConfigrations;
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

import java.util.ArrayList;
import java.util.List;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 20/9/17
 */
public class FacebookCustomAudienceReportsBuilder implements CustomAudienceReportsBuilder {
	private static Logger logger = LoggerFactory.getLogger(FacebookCustomAudienceReportsBuilder.class);
	private static List<String> customAudienceListFields = Arrays.asList(
			new String[]{
					CustomAudience.EnumFields.VALUE_ID.toString(),
					CustomAudience.EnumFields.VALUE_APPROXIMATE_COUNT.toString(),
					CustomAudience.EnumFields.VALUE_DELIVERY_STATUS.toString(),
					CustomAudience.EnumFields.VALUE_DESCRIPTION.toString(),
					CustomAudience.EnumFields.VALUE_NAME.toString(),
					CustomAudience.EnumFields.VALUE_TIME_CONTENT_UPDATED.toString(),
					CustomAudience.EnumFields.VALUE_TIME_CREATED.toString(),
					CustomAudience.EnumFields.VALUE_OPERATION_STATUS.toString()
			});

	@Override
	public List<CustomAudienceList> buildAll(long orgId) {
		logger.info("received call for building custom all audience report for org {}", orgId);
		FacebookAdsConfigrations facebookAdsConfigrations = OrgConfigurations.getFacebookConfigrations(orgId);
		Guard.notNullOrEmpty(facebookAdsConfigrations.getAccessToken(), "facebook access token");
		Guard.notNullOrEmpty(facebookAdsConfigrations.getAdsAccountId(), "facebook ads account id");
		APIContext context = new APIContext(facebookAdsConfigrations.getAccessToken()).enableDebug(true);
		List<CustomAudienceList> customAudienceLists = new ArrayList<>();
		logger.info("trying to fetch all the custom audience list for  \"{}\"", orgId);
		APINodeList<CustomAudience> customAudiences;
		try {
			customAudiences = new AdAccount(facebookAdsConfigrations.getAdsAccountId(), context)
					.getCustomAudiences()
					.requestFields(customAudienceListFields)
					.execute();
		} catch (APIException e) {
			logger.error("error while fetching custom audience list from facebook",e);
			throw new RuntimeException(e);
		}
		if (customAudiences.isEmpty()) {
			logger.info("facebook returned empty list");
		} else {
			for (CustomAudience customAudience : customAudiences) {
				customAudienceLists.add(convertToThriftObject(customAudience));
			}
		}
		return customAudienceLists;
	}

	private static  CustomAudienceList convertToThriftObject(CustomAudience fbObj) {

		CustomAudienceList thriftObj = new CustomAudienceList();
		CustomAudienceDeliveryStatus deliveryStatus = new CustomAudienceDeliveryStatus();
		CustomAudienceOperationStatus operationStatus = new CustomAudienceOperationStatus();

		deliveryStatus.setCode(fbObj.getFieldDeliveryStatus().getFieldCode());
		deliveryStatus.setDescription(fbObj.getFieldDeliveryStatus().getFieldDescription());
		thriftObj.setDeliveryStatus(deliveryStatus);

		operationStatus.setCode(fbObj.getFieldOperationStatus().getFieldCode());
		operationStatus.setDescription(fbObj.getFieldOperationStatus().getFieldDescription());
		thriftObj.setOperationStatus(operationStatus);

		thriftObj.setId(fbObj.getId());
		thriftObj.setApproximateCount(fbObj.getFieldApproximateCount());
		thriftObj.setDescription(fbObj.getFieldDescription());
		thriftObj.setName(fbObj.getFieldName());
		thriftObj.setContentUpdatedTime(fbObj.getFieldTimeContentUpdated());
		thriftObj.setCreatedTime(fbObj.getFieldTimeCreated());
		return thriftObj;
	}

	@Override
	public CustomAudienceList build(long orgId, String listId) {
		return null;
	}
}
