package com.capillary.social.services.api;

import static com.capillary.social.external.impl.FacebookConstants.SEND_MESSAGE_URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import in.capillary.ifaces.Shopbook.AccountDetails;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capillary.social.library.api.FacebookAccountDetails;

public abstract class FacebookMessage {

	private static Logger logger = LoggerFactory
			.getLogger(FacebookMessage.class);

	public abstract String messagePayload(String recipientId, String messageText);

	public boolean send(String recipientId, String messageText, String pageId,
			int orgId) {

		try {
			String accessToken = getAccessToken(orgId, pageId);

			StringBuffer result = new StringBuffer();
			String url = SEND_MESSAGE_URL + accessToken;
			logger.info("send_message_url: {}", url);

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);

			post.setHeader("Content-Type", "application/json");
			String payload = messagePayload(recipientId, messageText);
			post.setEntity(new StringEntity(payload));
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
