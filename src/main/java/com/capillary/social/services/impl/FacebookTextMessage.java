package com.capillary.social.services.impl;

import static com.capillary.social.services.impl.FacebookConstants.ID;
import static com.capillary.social.services.impl.FacebookConstants.MESSAGE;
import static com.capillary.social.services.impl.FacebookConstants.RECIPIENT;
import static com.capillary.social.services.impl.FacebookConstants.TEXT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.capillary.social.MessageType;
import com.capillary.social.TextMessage;
import com.capillary.social.services.api.FacebookMessage;
import com.capillary.social.validator.TextMessageTextValidator;
import com.google.gson.JsonObject;

@Component
public class FacebookTextMessage extends FacebookMessage {

    private static Logger logger = LoggerFactory.getLogger(FacebookTextMessage.class);

    private TextMessage textMessage;
    
    public FacebookTextMessage() {
    }

    public FacebookTextMessage(TextMessage textMessage) {
        this.textMessage = textMessage;
    }

    @Override
    public boolean validateMessage() {
        logger.info("validating text message : " + textMessage);
        return new TextMessageTextValidator(textMessage.text).validate();
    }

    @Override
    public JsonObject messagePayload(String recipientId) {

        JsonObject recipientEntry = new JsonObject();
        recipientEntry.addProperty(ID, recipientId);
        JsonObject messageEntry = new JsonObject();
        messageEntry.addProperty(TEXT, textMessage.text);
        JsonObject messagePayload = new JsonObject();
        messagePayload.add(RECIPIENT, recipientEntry);
        messagePayload.add(MESSAGE, messageEntry);
        return messagePayload;

    }

    public TextMessage getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(TextMessage textMessage) {
        this.textMessage = textMessage;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.textMessage;
    }

}
