package com.capillary.social.external.impl;

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
import com.capillary.social.handler.FacebookMessageHandler;
import com.capillary.social.services.api.FacebookMessage;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class FacebookServiceListener implements Iface {

    private static Logger logger = LoggerFactory.getLogger(FacebookServiceListener.class);

    private FacebookMessageHandler facebookMessageHandler;

    public FacebookServiceListener(FacebookMessageHandler facebookMessageHandler) {
        super();
        this.facebookMessageHandler = facebookMessageHandler;
    }

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
            FacebookMessage facebookTextMessage = facebookMessageHandler.getFacebookTextMessage(textMessage);
            gtwResponse = facebookTextMessage.send(recipientId, senderId, orgId, MessageType.textMessage);

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
            FacebookMessage facebookButtonMessage = facebookMessageHandler.getFacebookButtonMessage(buttonMessage);
            gtwResponse = facebookButtonMessage.send(recipientId, senderId, orgId, MessageType.buttonMessage);
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
            FacebookMessage facebookGenericMessage = facebookMessageHandler.getFacebookGenericMessage(genericMessage);
            gtwResponse = facebookGenericMessage.send(recipientId, senderId, orgId, MessageType.genericMessage);
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
            FacebookMessage facebookQuickReplyMessage = facebookMessageHandler
                    .getFacebookQuickReplyMessage(quickReplyMessage);
            gtwResponse = facebookQuickReplyMessage.send(recipientId, senderId, orgId, MessageType.quickReplyMessage);
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
            FacebookMessage facebookReceiptMessage = facebookMessageHandler.getFacebookReceiptMessage(receiptMessage);
            gtwResponse = facebookReceiptMessage.send(recipientId, senderId, orgId, MessageType.receiptMessage);
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
            FacebookMessage facebookListMessage = facebookMessageHandler.getFacebookListMessage(listMessage);
            gtwResponse = facebookListMessage.send(recipientId, senderId, orgId, MessageType.listMessage);
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

}
