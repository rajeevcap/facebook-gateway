package com.capillary.social.external.impl;

import com.capillary.social.*;
import com.capillary.social.commons.data.manager.ShardContext;
import com.capillary.social.services.api.builders.*;
import com.capillary.social.services.impl.builders.*;
import com.capillary.social.services.impl.factories.AdsetInsightsReportBuilderFactory;
import com.capillary.social.services.impl.factories.AdsetsReportBuilderFactory;
import com.capillary.social.services.impl.factories.CustomAudienceListBuilderFactory;
import com.capillary.social.services.impl.factories.CustomAudienceReportsBuilderFactory;
import com.capillary.social.systems.config.LockHolder;
import com.capillary.social.utils.Guard;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.client.auth.oauth2.Credential;
import com.google.common.base.Preconditions;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.capillary.social.FacebookService.Iface;

import com.capillary.social.handler.ApplicationContextAwareHandler;
import com.capillary.social.services.impl.FacebookButtonMessage;
import com.capillary.social.services.impl.FacebookGenericMessage;
import com.capillary.social.services.impl.FacebookListMessage;
import com.capillary.social.services.impl.FacebookQuickReplyMessage;
import com.capillary.social.services.impl.FacebookReceiptMessage;
import com.capillary.social.services.impl.FacebookTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
@Service
public class FacebookServiceListener implements Iface {

    private static Logger logger = LoggerFactory.getLogger(FacebookServiceListener.class);
    @Autowired
    public CustomAudienceListBuilderFactory customAudienceListBuider;
    @Autowired
    private CustomAudienceReportsBuilderFactory customAudienceReportsBuilder;
    @Autowired
    private AdsetsReportBuilderFactory adsetsReportBuilderFactory;
    @Autowired
    private AdsetInsightsReportBuilderFactory adsetInsightsReportBuilderFactory;

    @Override
    public boolean isAlive() throws TException {
        logger.info("is alive called");
        return true;
    }

    @Override
    public GatewayResponse sendTextMessage(String recipientId, TextMessage textMessage, String senderId, long orgId,
            String requestId) throws FacebookException, TException {

        logger.info("send text message called for recipient id: "
                    + recipientId
                    + " message text: "
                    + textMessage.text
                    + " sender id: "
                    + senderId
                    + " org id: "
                    + orgId);

        MDC.put("requestOrgId", "ORG_ID_" + orgId);
        MDC.put("requestId", requestId);
        MDC.put("requestType", "PAGE_ID_" + senderId);
        MDC.put("userID", "USER_ID_" + recipientId);
        GatewayResponse gtwResponse = null;
        try {

            FacebookTextMessage facebookTextMessage = new FacebookTextMessage(textMessage);
            gtwResponse = facebookTextMessage.send(recipientId, senderId, orgId);
        } catch (Exception e) {
            logger.error("exception occured in sending text message", e);
        } finally {
            MDC.remove("requestOrgId");
            MDC.remove("requestId");
            MDC.remove("requestType");
            MDC.remove("userID");
        }
        return gtwResponse;
    }

    @Override
    public GatewayResponse sendButtonMessage(String recipientId, ButtonMessage buttonMessage, String senderId,
            long orgId, String requestId) throws FacebookException, TException {
        logger.info("send button message called for recipient id : "
                    + recipientId
                    + " button message : "
                    + buttonMessage
                    + " sender id : "
                    + senderId
                    + " org id : "
                    + orgId);
        MDC.put("requestOrgId", "ORG_ID_" + orgId);
        MDC.put("requestId", requestId);
        MDC.put("requestType", "PAGE_ID_" + senderId);
        MDC.put("userID", "USER_ID_" + recipientId);
        GatewayResponse gtwResponse = null;
        try {

            FacebookButtonMessage facebookButtonMessage = new FacebookButtonMessage(buttonMessage);
            gtwResponse = facebookButtonMessage.send(recipientId, senderId, orgId);
        } catch (Exception e) {
            logger.error("exception occured in sending button message", e);
        } finally {
            MDC.remove("requestOrgId");
            MDC.remove("requestId");
            MDC.remove("requestType");
            MDC.remove("userID");
        }
        return gtwResponse;
    }

