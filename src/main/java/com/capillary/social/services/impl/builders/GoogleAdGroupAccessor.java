package com.capillary.social.services.impl.builders;

import com.capillary.social.SocialAdSet;
import com.google.api.ads.adwords.axis.utils.v201710.SelectorBuilder;
import com.google.api.ads.adwords.axis.v201710.cm.*;
import com.google.api.ads.adwords.lib.selectorfields.v201710.cm.AdGroupField;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by rajeev on 10/11/17.
 */
public class GoogleAdGroupAccessor extends SocialAdBatchAccessor {

    private static final Logger logger = LoggerFactory.getLogger(GoogleAdGroupAccessor.class);

    private GoogleProcessorHelper googleHelper;

    @Override
    protected void prepareAPICallContext() throws ConfigurationLoadException, ValidationException, OAuthException, ConfigurationException {
        googleHelper = new GoogleProcessorHelper(orgId);
        googleHelper.authenticate();
        googleHelper.generateServices();
    }

    @Override
    protected List<SocialAdSet> getAllAdBatch() throws RemoteException {
        Selector selector = getSelector();
        AdGroupPage adGroupPage = googleHelper.adGroupService.get(selector);
        if(adGroupPage.getEntries() != null) {
            for(AdGroup adGroup : adGroupPage.getEntries()) {
                logger.info("ad group with name {}, id {} and campaign id {} was found", new Object[]{adGroup.getName(), adGroup.getId(), adGroup.getCampaignId()});
            }
        }
        return googleHelper.toSocialAdSet(adGroupPage.getEntries());
    }

    private Selector getSelector() {
        SelectorBuilder selectorBuilder = new SelectorBuilder();
        return selectorBuilder.fields(AdGroupField.Id, AdGroupField.Name, AdGroupField.CampaignId, AdGroupField.Status).build();
    }

}
