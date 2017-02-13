package com.capillary.social.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capillary.social.ButtonMessage;
import com.capillary.social.GenericMessage;
import com.capillary.social.ListMessage;
import com.capillary.social.QuickReplyMessage;
import com.capillary.social.ReceiptMessage;
import com.capillary.social.TextMessage;
import com.capillary.social.services.impl.FacebookButtonMessage;
import com.capillary.social.services.impl.FacebookGenericMessage;
import com.capillary.social.services.impl.FacebookListMessage;
import com.capillary.social.services.impl.FacebookQuickReplyMessage;
import com.capillary.social.services.impl.FacebookReceiptMessage;
import com.capillary.social.services.impl.FacebookTextMessage;

@Component
public class FacebookMessageHandler {

    @Autowired
    private FacebookTextMessage facebookTextMessage;

    @Autowired
    private FacebookButtonMessage facebookButtonMessage;

    @Autowired
    private FacebookGenericMessage facebookGenericMessage;

    @Autowired
    private FacebookReceiptMessage facebookReceiptMessage;

    @Autowired
    private FacebookListMessage facebookListMessage;

    @Autowired
    private FacebookQuickReplyMessage facebookQuickReplyMessage;

    public FacebookTextMessage getFacebookTextMessage(TextMessage textMessage) {
        facebookTextMessage.setTextMessage(textMessage);
        return facebookTextMessage;
    }

    public FacebookButtonMessage getFacebookButtonMessage(ButtonMessage buttonMessage) {
        facebookButtonMessage.setButtonMessage(buttonMessage);
        return facebookButtonMessage;
    }

    public FacebookGenericMessage getFacebookGenericMessage(GenericMessage genericMessage) {
        facebookGenericMessage.setGenericMessage(genericMessage);
        return facebookGenericMessage;
    }

    public FacebookReceiptMessage getFacebookReceiptMessage(ReceiptMessage receiptMessage) {
        facebookReceiptMessage.setReceiptMessage(receiptMessage);
        return facebookReceiptMessage;
    }

    public FacebookListMessage getFacebookListMessage(ListMessage listMessage) {
        facebookListMessage.setListMessage(listMessage);
        return facebookListMessage;
    }

    public FacebookQuickReplyMessage getFacebookQuickReplyMessage(QuickReplyMessage quickReplyMessage) {
        facebookQuickReplyMessage.setQuickReplyMessage(quickReplyMessage);
        return facebookQuickReplyMessage;
    }

}
