package com.capillary.social.services.api.builders;

import com.capillary.social.SocialAdSet;

import java.util.List;

/**
 * Created by rajeev on 10/11/17.
 * Inteface for building and saving social groups
 */
public interface ISocialAdBatchAccessor {

    /**
     * @param orgId
     * @return list of social ad set
     */
    public List<SocialAdSet> getAll(long orgId);

}
