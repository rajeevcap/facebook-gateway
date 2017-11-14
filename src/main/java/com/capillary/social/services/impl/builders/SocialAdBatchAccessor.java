package com.capillary.social.services.impl.builders;

import com.capillary.social.AdSetStatus;
import com.capillary.social.SocialAdSet;
import com.capillary.social.commons.dao.api.SocialAudienceListDao;
import com.capillary.social.handler.ApplicationContextAwareHandler;
import com.capillary.social.services.api.builders.ISocialAdBatchAccessor;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroup;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroupStatus;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponseException;
import com.google.api.ads.adwords.lib.utils.ReportException;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static com.google.api.ads.adwords.axis.v201710.cm.AdGroupStatus.ENABLED;
import static com.google.api.ads.adwords.axis.v201710.cm.AdGroupStatus.PAUSED;

/**
 * Created by rajeev on 10/11/17.
 */
public abstract class SocialAdBatchAccessor extends SocialProcessor implements ISocialAdBatchAccessor {

    private static final Logger logger = LoggerFactory.getLogger(SocialListAccessor.class);

    private long orgId;

    protected abstract void generateAuthentication() throws ConfigurationLoadException, ValidationException, OAuthException;

    protected abstract List<SocialAdSet> getAllAdBatch() throws RemoteException, ReportDownloadResponseException, ReportException;

    private void setFields(long orgId) {
        this.orgId = orgId;
    }

    @Override
    public List<SocialAdSet> getAll(long orgId) throws RemoteException, ConfigurationLoadException, OAuthException, ValidationException, ReportDownloadResponseException, ReportException {
        setFields(orgId);
        generateAuthentication();
        return getAllAdBatch();
    }

    List<SocialAdSet> toSocialAdSet(AdGroup[] adGroups) {
        List<SocialAdSet> socialAdSets = new ArrayList<>();
        for(AdGroup adGroup : adGroups) {
            socialAdSets.add(toSocialAdSet(adGroup));
        }
        return socialAdSets;
    }

    SocialAdSet toSocialAdSet(AdGroup adGroup) {
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

}
