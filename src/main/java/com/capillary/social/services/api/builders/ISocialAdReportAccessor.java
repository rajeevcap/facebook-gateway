package com.capillary.social.services.api.builders;

import com.capillary.social.AdInsight;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponseException;
import com.google.api.ads.adwords.lib.utils.ReportException;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;

import java.io.IOException;

/**
 * Created by rajeev on 10/11/17.
 * Interface for fetching social Ad Reports
 */
public interface ISocialAdReportAccessor {

    /**
     * @param orgId
     * @return list of
     */
    public void getAll(long orgId, String adSetId) throws ConfigurationLoadException, OAuthException, ValidationException, IOException, ReportDownloadResponseException, ReportException;

}
