package com.capillary.social.services.impl;

import static com.capillary.social.ListMessageTopElementStyle.large;
import static com.capillary.social.services.impl.FacebookConstants.ATTACHMENT;
import static com.capillary.social.services.impl.FacebookConstants.BUTTONS;
import static com.capillary.social.services.impl.FacebookConstants.DEFAULT_ACTION;
import static com.capillary.social.services.impl.FacebookConstants.ELEMENTS;
import static com.capillary.social.services.impl.FacebookConstants.ID;
import static com.capillary.social.services.impl.FacebookConstants.IMAGE_URL;
import static com.capillary.social.services.impl.FacebookConstants.LIST;
import static com.capillary.social.services.impl.FacebookConstants.MESSAGE;
import static com.capillary.social.services.impl.FacebookConstants.PAYLOAD;
import static com.capillary.social.services.impl.FacebookConstants.RECIPIENT;
import static com.capillary.social.services.impl.FacebookConstants.SUBTITLE;
import static com.capillary.social.services.impl.FacebookConstants.TEMPLATE;
import static com.capillary.social.services.impl.FacebookConstants.TEMPLATE_TYPE;
import static com.capillary.social.services.impl.FacebookConstants.TITLE;
import static com.capillary.social.services.impl.FacebookConstants.TOP_ELEMENT_STYLE;
import static com.capillary.social.services.impl.FacebookConstants.TYPE;
import static com.capillary.social.services.impl.FacebookConstants.WEB_URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.capillary.social.Button;
import com.capillary.social.ButtonField;
import com.capillary.social.Element;
import com.capillary.social.ListMessage;
import com.capillary.social.MessageType;
import com.capillary.social.services.api.FacebookMessage;
import com.capillary.social.validator.ElementListValidator;
import com.google.common.base.Strings;
import com.google.gson.JsonObject;

@Component
public class FacebookListMessage extends FacebookMessage {

    private static Logger logger = LoggerFactory.getLogger(FacebookListMessage.class);

    private ListMessage listMessage;
    
    public FacebookListMessage(){
    }
    
    public FacebookListMessage(ListMessage listMessage) {
        this.listMessage = listMessage;
    }

    @Override
    public boolean validateMessage() {
        logger.info("validating list message : " + listMessage);
        return new ElementListValidator(listMessage.elementList, MessageType.listMessage).validate()
               && !(listMessage.topElementStyle == large && Strings
                       .isNullOrEmpty(listMessage.elementList.get(0).imageUrl))
               && (listMessage.buttonList == null || listMessage.buttonList.size() <= 1);
    }

    private boolean isNotNull(Object obj) {
        return obj != null;
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

        List<JsonObject> elements = getElementJsonObjects(listMessage.elementList);

        payloadBody.addProperty(TEMPLATE_TYPE, LIST);
        payloadBody.addProperty(ELEMENTS, elements.toString());
        if (isNotNull(listMessage.topElementStyle))
            payloadBody.addProperty(TOP_ELEMENT_STYLE, listMessage.topElementStyle.toString());
        if (isNotNull(listMessage.buttonList)) {
            List<Button> newButtonList = new ArrayList<Button>();
            newButtonList.add(listMessage.buttonList.get(0));
            payloadBody.addProperty(BUTTONS, getButtonJsonObjects(newButtonList).toString());
        }
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

    public void setListMessage(ListMessage listMessage) {
        this.listMessage = listMessage;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.listMessage;
    }

}
