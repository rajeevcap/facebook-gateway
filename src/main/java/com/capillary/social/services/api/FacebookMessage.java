package com.capillary.social.services.api;

import static com.capillary.social.services.impl.FacebookConstants.SEND_MESSAGE_URL;
import in.capillary.ifaces.Shopbook.AccountDetails;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

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
import com.google.gson.JsonObject;

public abstract class FacebookMessage {

    private static Logger logger = LoggerFactory.getLogger(FacebookMessage.class);

    public abstract JsonObject messagePayload(String recipientId);

    public abstract boolean validateMessage();

    public JsonObject send(String recipientId, String pageId, long orgId) {

        JsonObject output = new JsonObject();
        try {
            boolean isValid = validateMessage();
            if (!isValid)
                return output;

            JsonObject payload = messagePayload(recipientId);

            logger.info("final message payload : " + payload);

            HttpResponse response = sendMessage(pageId, orgId, payload);

            if (response.getStatusLine().getStatusCode() != HttpResponseStatus.OK.getCode()) {
                logger.info("Recieved Error code while doing a post request: errorCode : {}, response: {}", response
                        .getStatusLine()
                        .getStatusCode(), response);

                return output;

            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line = "";
            StringBuffer result = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            output = payload;
            output.addProperty("response", result.toString());
            logger.info("successfully sent the messages: {}", result);
        } catch (Exception e) {
            logger.error("exception in sending message", e);
            return output;
        }
        return output;

    }

    protected HttpResponse sendMessage(String pageId, long orgId, JsonObject payload) throws UnsupportedEncodingException,
            IOException, ClientProtocolException {
        String accessToken = getAccessToken(orgId, pageId);

        String url = SEND_MESSAGE_URL + accessToken;
        logger.info("send_message_url: {}", url);

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json");

        post.setEntity(new StringEntity(payload.toString()));
        HttpResponse response = client.execute(post);
        return response;
    }

    public String getAccessToken(long orgId, String pageId) {
        logger.info("Inside Access Token of Facebook Listener Service");
        FacebookAccountDetails facebookAccountDetails = new FacebookAccountDetails();
        AccountDetails result = facebookAccountDetails.getAccountDetails(orgId, pageId);
        String accessToken = result.pageAccessToken;
        logger.info("Access Token: " + accessToken);
//        String accessToken = "EAARlLJ0mBswBAJ3AywiSIoVRAeOEdZBZBxBLOMGagzbY8s7SncAjmC9j0ZAgF7MDvLXW8qTadZCDJOJl3hAHZB1wmWQqPktJVDMZC12WNDuAXhi5qvd05YiPzxQ0QQEg7jLOsGWMoWkLinTyPxT7ZCZB0qxASdSxisekQsUiK47E7wZDZD";
        return accessToken;

    }

}
