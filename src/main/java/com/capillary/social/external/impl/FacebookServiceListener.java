package com.capillary.social.external.impl;

import in.capillary.ifaces.Shopbook.AccountDetails;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import com.capillary.social.FacebookException;
import com.capillary.social.FacebookService.Iface;
import com.capillary.social.library.api.FacebookAccountDetails;
import com.capillary.social.services.impl.FacebookSendTextMessage;
import com.capillary.social.systems.config.SystemConfig;

public class FacebookServiceListener implements Iface {

	private static Logger logger = LoggerFactory
			.getLogger(FacebookServiceListener.class);
	@Autowired
	private SystemConfig systemConfig;

	@Override
	public boolean isAlive() throws TException {
		// TODO Auto-generated method stub
		logger.info("Is alive called");
		return true;
	}

	// TODO: remove accessToken on the method contract
	// Receive senderId=pageId and retrieve accessToken
	@Override
	public boolean sendMessage(String recipientId, String messageText,
			String pageId, int orgId) throws FacebookException, TException {
		// TODO Auto-generated method stub
		logger.info("send message called: Recipient Id: " + recipientId
				+ "Message Text: " + messageText + "Page Id: " + pageId
				+ "Org Id: " + orgId);

		MDC.put("requestOrgId", "ORG_ID_" + orgId);
		MDC.put("requestId", "PAGE_ID_" + pageId);
		MDC.put("requestType", System.currentTimeMillis() + "");
		MDC.put("userID", "USER_ID_" + recipientId);

		try {

			FacebookSendTextMessage facebookSendTextMessage = new FacebookSendTextMessage();
			facebookSendTextMessage.send(recipientId, messageText, pageId,
					orgId);

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

	public String getAccessToken(Integer orgId, String pageId) {
		logger.info("Inside Access Token of Facebook Listener Service");
		FacebookAccountDetails facebookAccountDetails = new FacebookAccountDetails();
		AccountDetails result = facebookAccountDetails.getAccountDetails(orgId,
				pageId.replace("\"", ""));
		String accessToken = result.pageAccessToken;
		logger.info("Access Token: " + accessToken);
		return accessToken;

	}

}
