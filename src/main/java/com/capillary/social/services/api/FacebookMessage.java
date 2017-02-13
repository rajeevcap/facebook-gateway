package com.capillary.social.services.api;

import static com.capillary.social.GatewayResponseType.failed;
import static com.capillary.social.GatewayResponseType.invalidContent;
import static com.capillary.social.GatewayResponseType.policyViolation;
import static com.capillary.social.GatewayResponseType.sent;
import static com.capillary.social.MessageType.receiptMessage;
import static com.capillary.social.services.impl.FacebookConstants.MESSAGING_RESPONSE_TIME_LIMIT;
import static com.capillary.social.services.impl.FacebookConstants.SEND_MESSAGE_URL;
import in.capillary.ifaces.Shopbook.AccountDetails;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.capillary.social.GatewayResponse;
import com.capillary.social.GatewayResponseType;
import com.capillary.social.MessageType;
import com.capillary.social.dao.api.ChatDao;
import com.capillary.social.handler.ApplicationContextAwareHandler;
import com.capillary.social.library.api.FacebookAccountDetails;
import com.capillary.social.model.Chat;
import com.capillary.social.model.Chat.ChatStatus;
import com.google.gson.JsonObject;

public abstract class FacebookMessage {

    private static Logger logger = LoggerFactory.getLogger(FacebookMessage.class);
    
    public abstract JsonObject messagePayload(String recipientId);

    public abstract boolean validateMessage();

    private List<MessageType> skipMessageTypesForUserPolicy = new ArrayList<MessageType>(Arrays.asList(receiptMessage));

    public GatewayResponse send(String recipientId, String pageId, long orgId, MessageType messageType) {
        GatewayResponse gtwResponse = new GatewayResponse();
        gtwResponse.response = "{}";
        try {
            if (!canSendMessage(recipientId, pageId, messageType, gtwResponse)) return gtwResponse;

            JsonObject payload = messagePayload(recipientId);
            gtwResponse.message = payload.toString();
            logger.info("final message payload : " + payload);

            HttpResponse response = sendMessage(pageId, orgId, payload);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            StringBuffer result = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            if (response.getStatusLine().getStatusCode() != HttpResponseStatus.OK.getCode()) {
                logger.info("Recieved Error code while doing a post request: errorCode : {}, response: {}", response
                        .getStatusLine()
                        .getStatusCode(), response);
                gtwResponse.gatewayResponseType = failed;
            } else {
                gtwResponse.gatewayResponseType = sent;
                logger.info("successfully sent the messages: {}", result);
            }
            gtwResponse.response = result.toString();
        } catch (Exception e) {
            gtwResponse.gatewayResponseType = GatewayResponseType.failed;
            gtwResponse.response = "exception in sending message";

            logger.error("exception in sending message", e);
        }
        logger.info("send response : " + gtwResponse);
        return gtwResponse;
    }

    private boolean canSendMessage(String recipientId, String pageId, MessageType messageType, GatewayResponse gtwResponse) {
        boolean isMessageSendingPermitted = checkUserPolicy(recipientId, pageId, messageType);
        if (!isMessageSendingPermitted) {
            gtwResponse.gatewayResponseType = policyViolation;
            gtwResponse.response = "{\"error\":{\"message\":\"message blocked due to user policy violation, last message by user was more than 24 hours ago\"}}";
            return false;
        }
        boolean isMessageContentValid = validateMessage();
        if (!isMessageContentValid) {
            gtwResponse.gatewayResponseType = invalidContent;
            gtwResponse.response = "{\"error\":{\"message\":\"message blocked due to content policy violation\"}}";
            return false;
        }
        return true;
    }

    protected HttpResponse sendMessage(String pageId, long orgId, JsonObject payload)
            throws UnsupportedEncodingException, IOException, ClientProtocolException {
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
        return accessToken;
    }

    protected boolean checkUserPolicy(String recipientId, String pageId, MessageType messageType) {
        logger.info("inside checking user policy method");
        if (skipMessageTypesForUserPolicy.contains(messageType)) {
            return true;
        }

        ApplicationContext applicationContext = ApplicationContextAwareHandler.getApplicationContext();
        ChatDao chatDao = (ChatDao) applicationContext.getBean("chatDaoImpl");
        Chat chat = chatDao.findChat(recipientId, pageId, ChatStatus.RECEIVED);
        long timeDifference = new Date().getTime() - chat.getReceivedTime().getTime();
        logger.info("last chat : {} by user was {} milliseconds ago", chat, timeDifference);
        if (chat != null && timeDifference > MESSAGING_RESPONSE_TIME_LIMIT) {
            logger.info("last message sent by user was more than 24 hours ago");
            return false;
        }
        return true;
    }

}
