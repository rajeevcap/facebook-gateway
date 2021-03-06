package com.capillary.social.services.impl.builders;

import com.capillary.social.AdInsight;
import com.capillary.social.SocialChannel;
import com.capillary.social.commons.dao.api.*;
import com.capillary.social.commons.model.CommunicationDetails;
import com.capillary.social.commons.model.SocialAudienceList;
import com.capillary.social.handler.ApplicationContextAwareHandler;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.capillary.social.services.impl.builders.GoogleProcessorHelper.getFieldsForGoogle;

/**
 * Created by rajeev on 13/11/17.
 * loads all config keys for the social channel with given org id
 * generated all required beans
 * contains static helper methods
 */
public class SocialProcessorHelper {

    private static Logger logger = LoggerFactory.getLogger(SocialProcessorHelper.class);

    protected long orgId;
    Map<String, String> keyValueMap;
    private static final MessageDigest digest = getSHA256MessageDigest();

    static SocialAudienceListDao socialAudienceListDao;
    static FacebookAdsetInsightsDao facebookAdsetInsightsDao;
    static ConfigKeyValuesDao configKeyValuesDao;
    static MessageAdsetMappingDao messageAdsetMappingDao;
    static CommunicationDetailsDao communicationDetailsDao;

    static {
        socialAudienceListDao = (SocialAudienceListDao) getBean("socialAudienceListDaoImpl");
        facebookAdsetInsightsDao = (FacebookAdsetInsightsDao) getBean("facebookAdsetInsightsDaoImpl");
        configKeyValuesDao = (ConfigKeyValuesDao) getBean("configKeyValuesDaoImpl");
        messageAdsetMappingDao = (MessageAdsetMappingDao) getBean("messageAdsetMappingDaoImpl");
        communicationDetailsDao = (CommunicationDetailsDao) getBean("communicationDetailsDaoImpl");
    }

    SocialProcessorHelper(long orgId, String... keys) {
        this.orgId = orgId;
        loadKeys(keys);
    }


    private void loadKeys(String ...keys) {
        keyValueMap = configKeyValuesDao.findAllKeyValueMapForOrg(orgId, keys);
        logger.info("key values loaded for org {} : {}", orgId, keyValueMap);
    }

    private static Object getBean(String beanName) {
        ApplicationContext applicationContext = ApplicationContextAwareHandler.getApplicationContext();
        return applicationContext.getBean(beanName);
    }

    private static MessageDigest getSHA256MessageDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Missing SHA-256 algorithm implementation.", e);
        }
    }

    static String toSHA256String(String value) throws UnsupportedEncodingException {
        byte[] hash = digest.digest(value.getBytes("UTF-8"));
        StringBuilder result = new StringBuilder();
        for (byte b : hash) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    static String toNormalizedString(String value) {
        return value.trim().toLowerCase();
    }

    static String xmlToJsonParser(String xml) {
        JSONObject xmlJsonObject = XML.toJSONObject(xml);
        return xmlJsonObject.toString(3);
    }

    static Map<String, SocialAudienceList> getRemoteLocalListMap(List<SocialAudienceList> audienceLists) {
        Map<String, SocialAudienceList> remoteLocalListMap = new HashMap<>();
        for(SocialAudienceList audienceList : audienceLists) {
            remoteLocalListMap.put(audienceList.getRemoteListId(), audienceList);
        }
        return remoteLocalListMap;
    }

    AdInsight convertToThriftObject(com.capillary.social.commons.model.AdsInsights dbObject) {
        AdInsight adInsight = new AdInsight();
        adInsight.setOrgId(dbObject.getOrgId());
        adInsight.setSocialChannel(SocialChannel.valueOf(dbObject.getType().name().toLowerCase()));
        adInsight.setAdsetId(dbObject.getAdsetId());
        adInsight.setInsights(getFieldsForGoogle(dbObject.getInsights()));
        adInsight.setCachedon(dbObject.getCachedOn().getTime());
        return adInsight;
    }

}