    @Override
    public GatewayResponse sendGenericMessage(String recipientId, GenericMessage genericMessage, String senderId,
            long orgId, String requestId) throws FacebookException, TException {
        logger.info("send generic message called for recipient id : "
                    + recipientId
                    + " generic message : "
                    + genericMessage
                    + " sender id : "
                    + senderId
                    + " org id : "
                    + orgId);
        MDC.put("requestOrgId", "ORG_ID_" + orgId);
        MDC.put("requestId", requestId);
        MDC.put("requestType", "PAGE_ID_" + senderId);
        MDC.put("userID", "USER_ID_" + recipientId);
        GatewayResponse gtwResponse = null;
        try {
            FacebookGenericMessage facebookGenericMessage = new FacebookGenericMessage(genericMessage);
            gtwResponse = facebookGenericMessage.send(recipientId, senderId, orgId);
        } catch (Exception e) {
            logger.error("exception occured in sending generic message", e);
        } finally {
            MDC.remove("requestOrgId");
            MDC.remove("requestId");
            MDC.remove("requestType");
            MDC.remove("userID");
        }
        return gtwResponse;
    }

    @Override
    public GatewayResponse sendQuickReplyMessage(String recipientId, QuickReplyMessage quickReplyMessage,
            String senderId, long orgId, String requestId) throws FacebookException, TException {
        logger.info("send quick reply message called for recipient id : "
                    + recipientId
                    + " quick reply : "
                    + quickReplyMessage
                    + " sender id : "
                    + senderId
                    + " org id : "
                    + orgId);
        MDC.put("requestOrgId", "ORG_ID_" + orgId);
        MDC.put("requestId", requestId);
        MDC.put("requestType", "PAGE_ID_" + senderId);
        MDC.put("userID", "USER_ID_" + recipientId);
        GatewayResponse gtwResponse = null;
        try {
            FacebookQuickReplyMessage facebookQuickReplyMessage = new FacebookQuickReplyMessage(quickReplyMessage);
            gtwResponse = facebookQuickReplyMessage.send(recipientId, senderId, orgId);
        } catch (Exception e) {
            logger.error("exception occured in sending quick reply message", e);
        } finally {
            MDC.remove("requestOrgId");
            MDC.remove("requestId");
            MDC.remove("requestType");
            MDC.remove("userID");
        }
        return gtwResponse;
    }

    @Override
    public GatewayResponse sendReceiptMessage(String recipientId, ReceiptMessage receiptMessage, String senderId,
            long orgId, String requestId) throws FacebookException, TException {
        logger.info("send receipt message called for recipient id : "
                    + recipientId
                    + " receipt message : "
                    + receiptMessage
                    + " sender id : "
                    + senderId
                    + " org id : "
                    + orgId);
        MDC.put("requestOrgId", "ORG_ID_" + orgId);
        MDC.put("requestId", requestId);
        MDC.put("requestType", "PAGE_ID_" + senderId);
        MDC.put("userID", "USER_ID_" + recipientId);
        GatewayResponse gtwResponse = null;
        try {
            FacebookReceiptMessage facebookReceiptMessage = new FacebookReceiptMessage(receiptMessage);
            gtwResponse = facebookReceiptMessage.send(recipientId, senderId, orgId);
        } catch (Exception e) {
            logger.error("exception occured in sending receipt message", e);
        } finally {
            MDC.remove("requestOrgId");
            MDC.remove("requestId");
            MDC.remove("requestType");
            MDC.remove("userID");
        }
        return gtwResponse;
    }

