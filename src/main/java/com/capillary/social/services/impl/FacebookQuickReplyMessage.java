package com.capillary.social.services.impl;

import static com.capillary.social.services.impl.FacebookConstants.CONTENT_TYPE;
import static com.capillary.social.services.impl.FacebookConstants.ID;
import static com.capillary.social.services.impl.FacebookConstants.IMAGE_URL;
import static com.capillary.social.services.impl.FacebookConstants.MESSAGE;
import static com.capillary.social.services.impl.FacebookConstants.PAYLOAD;
import static com.capillary.social.services.impl.FacebookConstants.QUICK_REPLIES;
import static com.capillary.social.services.impl.FacebookConstants.RECIPIENT;
import static com.capillary.social.services.impl.FacebookConstants.TEXT;
import static com.capillary.social.services.impl.FacebookConstants.TITLE;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capillary.social.QuickReply;
import com.capillary.social.QuickReplyMessage;
import com.capillary.social.services.api.FacebookMessage;
import com.capillary.social.validator.QuickReplyListValidator;
import com.capillary.social.validator.QuickReplyMessageTextValidator;
import com.google.common.base.Strings;
import com.google.gson.JsonObject;

public class FacebookQuickReplyMessage extends FacebookMessage {

    private static Logger logger = LoggerFactory.getLogger(FacebookQuickReplyMessage.class);

    private QuickReplyMessage quickReplyMessage;

    public FacebookQuickReplyMessage(QuickReplyMessage quickReplyMessage) {
        this.quickReplyMessage = quickReplyMessage;
    }

    @Override
    public boolean validateMessage() {
        logger.debug("validating quick reply : " + quickReplyMessage);
        return new QuickReplyMessageTextValidator(quickReplyMessage.text).validate()
               && new QuickReplyListValidator(quickReplyMessage.quickReplyList).validate();
    }

    @Override
    public JsonObject messagePayload(String recipientId) {
        JsonObject messageJson = new JsonObject();
        JsonObject recipientBody = new JsonObject();
        JsonObject messageBody = getMessageBody();
        recipientBody.addProperty(ID, recipientId);
        messageJson.add(RECIPIENT, recipientBody);
        messageJson.add(MESSAGE, messageBody);
        return messageJson;
    }

    private JsonObject getMessageBody() {
        JsonObject messageBody = new JsonObject();
        List<JsonObject> quickReplyList = getQuickReplyJsonObjects(quickReplyMessage.quickReplyList);
        messageBody.addProperty(TEXT, quickReplyMessage.text);
        messageBody.addProperty(QUICK_REPLIES, quickReplyList.toString());
        return messageBody;
    }

    private List<JsonObject> getQuickReplyJsonObjects(List<QuickReply> quickReplyList) {
        List<JsonObject> quickReplies = new ArrayList<JsonObject>();
        for (QuickReply quickReply : quickReplyList) {
            quickReplies.add(generateQuickReply(quickReply));
        }
        return quickReplies;
    }

    private JsonObject generateQuickReply(QuickReply quickReply) {
        JsonObject quickReplyJson = new JsonObject();
        quickReplyJson.addProperty(CONTENT_TYPE, quickReply.contentType.toString());
        if (!Strings.isNullOrEmpty(quickReply.title))
            quickReplyJson.addProperty(TITLE, quickReply.title);
        if (!Strings.isNullOrEmpty(quickReply.payload))
            quickReplyJson.addProperty(PAYLOAD, quickReply.payload);
        if (!Strings.isNullOrEmpty(quickReply.imageUrl))
            quickReplyJson.addProperty(IMAGE_URL, quickReply.imageUrl);
        return quickReplyJson;
    }

}
