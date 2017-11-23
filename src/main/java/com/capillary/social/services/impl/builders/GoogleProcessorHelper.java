package com.capillary.social.services.impl.builders;


import com.capillary.social.*;
import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroup;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroupServiceInterface;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroupStatus;
import com.google.api.ads.adwords.axis.v201710.rm.AdwordsUserListServiceInterface;
import com.google.api.ads.adwords.axis.v201710.rm.Member;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.utils.v201710.ReportDownloaderInterface;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;
import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.capillary.social.services.impl.builders.GoogleProcessorHelper.GoogleAPIKeys.*;
import static com.google.api.ads.adwords.axis.v201710.cm.AdGroupStatus.ENABLED;
import static com.google.api.ads.adwords.axis.v201710.cm.AdGroupStatus.PAUSED;

/**
 * Created by rajeev on 19/11/17.
 * contains specific keys required for authentication with google
 * gives back properties configuration
 */
class GoogleProcessorHelper extends SocialProcessorHelper {

    private long orgId;

    AdWordsSession session;

    AdwordsUserListServiceInterface userListService;
    AdGroupServiceInterface adGroupService;
    ReportDownloaderInterface reportDownloader;

    public enum GoogleAPIKeys {

        GOOGLE_ADS_CLIENT_ID("api.adwords.clientId"),
        GOOGLE_ADS_CLIENT_SECRET("api.adwords.clientSecret"),
        GOOGLE_ADS_REFRESH_TOKEN("api.adwords.refreshToken"),
        GOOGLE_ADS_DEVELOPER_TOKEN("api.adwords.developerToken"),
        GOOGLE_ADS_USER_AGENT("api.adwords.userAgent"),
        GOOGLE_ADS_CLIENT_CUSTOMER_ID("api.adwords.clientCustomerId");

        String key;

        GoogleAPIKeys(String key) {
            this.key = key;
        }

    }

    private static final String[] API_ADWORDS_KEYSET = new String[]{GOOGLE_ADS_CLIENT_ID.name(), GOOGLE_ADS_CLIENT_SECRET.name(), GOOGLE_ADS_REFRESH_TOKEN.name(), GOOGLE_ADS_DEVELOPER_TOKEN.name(), GOOGLE_ADS_CLIENT_CUSTOMER_ID.name(), GOOGLE_ADS_USER_AGENT.name()};

    GoogleProcessorHelper(long orgId) throws OAuthException, ConfigurationException, ValidationException {
        super(orgId, API_ADWORDS_KEYSET);
        this.orgId = orgId;
    }

    void authenticate() throws ValidationException, OAuthException, ConfigurationException {
        PropertiesConfiguration config = getPropertiesConfiguration();
        Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(OfflineCredentials.Api.ADWORDS).from(config).build().generateCredential();
        session = new AdWordsSession.Builder().from(config).withOAuth2Credential(oAuth2Credential).build();
    }

    private PropertiesConfiguration getPropertiesConfiguration() throws ConfigurationException {
        PropertiesConfiguration config = new PropertiesConfiguration();
        for(String adwordsKey : API_ADWORDS_KEYSET) {
            if(!keyValueMap.containsKey(adwordsKey)) {
                throw new ConfigurationException("key " + adwordsKey + " not found for org " + orgId);
            }
            config.setProperty(GoogleAPIKeys.valueOf(adwordsKey).key, keyValueMap.get(adwordsKey));
        }
        return config;
    }

    void generateServices() {
        AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();
        userListService = adWordsServices.get(session, AdwordsUserListServiceInterface.class);
        adGroupService = adWordsServices.get(session, AdGroupServiceInterface.class);
        reportDownloader = adWordsServices.getUtility(session, ReportDownloaderInterface.class);
    }

    static Member[] getMemberList(List<UserDetails> userDetails) throws UnsupportedEncodingException {
        List<Member> members = new ArrayList<>();
        for(UserDetails userDetail : userDetails) {
            Member member = new Member();
            String email = userDetail.getEmail();
            if(!Strings.isNullOrEmpty(email)) {
                String normalizedEmail = toNormalizedString(userDetail.getEmail());
                member.setHashedEmail(toSHA256String(normalizedEmail));
            }
            String mobile = userDetail.getMobile();
            if(!Strings.isNullOrEmpty(mobile)) {
                String normalizedMobile = toNormalizedString(userDetail.getMobile());
                member.setHashedPhoneNumber(toSHA256String(normalizedMobile));
            }
            members.add(member);
        }
        return members.toArray(new Member[members.size()]);
    }

    List<SocialAdSet> toSocialAdSet(AdGroup[] adGroups) {
        List<SocialAdSet> socialAdSets = new ArrayList<>();
        for(AdGroup adGroup : adGroups) {
            socialAdSets.add(toSocialAdSet(adGroup));
        }
        return socialAdSets;
    }

    private SocialAdSet toSocialAdSet(AdGroup adGroup) {
        SocialAdSet socialAdSet = new SocialAdSet();
        socialAdSet.id = adGroup.getId().toString();
        socialAdSet.name = adGroup.getName();
        socialAdSet.campaignId = adGroup.getCampaignId().toString();
        socialAdSet.status = toSocialAdSetStatus(adGroup.getStatus());
        return socialAdSet;
    }

    private AdSetStatus toSocialAdSetStatus(AdGroupStatus status) {
        if(status == ENABLED) {
            return AdSetStatus.ACTIVE;
        } else if (status == PAUSED) {
            return AdSetStatus.PAUSED;
        } else {
            return AdSetStatus.DELETED;
        }
    }

    static String getFieldsForGoogle(String jsonFields) {
        JsonParser parser = new JsonParser();
        JsonObject returnObject = new JsonObject();
        JsonObject json = parser.parse(jsonFields).getAsJsonObject();
        json = json.get("report").getAsJsonObject();
        returnObject.addProperty("adset_name", json.get("report-name").getAsJsonObject().get("name").getAsString());
        json = json.get("table").getAsJsonObject();
        json = json.get("row").getAsJsonObject();
        returnObject.addProperty("reach", json.get("impressions").getAsString());
        returnObject.addProperty("spend", json.get("cost").getAsString());
        returnObject.addProperty("clicks", json.get("clicks").getAsString());
        returnObject.addProperty("impressions", json.get("impressions").getAsString());
        returnObject.addProperty("account_id", json.get("customerID").getAsString());
        returnObject.addProperty("account_name", json.get("clientName").getAsString());
        return returnObject.toString();
    }

}
