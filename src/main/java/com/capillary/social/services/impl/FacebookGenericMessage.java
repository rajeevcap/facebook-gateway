package com.capillary.social.services.impl;

import static com.capillary.social.services.impl.FacebookConstants.ATTACHMENT;
import static com.capillary.social.services.impl.FacebookConstants.BUTTONS;
import static com.capillary.social.services.impl.FacebookConstants.DEFAULT_ACTION;
import static com.capillary.social.services.impl.FacebookConstants.ELEMENTS;
import static com.capillary.social.services.impl.FacebookConstants.GENERIC;
import static com.capillary.social.services.impl.FacebookConstants.ID;
import static com.capillary.social.services.impl.FacebookConstants.IMAGE_URL;
import static com.capillary.social.services.impl.FacebookConstants.MESSAGE;
import static com.capillary.social.services.impl.FacebookConstants.PAYLOAD;
import static com.capillary.social.services.impl.FacebookConstants.RECIPIENT;
import static com.capillary.social.services.impl.FacebookConstants.SUBTITLE;
import static com.capillary.social.services.impl.FacebookConstants.TEMPLATE;
import static com.capillary.social.services.impl.FacebookConstants.TEMPLATE_TYPE;
import static com.capillary.social.services.impl.FacebookConstants.TITLE;
import static com.capillary.social.services.impl.FacebookConstants.TYPE;
import static com.capillary.social.services.impl.FacebookConstants.WEB_URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capillary.social.Button;
import com.capillary.social.ButtonField;
import com.capillary.social.Element;
import com.capillary.social.GenericMessage;
import com.capillary.social.MessageType;
import com.capillary.social.services.api.FacebookMessage;
import com.capillary.social.validator.ElementListValidator;
import com.google.common.base.Strings;
import com.google.gson.JsonObject;

@Component
public class FacebookGenericMessage extends FacebookMessage {

    private static Logger logger = LoggerFactory.getLogger(FacebookGenericMessage.class);

    private GenericMessage genericMessage;
    
    public FacebookGenericMessage(){
    }
    
    public FacebookGenericMessage(GenericMessage genericMessage) {
        this.genericMessage = genericMessage;
    }

    private boolean isNotNull(Object obj) {
        return obj != null;
    }

    @Override
    public boolean validateMessage() {
        logger.info("validating generic message : " + genericMessage);
        return new ElementListValidator(genericMessage.elementList, MessageType.genericMessage).validate();
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

        List<JsonObject> elements = getElementJsonObjects(genericMessage.elementList);

        payloadBody.addProperty(TEMPLATE_TYPE, GENERIC);
        payloadBody.addProperty(ELEMENTS, elements.toString());

        attachmentBody.addProperty(TYPE, TEMPLATE);
        attachmentBody.add(PAYLOAD, payloadBody);

        messageBody.add(ATTACHMENT, attachmentBody);
        return messageBody;
    }

    private List<JsonObject> getElementJsonObjects(List<Element> elementList) {
        List<JsonObject> elements = new ArrayList<JsonObject>();
        for (Element element : elementList) {
            elements.add(generateElement(element));
        }
        return elements;
    }

    private JsonObject generateElement(Element element) {
        JsonObject elementJson = new JsonObject();
        elementJson.addProperty(TITLE, element.title);
        if (!Strings.isNullOrEmpty(element.subtitle))
            elementJson.addProperty(SUBTITLE, element.subtitle);
        if (!Strings.isNullOrEmpty(element.imageUrl))
            elementJson.addProperty(IMAGE_URL, element.imageUrl);
        if (isNotNull(element.defaultAction)) {
            Button button = element.defaultAction;
            JsonObject defaultActionBody = new JsonObject();
            defaultActionBody.addProperty(TYPE, WEB_URL);
            for (Map.Entry<ButtonField, String> entry : button.data.entrySet()) {
                defaultActionBody.addProperty(entry.getKey().toString(), entry.getValue());
            }
            elementJson.add(DEFAULT_ACTION, defaultActionBody);
        }
        if (isNotNull(element.buttonList)) {
            List<JsonObject> buttons = getButtonJsonObjects(element.buttonList);
            elementJson.addProperty(BUTTONS, buttons.toString());
        }
        return elementJson;
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

    public void setGenericMessage(GenericMessage genericMessage) {
        this.genericMessage = genericMessage;
    }
    @Override
    public MessageType getMessageType() {
        return MessageType.genericMessage;
    }

}
