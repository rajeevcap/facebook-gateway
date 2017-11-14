package com.capillary.social.services.impl.builders;

import com.capillary.social.SocialAdSet;
import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.utils.v201710.SelectorBuilder;
import com.google.api.ads.adwords.axis.v201710.cm.*;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.selectorfields.v201710.cm.AdGroupField;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by rajeev on 10/11/17.
 */
public class GoogleAdGroupAccessor extends SocialAdBatchAccessor {

    private static final Logger logger = LoggerFactory.getLogger(GoogleAdGroupAccessor.class);

    private AdGroupServiceInterface adGroupService;

    @Override
    protected void generateAuthentication() throws ConfigurationLoadException, ValidationException, OAuthException {
        PropertiesConfiguration config = getPropertiesConfiguration();
        Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(OfflineCredentials.Api.ADWORDS).from(config).build().generateCredential();
        AdWordsSession session = new AdWordsSession.Builder().from(config).withOAuth2Credential(oAuth2Credential).build();
        AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();
        adGroupService = adWordsServices.get(session, AdGroupServiceInterface.class);
    }

    @Override
    protected List<SocialAdSet> getAllAdBatch() throws RemoteException {
        SelectorBuilder selectorBuilder = new SelectorBuilder();
        Selector selector = selectorBuilder.fields(AdGroupField.Id, AdGroupField.Name, AdGroupField.CampaignId, AdGroupField.Status).build();
        AdGroupPage adGroupPage = null;
        try {
            adGroupPage = adGroupService.get(selector);
        } catch (ApiException e) {
            logger.error("exception occured while get ad batch " + e.getFaultString());
            throw e;
        }
        if(adGroupPage.getEntries() != null) {
            for(AdGroup adGroup : adGroupPage.getEntries()) {
                logger.info("ad group with name {} and id {} was found", adGroup.getName(), adGroup.getId());
            }
        }
        return toSocialAdSet(adGroupPage.getEntries());
    }

}