    @Override
    public GatewayResponse sendListMessage(String recipientId, ListMessage listMessage, String senderId, long orgId,
            String requestId) throws FacebookException, TException {
        logger.info("send list message called for recipient id : "
                    + recipientId
                    + " list message : "
                    + listMessage
                    + " sender id : "
                    + senderId
                    + " orgId : "
                    + orgId);
        MDC.put("requestOrgId", "ORG_ID_" + orgId);
        MDC.put("requestId", requestId);
        MDC.put("requestType", "PAGE_ID_" + senderId);
        MDC.put("userID", "USER_ID_" + recipientId);
        GatewayResponse gtwResponse = null;
        try {
            FacebookListMessage facebookListMessage = new FacebookListMessage(listMessage);
            gtwResponse = facebookListMessage.send(recipientId, senderId, orgId);
        } catch (Exception e) {
            logger.error("exception occured in sending list ", e);
        } finally {
            MDC.remove("requestOrgId");
            MDC.remove("requestId");
            MDC.remove("requestType");
            MDC.remove("userID");
        }
        return gtwResponse;
    }

	@Override
	public CreateCustomAudienceListResponse createCustomList(List<UserDetails> userDetailsList, CustomAudienceListDetails customAudienceListDetails, SocialAccountDetails socialAccountDetails, long orgId,String recipientListId, String requestId) throws FacebookException, TException {
		MDC.put("requestOrgId", "ORG_ID_" + orgId);
		MDC.put("requestId", requestId);
		logger.info("createCustomList called for userslist of size: "
				+ userDetailsList.size()
				+ " "
				+ customAudienceListDetails.toString()
				+ socialAccountDetails.toString()
				+ "orgId" + orgId
				+ "recipient list id :"+recipientListId
				+ "request Id "+requestId
				+ "for org"
				+ " orgId : "
				+ orgId);
		ShardContext.set((int) orgId);
		CreateCustomAudienceListResponse createCustomUserListResponse = new CreateCustomAudienceListResponse();
		try {
			Guard.notNull(socialAccountDetails, "socialAccountDetails");
			Guard.notNullOrEmpty(userDetailsList, "userList");
			Guard.notNullOrEmpty(recipientListId,"recipientListId");
//			CustomAudienceListBuider customAudienceListBuider = this.customAudienceListBuider.getBulder(socialAccountDetails.getChannel());
//			String listId = customAudienceListBuider.build(userDetailsList, customAudienceListDetails.name, customAudienceListDetails.description,recipientListId, orgId);
            ISocialListBuilder googleListBuilder = new GoogleListBuilder();
            String listId = googleListBuilder.build(userDetailsList, customAudienceListDetails.getName(), customAudienceListDetails.getDescription(), orgId, recipientListId);
			createCustomUserListResponse.setListid(listId);
			createCustomUserListResponse.setResponse(GatewayResponseType.success);
			createCustomUserListResponse.setMessage("custom audience list has been created successfully");
		} catch (Exception e) {
			logger.error("exception occurred while creating a custom user list", e);
			throw new FacebookException(e.getMessage());
		} finally {
			MDC.remove("requestOrgId");
			MDC.remove("requestId");
		}
		return createCustomUserListResponse;
	}

	@Override
	public GetCustomAudienceListsResponse getCustomAudienceLists(long orgId, SocialChannel socialChannel,boolean clearCache, String requestId) throws FacebookException, TException {
		MDC.put("requestOrgId", "ORG_ID_" + orgId);
		MDC.put("requestId", requestId);
		logger.info("received call for getCustomAudienceLists for orgId {} socialChannel {} clearche {}",new Object[]{orgId, socialChannel, clearCache});
		ShardContext.set((int) orgId);
		GetCustomAudienceListsResponse response = new GetCustomAudienceListsResponse();
		try {
//			CustomAudienceReportsBuilder customAudienceReportsBuilder = this.customAudienceReportsBuilder.getBulder(socialChannel);
//			List<CustomAudienceList> customAudienceLists = customAudienceReportsBuilder.buildAll(orgId,clearCache);
            SocialListAccessor googleListAccessor = new GoogleListAccessor();
            List<CustomAudienceList> customAudienceLists = googleListAccessor.getAll(0);
            response.customAudienceLists = customAudienceLists;
			response.response = GatewayResponseType.success;
			if (customAudienceLists.isEmpty()) {
				response.message = "social channel returned empty list";
			} else {
				response.message = "customAudienceLists has been fetched successfully";
			}
		} catch (Exception e) {
			logger.error("error while getting custom audience list from facebook", e);
			throw new FacebookException(e.getMessage());
		}
		return response;
	}

