package com.capillary.social.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capillary.social.Button;
import com.capillary.social.ButtonField;
import com.capillary.social.ButtonMessage;
import com.capillary.social.services.api.FacebookMessage;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

public class FacebookSendButtonMessage extends FacebookMessage {
	
	private static Logger logger = LoggerFactory
			.getLogger(FacebookSendTextMessage.class);
	
	private static final Type type = new TypeToken<Map<String, Object>>() {
		private static final long serialVersionUID = 1L;}.getType();
		
	private ButtonMessage buttonMessage;
	
	public FacebookSendButtonMessage(ButtonMessage buttonMessage) {
	    this.buttonMessage = buttonMessage;
	}

	@Override
	public boolean validateMessage() {
		logger.info("validating message : " + buttonMessage);
		if(buttonMessage.text.length() > 640) {
			logger.debug("message text length exceeds the limit");
			return false;
		}
		// utf-8 check
		boolean isValid = true;
		List<Button> buttons = buttonMessage.buttonList;
		for(Button button : buttons) {
			if(button.title == null) {
				logger.debug("button title empty");
				return false;
			} else if(button.title.length() > 20) {
				logger.debug("button title length exceeds the limit");
				return false;
			}
			switch(button.type) {
				case web_url:
					isValid &= validateUrl(button.data.get("url"));
				case postback:
					isValid &= validatePayload(button.data.get("payload"));
				case phone_number:
					isValid &= validatePayload(button.data.get("payload"));
				case element_share:
					// always valid
				case payment:
					isValid &= validatePayload(button.data.get("payload"));
					isValid &= validatePaymentSummary(button.data.get("payment_summary"));
				case account_link:
					isValid &= validateUrl(button.data.get("url"));
				case account_unlink:
					// always valid
				default:
					logger.debug("invalid button type");
					isValid = false;
			}
			if(!isValid) {
			    return false;
			}
		}
		return true;
	}

	private boolean validateUrl(String url) {
		if(url == null || url.isEmpty()) {
			logger.debug("url is missing");
			return false;
		}
		return true;
	}

	private boolean validatePayload(String payload) {
		if(payload == null || payload.isEmpty()) {
			logger.debug("payload is missing");
			return false;
		} else if(payload.length() > 1000) {
			logger.debug("payload length exceeds the limit");
			return false;
		}
		return true;
	}

	private boolean validatePaymentSummary(String paymentSummary) {
		Map<String, Object> paymentSummaryMap = new Gson().fromJson(paymentSummary, type);
		if(!paymentSummaryMap.containsKey("currency")) {
			logger.debug("payment summary validation failed, currency missing");
			return false;
		}
		if(!paymentSummaryMap.containsKey("payment_type")) {
		    logger.debug("payment summary validation failed, payment type missing");
            return false;
		}
		if(!paymentSummaryMap.containsKey("merchant_name")) {
		    logger.debug("payment summary validation failed, merchant name missing");
            return false;
		}
		if(!paymentSummaryMap.containsKey("requested_user_info")) {
		    logger.debug("payment summary validation failed, requested user info missing");
            return false;
		}
		if(!paymentSummaryMap.containsKey("price_list")) {
		    logger.debug("payment summary validation failed, price list missing");
            return false;
		}
		List<Map<String, Object> > priceLists = new Gson().fromJson((String)paymentSummaryMap.get("price_list"), type);
		for(Map<String, Object> priceList : priceLists) {
			if(!priceList.containsKey("label")) {
			    logger.debug("payment summary validation failed, lable missing in price list");
	            return false;
			}
			if(!priceList.containsKey("amount")) {
			    logger.debug("payment summary validation failed, amount missing in price list");
	            return false;
			}
		}
		return true;
	}

    @Override
    public String messagePayload(String recipientId) {
        JsonObject messageJson = new JsonObject();
        JsonObject recipientBody = new JsonObject();
        JsonObject messageBody = getMessageBody();
        recipientBody.addProperty("id", recipientId);
        messageJson.add("recipient", recipientBody);
        messageJson.add("message", messageBody);
        return messageJson.toString();
    }

	private JsonObject getMessageBody() {
		JsonObject messageBody = new JsonObject();
		JsonObject attachmentBody = new JsonObject();
		JsonObject payloadBody = new JsonObject();
		List<JsonObject> buttons = getButtonJsonObjects(buttonMessage.buttonList);
		payloadBody.addProperty("template_type", "button");
		payloadBody.addProperty("text", buttonMessage.text);
		
		for(JsonObject button : buttons) {
			payloadBody.add("buttons", button);
		}
		
		attachmentBody.addProperty("type", "template");
		attachmentBody.add("payload", payloadBody);
		
		messageBody.add("attachment", attachmentBody);
		return messageBody;
	}

	private List<JsonObject> getButtonJsonObjects(List<Button> buttonList) {
		List<JsonObject> buttons = new ArrayList<JsonObject>();
		for(Button button : buttonList) {
		    buttons.add(generateButton(button));
		}
		return buttons;
	}

    private JsonObject generateButton(Button button) {
        JsonObject buttonJson = new JsonObject();
        buttonJson.addProperty("type", button.type.toString());
        if(button.title != null && !button.title.isEmpty()) {
            buttonJson.addProperty("title", button.title);
        }
        for(Map.Entry<ButtonField, String> entry : button.data.entrySet()) {
            buttonJson.addProperty(entry.getKey().toString(), entry.getValue());
        }
        return buttonJson;
    }
}
