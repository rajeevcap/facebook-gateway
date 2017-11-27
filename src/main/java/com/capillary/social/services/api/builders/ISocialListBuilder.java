package com.capillary.social.services.api.builders;

import com.capillary.social.UserDetails;
import com.capillary.social.commons.model.SocialAudienceList;
import com.google.api.ads.adwords.axis.v201710.cm.ApiException;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by rajeev on 3/11/17.
 * Interface for building and saving social groups
 *
 */
public interface ISocialListBuilder {

    /**
     * @param userDetails list of email, mobile pair of the user
     * @param listName
     * @param listDescription
     * @param orgId
     * @param recipientListId group version id of the list in campaign shard
     * @return (string) remote list id generated after audience list creation
     */
    public String build(List<UserDetails> userDetails, String listName, String listDescription, long orgId, String recipientListId) throws RemoteException, ConfigurationLoadException, OAuthException, ValidationException, UnsupportedEncodingException, Exception, ApiException;
/*
    *//**
     * @param audienceLists list of abstract audience type for all social channels
     * @return boolean showing save status
     *//*
    public boolean save(List<SocialAudienceList> audienceLists);*/
}
