package com.capillary.social;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacebookEntityGenerator {

    public static TextMessage generateTextMessage() {
        TextMessage textMessage = new TextMessage();
        textMessage.text = "sample text message";
        return textMessage;
    }

    public static ButtonMessage generateButtonMessage() {
        ButtonMessage buttonMessage = new ButtonMessage();
        ArrayList<Button> buttonList = new ArrayList<Button>();
        Button button = generateButton();
        buttonList.add(button);

        buttonMessage.buttonList = buttonList;
        buttonMessage.text = "This is button text";

        return buttonMessage;
    }

    public static GenericMessage generateGenericMessage() {
        GenericMessage genericMessage = new GenericMessage();
        List<Element> elementList = new ArrayList<Element>();
        Element element = new Element();
        element.title = "Welcome to Capillary!";
        element.subtitle = "How may I help you?";
        element.imageUrl = "https://cabinetm-stag.s3.amazonaws.com/0000014b-2c03-e32f-d99e-d1b144b662d8.jpg";
        element.defaultAction = generateButton();
        ArrayList<Button> buttonList = new ArrayList<Button>();
        Button button = generateButton();
        buttonList.add(button);
        buttonList.add(button);
        element.buttonList = buttonList;
        elementList.add(element);
        elementList.add(element);
        genericMessage.elementList = elementList;
        return genericMessage;
    }

    public static Button generateButton() {
        Button button = new Button();
        Map<ButtonField, String> data = new HashMap<ButtonField, String>();

        data.put(ButtonField.url, "https://gmail.com");

        button.data = data;
        button.type = ButtonType.web_url;
        button.title = "Show Website";
        return button;
    }

    public static QuickReplyMessage generateQuickReplyTextMessage() {
        QuickReplyMessage quickReplyMessage = new QuickReplyMessage();
        List<QuickReply> quickReplyList = new ArrayList<QuickReply>();
        QuickReply quickReply1 = new QuickReply();
        QuickReply quickReply2 = new QuickReply();
        QuickReply quickReply3 = new QuickReply();
        quickReply1.content_type = quickReply2.content_type = quickReply3.content_type = QuickReplyContentType.text;
        quickReply1.title = "alpha";
        quickReply1.image_url = "http://www.thepointless.com/images/reddot.jpg";
        quickReply2.title = "bravo";
        quickReply2.image_url = "http://www.thepointless.com/images/greendot.jpg";
        quickReply3.title = "charlie";
        quickReply3.image_url = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/48/Bluedot.svg/402px-Bluedot.svg.png";
        quickReply1.payload = quickReply2.payload = quickReply3.payload = "thereisnothinghere";
        quickReplyList.add(quickReply1);
        quickReplyList.add(quickReply2);
        quickReplyList.add(quickReply3);
        quickReplyMessage.text = "quick reply message text";
        quickReplyMessage.quickReplyList = quickReplyList;
        return quickReplyMessage;
    }

    public static QuickReplyMessage generateQuickReplyLocationMessage() {
        QuickReplyMessage quickReplyMessage = new QuickReplyMessage();
        List<QuickReply> quickReplyList = new ArrayList<QuickReply>();
        QuickReply quickReply = new QuickReply();
        quickReply.content_type = QuickReplyContentType.location;
        quickReplyList.add(quickReply);
        quickReplyMessage.quickReplyList = quickReplyList;
        quickReplyMessage.text = "Please share your location";
        return quickReplyMessage;
    }

}
