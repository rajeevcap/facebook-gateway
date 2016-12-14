package com.capillary.social.external.impl;

import static com.capillary.social.external.impl.FacebookConstants.SEND_MESSAGE_URL;
import in.capillary.ifaces.Shopbook.AccountDetails;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.MDC;
import org.apache.thrift.TException;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.capillary.social.FacebookException;
import com.capillary.social.FacebookService.Iface;
import com.capillary.social.library.api.FacebookAccountDetails;
import com.capillary.social.systems.config.SystemConfig;
import com.google.gson.JsonObject;

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
		StringBuffer result = new StringBuffer();

		MDC.put("requestOrgId", "ORG_ID_" + orgId);
		MDC.put("requestId", "PAGE_ID_" + pageId);
		MDC.put("requestType", System.currentTimeMillis() + "");
		MDC.put("userID", "USER_ID_" + recipientId);

		try {

			String accessToken = getAccessToken(orgId, pageId);

			String url = SEND_MESSAGE_URL + accessToken;
			logger.info("send_message_url: {}", url);

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);

			post.setHeader("Content-Type", "application/json");
			JsonObject recipientEntry = new JsonObject();
			recipientEntry.addProperty("id",
					recipientId.replaceAll("^\"|\"$", ""));
			JsonObject messageEntry = new JsonObject();
			messageEntry.addProperty("text",
					messageText.replaceAll("^\"|\"$", ""));
			JsonObject messagePayload = new JsonObject();
			messagePayload.add("recipient", recipientEntry);
			messagePayload.add("message", messageEntry);

			logger.info("Final String: " + messagePayload.toString());

			post.setEntity(new StringEntity(messagePayload.toString()));
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() != HttpResponseStatus.OK
					.getCode()) {
				logger.info(
						"Recieved Error code while doing a post request: errorCode : {}, response: {}",
						response.getStatusLine().getStatusCode(), response);

				return false;

			}
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			logger.info("successfully sent the messages: {}", result);

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
