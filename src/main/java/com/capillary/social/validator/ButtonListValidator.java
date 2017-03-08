package com.capillary.social.validator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capillary.social.Button;
import com.capillary.social.ButtonField;
import com.capillary.social.MessageType;

public class ButtonListValidator {

    private static Logger logger = LoggerFactory.getLogger(ButtonListValidator.class);

    private List<Button> buttonList;

    private MessageType messageType;

    public ButtonListValidator(List<Button> buttonList, MessageType messageType) {
        this.buttonList = buttonList;
        this.messageType = messageType;
    }

    public boolean validate() {
        boolean isValid = true;
        if (!new ButtonListCountValidator(buttonList.size(), messageType).validate()) {
            logger.error("button list count " + buttonList.size() + " is invalid for : " + messageType);
            isValid = false;
        } else if (buttonList.isEmpty() || buttonList == null) {
            logger.debug("button list is empty in button message");
            isValid = false;
        }
        for (Button button : buttonList) {
            if (button.type != null) {
                switch (button.type) {
                    case web_url:
                        isValid &= new ButtonMessageTitleValidator(button.title).validate();
                    case account_link:
                        isValid &= new ButtonMessageUrlValidator(button.data.get(ButtonField.url)).validate();
                        break;
                    case postback:
                    case phone_number:
                        isValid &= new ButtonMessageTitleValidator(button.title).validate();
                        isValid &= new ButtonMessagePayloadValidator(button.data.get(ButtonField.payload)).validate();
                        break;
                    default:
                        logger.error("button type is invalid");
                        isValid = false;
                }
            } else {
                logger.error("button type is null");
                isValid = false;
            }
        }
        return isValid;
    }

}
