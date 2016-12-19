package com.capillary.social.external.impl;

import com.capillary.social.ButtonMessage;
import com.capillary.social.FacebookException;
import com.capillary.social.FacebookService.Iface;
import com.capillary.social.services.api.FacebookMessage;
import com.capillary.social.services.impl.FacebookSendTextMessage;
import com.capillary.social.systems.config.SystemConfig;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import com.capillary.social.services.impl.FacebookSendButtonMessage;

public class FacebookServiceListener implements Iface {

    private static Logger logger = LoggerFactory.getLogger(FacebookServiceListener.class);
    @Autowired
    private SystemConfig systemConfig;

    @Override
    public boolean isAlive() throws TException {

        logger.info("Is alive called");
        return true;
    }

    @Override
    public boolean sendMessage(String recipientId, String messageText, String pageId, int orgId)
            throws FacebookException, TException {

        logger.info("send message called: Recipient Id: "
                    + recipientId
                    + "Message Text: "
                    + messageText
                    + "Page Id: "
                    + pageId
                    + "Org Id: "
                    + orgId);

        MDC.put("requestOrgId", "ORG_ID_" + orgId);
        MDC.put("requestId", "PAGE_ID_" + pageId);
        MDC.put("requestType", System.currentTimeMillis() + "");
        MDC.put("userID", "USER_ID_" + recipientId);

        try {

            FacebookMessage facebookMessage = new FacebookSendTextMessage(messageText);
            facebookMessage.send(recipientId, pageId, orgId);

        } catch (Exception e) {
            logger.error("exception in sending message", e);
            return false;
        } finally {
            MDC.remove("requestOrgId");
            MDC.remove("requestId");
            MDC.remove("requestType");
            MDC.remove("userID");

        }
        return true;

    }

    @Override
    public boolean sendButtonMessage(String recipientId, ButtonMessage buttonMessage, String pageId, int orgId)
            throws FacebookException, TException {
        logger.info("send button message called for recipient id : "
                    + recipientId
                    + " page id : "
                    + pageId
                    + " org id : "
                    + orgId);
        try {
            FacebookMessage facebookSendButtonMessage = new FacebookSendButtonMessage(buttonMessage);
            facebookSendButtonMessage.send(recipientId, pageId, orgId);

        } catch (Exception e) {
            logger.error("exception occured in sending button message", e);
            return false;
        }
        return true;
    }
}
