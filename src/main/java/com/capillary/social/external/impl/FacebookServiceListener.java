package com.capillary.social.external.impl;

import com.capillary.social.ButtonMessage;
import com.capillary.social.FacebookException;
import com.capillary.social.FacebookService.Iface;
import com.capillary.social.GenericMessage;
import com.capillary.social.QuickReplyMessage;
import com.capillary.social.ReceiptMessage;
import com.capillary.social.TextMessage;
import com.capillary.social.services.api.FacebookMessage;
import com.capillary.social.services.impl.FacebookGenericMessage;
import com.capillary.social.services.impl.FacebookQuickReplyMessage;
import com.capillary.social.services.impl.FacebookReceiptMessage;
import com.capillary.social.services.impl.FacebookTextMessage;
import com.capillary.social.services.impl.FacebookButtonMessage;
import com.google.gson.JsonObject;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class FacebookServiceListener implements Iface {

    private static Logger logger = LoggerFactory.getLogger(FacebookServiceListener.class);

    @Override
    public boolean isAlive() throws TException {

        logger.info("Is alive called");
        return true;
    }

    @Override
    public String sendTextMessage(String recipientId, TextMessage textMessage, String senderId, long orgId)
            throws FacebookException, TException {

        logger.info("send message called for recipient id: "
                    + recipientId
                    + " message text: "
                    + textMessage.text
                    + " sender id: "
                    + senderId
                    + " org id: "
                    + orgId);

        MDC.put("requestOrgId", "ORG_ID_" + orgId);
        MDC.put("requestId", "PAGE_ID_" + senderId);
        MDC.put("requestType", System.currentTimeMillis() + "");
        MDC.put("userID", "USER_ID_" + recipientId);
        JsonObject response = null;
        try {

            FacebookMessage facebookMessage = new FacebookTextMessage(textMessage);
            response = facebookMessage.send(recipientId, senderId, orgId);
        } catch (Exception e) {
            logger.error("exception in sending message", e);
        } finally {
            MDC.remove("requestOrgId");
            MDC.remove("requestId");
            MDC.remove("requestType");
            MDC.remove("userID");
        }
        return response.toString();
    }

    @Override
    public String sendButtonMessage(String recipientId, ButtonMessage buttonMessage, String senderId, long orgId)
            throws FacebookException, TException {
        logger.info("send button message called for recipient id : "
                    + recipientId
                    + "button message : "
                    + buttonMessage
                    + " sender id : "
                    + senderId
                    + " org id : "
                    + orgId);
        MDC.put("requestOrgId", "ORG_ID_" + orgId);
        MDC.put("requestId", "PAGE_ID_" + senderId);
        MDC.put("requestType", System.currentTimeMillis() + "");
        MDC.put("userID", "USER_ID_" + recipientId);
        JsonObject response = null;
        try {
            FacebookMessage facebookButtonMessage = new FacebookButtonMessage(buttonMessage);
            response = facebookButtonMessage.send(recipientId, senderId, orgId);
        } catch (Exception e) {
            logger.error("exception occured in sending button message", e);
        } finally {
            MDC.remove("requestOrgId");
            MDC.remove("requestId");
            MDC.remove("requestType");
            MDC.remove("userID");
        }
        return response.toString();
    }

    @Override
    public String sendGenericMessage(String recipientId, GenericMessage genericMessage, String senderId, long orgId)
            throws FacebookException, TException {
        logger.info("send generic message called for recipient id : "
                    + recipientId
                    + "generic message : "
                    + genericMessage
                    + " sender id : "
                    + senderId
                    + " org id : "
                    + orgId);
        MDC.put("requestOrgId", "ORG_ID_" + orgId);
        MDC.put("requestId", "PAGE_ID_" + senderId);
        MDC.put("requestType", System.currentTimeMillis() + "");
        MDC.put("userID", "USER_ID_" + recipientId);
        JsonObject response = null;
        try {
            FacebookMessage facebookGenericMessage = new FacebookGenericMessage(genericMessage);
            response = facebookGenericMessage.send(recipientId, senderId, orgId);
        } catch (Exception e) {
            logger.error("exception occured in sending button message", e);
        } finally {
            MDC.remove("requestOrgId");
            MDC.remove("requestId");
            MDC.remove("requestType");
            MDC.remove("userID");
        }
        return response.toString();
    }

    public String sendQuickReplyMessage(String recipientId, QuickReplyMessage quickReplyMessage, String senderId,
            long orgId) throws FacebookException, TException {
        logger.info("send quick reply called for recipient id : "
                    + recipientId
                    + "quick reply : "
                    + quickReplyMessage
                    + " sender id : "
                    + senderId
                    + " org id : "
                    + orgId);
        MDC.put("requestOrgId", "ORG_ID_" + orgId);
        MDC.put("requestId", "PAGE_ID_" + senderId);
        MDC.put("requestType", System.currentTimeMillis() + "");
        MDC.put("userID", "USER_ID_" + recipientId);
        JsonObject response = null;
        try {
            FacebookMessage facebookQuickReplyMessage = new FacebookQuickReplyMessage(quickReplyMessage);
            response = facebookQuickReplyMessage.send(recipientId, senderId, orgId);
        } catch (Exception e) {
            logger.error("exception occured in sending quick reply", e);
        } finally {
            MDC.remove("requestOrgId");
            MDC.remove("requestId");
            MDC.remove("requestType");
            MDC.remove("userID");
        }
        return response.toString();
    }

    @Override
    public String sendReceiptMessage(String recipientId, ReceiptMessage receiptMessage, String senderId, long orgId)
            throws FacebookException, TException {
        logger.info("send receipt message called for recipient id : "
                    + recipientId
                    + "receipt message : "
                    + receiptMessage
                    + "sender id : "
                    + senderId
                    + "org id : "
                    + orgId);
        MDC.put("requestOrgId", "ORG_ID_" + orgId);
        MDC.put("requestId", "PAGE_ID_" + senderId);
        MDC.put("requestType", System.currentTimeMillis() + "");
        MDC.put("userID", "USER_ID_" + recipientId);
        JsonObject response = null;
        try {
            FacebookMessage facebookReceiptMessage = new FacebookReceiptMessage(receiptMessage);
            response = facebookReceiptMessage.send(recipientId, senderId, orgId);
        } catch (Exception e) {
            logger.error("exception occured in sending receipt : ");
        } finally {
            MDC.remove("requestOrgId");
            MDC.remove("requestId");
            MDC.remove("requestType");
            MDC.remove("userID");
        }
        return response.toString();
    }
}
