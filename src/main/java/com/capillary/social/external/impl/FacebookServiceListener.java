package com.capillary.social.external.impl;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.capillary.social.ButtonMessage;
import com.capillary.social.FacebookException;
import com.capillary.social.FacebookService.Iface;
import com.capillary.social.GatewayResponse;
import com.capillary.social.GenericMessage;
import com.capillary.social.ListMessage;
import com.capillary.social.MessageType;
import com.capillary.social.QuickReplyMessage;
import com.capillary.social.ReceiptMessage;
import com.capillary.social.TextMessage;

import com.capillary.social.handler.ApplicationContextAwareHandler;
import com.capillary.social.services.impl.FacebookButtonMessage;
import com.capillary.social.services.impl.FacebookGenericMessage;
import com.capillary.social.services.impl.FacebookListMessage;
import com.capillary.social.services.impl.FacebookQuickReplyMessage;
import com.capillary.social.services.impl.FacebookReceiptMessage;
import com.capillary.social.services.impl.FacebookTextMessage;

public class FacebookServiceListener implements Iface {

    private static Logger logger = LoggerFactory.getLogger(FacebookServiceListener.class);

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
	public CreateCustomAudienceListResponse createCustomList(List<UserDetails> userDetailsList, SocialChannel channel, String listName, String listDescription, long orgId, String adAccountId, String requestId) throws FacebookException, TException {
		MDC.put("requestOrgId", "ORG_ID_" + orgId);
		MDC.put("requestId", requestId);
		logger.info("createCustomList called for userslist of size: "
				+ userDetailsList.size()
				+ " listName : "
				+ listName
				+ " listDescription : "
				+ listDescription
				+ " orgId : "
				+ orgId);
		MDC.put("requestOrgId", "ORG_ID_" + orgId);
		MDC.put("requestId", requestId);
		CreateCustomAudienceListResponse createCustomUserListResponse = new CreateCustomAudienceListResponse();
		try {
			Guard.notNull(channel, "channel");
			Guard.notNullOrEmpty(userDetailsList, "userList");
			Guard.notNullOrEmpty(listName, "listName");
			Guard.notNullOrEmpty(listDescription, "listDescription");
			Guard.notNullOrEmpty(adAccountId, "adAccountId");
			CustomAudienceListBuider customAudienceListBuider = CustomAudienceListBuilderFactory.getInstance().getBulder(channel);
			String listId = customAudienceListBuider.build(userDetailsList, listName, listDescription, orgId, adAccountId);
			createCustomUserListResponse.setList_id(listId);
			createCustomUserListResponse.setResponse(GatewayResponseType.success);
		} catch (Exception e) {
			logger.error("exception occurred while creating a custom user list", e);
			createCustomUserListResponse.setResponse(GatewayResponseType.failed);
			createCustomUserListResponse.setMessage(e.getMessage());
			return createCustomUserListResponse;
		} finally {
			MDC.remove("requestOrgId");
			MDC.remove("requestId");
		}
		return createCustomUserListResponse;
	}

	@Override
	public RemoveFromCustomAudienceListResponse removeFromCustomList(List<UserDetails> userDetailsList, SocialChannel socialChannel, String listId, long orgId, String adsAccountId, String requestId) throws FacebookException, TException {
		MDC.put("requestOrgId", "ORG_ID_" + orgId);
		MDC.put("requestId", requestId);
		logger.info("removeFromCustomList called for userslist of size: "
				+ userDetailsList.size()
				+ " listId : "
				+ listId
				+ " adsAccountId : "
				+ adsAccountId
				+ " orgId : "
				+ orgId);
		MDC.put("requestOrgId", "ORG_ID_" + orgId);
		MDC.put("requestId", requestId);
		RemoveFromCustomAudienceListResponse removeFromCustomAudienceListResponse = new RemoveFromCustomAudienceListResponse();
		try {
			Guard.notNullOrEmpty(userDetailsList,"userlist");
			Guard.notNullOrEmpty(listId,"customaudiencelistid");
			Guard.notNullOrEmpty(adsAccountId,"adsaccountId");
			CustomAudienceListBuider customAudienceListBuider = CustomAudienceListBuilderFactory.getInstance().getBulder(socialChannel);
			customAudienceListBuider.remove(userDetailsList,listId,orgId,adsAccountId);
			removeFromCustomAudienceListResponse.setResponse(GatewayResponseType.success);
		} catch (Exception e) {
			logger.error("exception occurred while removing users from custom list", e);
			removeFromCustomAudienceListResponse.setResponse(GatewayResponseType.failed);
			removeFromCustomAudienceListResponse.setMessage(e.getMessage());
			return removeFromCustomAudienceListResponse;
		} finally {
			MDC.remove("requestOrgId");
			MDC.remove("requestId");
		}
		return removeFromCustomAudienceListResponse;
	}


}
