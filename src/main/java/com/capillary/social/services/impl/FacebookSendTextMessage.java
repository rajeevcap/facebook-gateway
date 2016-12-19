package com.capillary.social.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capillary.social.services.api.FacebookMessage;
import com.google.gson.JsonObject;

public class FacebookSendTextMessage extends FacebookMessage {

	private static Logger logger = LoggerFactory
			.getLogger(FacebookSendTextMessage.class);
	
	private String message;
	
	public FacebookSendTextMessage(String message) {
	    this.message = message;
	}

	@Override
	public String messagePayload(String recipientId) {

		JsonObject recipientEntry = new JsonObject();
		recipientEntry.addProperty("id", recipientId.replaceAll("^\"|\"$", ""));
		JsonObject messageEntry = new JsonObject();
		messageEntry.addProperty("text", message.replaceAll("^\"|\"$", ""));
		JsonObject messagePayload = new JsonObject();
		messagePayload.add("recipient", recipientEntry);
		messagePayload.add("message", messageEntry);

		logger.info("Final String: " + messagePayload.toString());
		return messagePayload.toString();

	}

    @Override
    public boolean validateMessage() {
        return true;
    }

}