	@Override
	public List<SocialAdSet> getAdSets(SocialChannel socialChannel, long orgId, String requestId) throws FacebookException, TException {
		MDC.put("requestOrgId", "ORG_ID_" + orgId);
		MDC.put("requestId", requestId);
		logger.info("received call for getAdsets for orgId {} socialChannel {}", orgId, socialChannel);
		try{
//			AdsetsReportsBuilder adsetsReportsBuilder = this.adsetsReportBuilderFactory.getBulder(socialChannel);
//			return adsetsReportsBuilder.buildAll(orgId);
            SocialAdBatchAccessor googleAdGroupAccessor = new GoogleAdGroupAccessor();
            return googleAdGroupAccessor.getAll(0);
		}
		catch (Exception e){
			logger.error("error occurred while fetching adsets",e);
			throw new FacebookException(e.getMessage());
		}
	}

	@Override
	public AdInsight getAdsetInsights(SocialChannel socialChannel, long orgId, String adsetId, boolean clearCache, String requestId) throws FacebookException, TException {
		MDC.put("requestOrgId", "ORG_ID_" + orgId);
		MDC.put("requestId", requestId);
		logger.info("received call for getAdsetInsights for orgId {} socialChannel {}", orgId, socialChannel);
		Guard.notNullOrEmpty(adsetId, "adsetId");
		AdInsight adsInsights = null;
		try {
		    SocialAdReportAccessor googleAdReportAccessor = new GoogleAdReportAccessor();
		    googleAdReportAccessor.getAll(orgId, adsetId);
//			AdsetInsightsReportBuilder builder = adsetInsightsReportBuilderFactory.getBulder(socialChannel);
//			adsInsights = builder.build(orgId, adsetId, clearCache);
			if (adsInsights == null ) {
				logger.warn("could not fetch insights from facebook");
				throw new RuntimeException("Insights are not available");
			}
		} catch (Exception e) {
			logger.error("error occurred while fetching facebook adset with id {}", adsetId, e);
			throw new FacebookException(e.getMessage());
		}
		return adsInsights;
	}

	public static void main(String[] args) throws TException, FacebookException {
        List<UserDetails> userDetails = new ArrayList<>();
        UserDetails ud1 = new UserDetails();
        ud1.setEmail("em1");ud1.setMobile("mb1");
        UserDetails ud2 = new UserDetails();
        ud2.setEmail("em2");ud2.setMobile("mb2");
        userDetails.add(ud1);userDetails.add(ud2);
        try {
            getFacebookServiceClient().getCustomAudienceLists(0, SocialChannel.google, true, "requestId");
//            getFacebookServiceClient().createCustomList(userDetails, new CustomAudienceListDetails("calfdsdname","calddfsaesc"), new SocialAccountDetails(SocialChannel.google), 10l, "12334", "abc");
//            getFacebookServiceClient().getAdSets(SocialChannel.google, 0, "requestId");
//            getFacebookServiceClient().getAdsetInsights(SocialChannel.google, 0, "adSetId", false ,"requestId");
        } catch (Exception e) {
            logger.info("exception caught " + e);
        }
        /*Credential oAuth2Credential = null;
        try {

            OfflineCredentials.Api adwords = OfflineCredentials.Api.ADWORDS;
            OfflineCredentials.Api dfp = Api.DFP;

            oAuth2Credential = new OfflineCredentials.Builder().forApi(adwords).fromFile("ads.properties").build().generateCredential();
        } catch (Exception e) {
            logger.error("exception caught " + e);
        } catch (Error e) {
            logger.error("error caught " + e);
        }*/
        /*try {
            Preconditions.checkNotNull("123", "abc", new Object());
        } catch (Exception e) {
            logger.error("exception " + e);
        } catch (Error e) {
            logger.error("error " + e);
        }*/
    }

    public static FacebookService.Client getFacebookServiceClient() {
        TTransport transport = new TSocket("192.168.33.1", 9232);
        try {
            transport.open();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
        TProtocol protocol = new TBinaryProtocol(transport);
        return (new FacebookService.Client(protocol));
    }


}
