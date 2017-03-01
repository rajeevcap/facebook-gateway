package com.capillary.social.services.impl;

import static com.capillary.social.services.impl.FacebookConstants.ATTACHMENT;
import static com.capillary.social.services.impl.FacebookConstants.BUTTON;
import static com.capillary.social.services.impl.FacebookConstants.BUTTONS;
import static com.capillary.social.services.impl.FacebookConstants.ID;
import static com.capillary.social.services.impl.FacebookConstants.MESSAGE;
import static com.capillary.social.services.impl.FacebookConstants.PAYLOAD;
import static com.capillary.social.services.impl.FacebookConstants.RECIPIENT;
import static com.capillary.social.services.impl.FacebookConstants.TEMPLATE;
import static com.capillary.social.services.impl.FacebookConstants.TEMPLATE_TYPE;
import static com.capillary.social.services.impl.FacebookConstants.TEXT;
import static com.capillary.social.services.impl.FacebookConstants.TITLE;
import static com.capillary.social.services.impl.FacebookConstants.TYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.capillary.social.Button;
import com.capillary.social.ButtonField;
import com.capillary.social.ButtonMessage;
import com.capillary.social.MessageType;
import com.capillary.social.services.api.FacebookMessage;
import com.capillary.social.validator.ButtonListValidator;
import com.capillary.social.validator.ButtonMessageTextValidator;
import com.google.gson.JsonObject;

@Component
public class FacebookButtonMessage extends FacebookMessage {

    private static Logger logger = LoggerFactory.getLogger(FacebookButtonMessage.class);

    private ButtonMessage buttonMessage;

    public FacebookButtonMessage() {
    }

    public FacebookButtonMessage(ButtonMessage buttonMessage) {
        this.buttonMessage = buttonMessage;
    }

    @Override
    public boolean validateMessage() {
        logger.info("validating button message : " + buttonMessage);
        return new ButtonMessageTextValidator(buttonMessage.text).validate()
               && new ButtonListValidator(buttonMessage.buttonList, MessageType.buttonMessage).validate();
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
        JsonObject attachmentBody = new JsonObject();
        JsonObject payloadBody = new JsonObject();
        List<JsonObject> buttons = getButtonJsonObjects(buttonMessage.buttonList);
        payloadBody.addProperty(TEMPLATE_TYPE, BUTTON);
        payloadBody.addProperty(TEXT, buttonMessage.text);

        payloadBody.addProperty(BUTTONS, buttons.toString());

        attachmentBody.addProperty(TYPE, TEMPLATE);
        attachmentBody.add(PAYLOAD, payloadBody);

        messageBody.add(ATTACHMENT, attachmentBody);
        return messageBody;
    }

    private List<JsonObject> getButtonJsonObjects(List<Button> buttonList) {
        List<JsonObject> buttons = new ArrayList<JsonObject>();
        for (Button button : buttonList) {
            buttons.add(generateButton(button));
        }
        return buttons;
    }

    private JsonObject generateButton(Button button) {
        JsonObject buttonJson = new JsonObject();
        buttonJson.addProperty(TYPE, button.type.toString());
        buttonJson.addProperty(TITLE, button.title);
        for (Map.Entry<ButtonField, String> entry : button.data.entrySet()) {
            buttonJson.addProperty(entry.getKey().toString(), entry.getValue());
        }
        return buttonJson;
    }

    public void setButtonMessage(ButtonMessage buttonMessage) {
        this.buttonMessage = buttonMessage;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.buttonMessage;
    }
}
