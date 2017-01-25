package com.capillary.social.validator;

import static com.capillary.social.services.impl.FacebookConstants.BUTTON_MESSAGE_BUTTON_LIST_MAX_SIZE;
import static com.capillary.social.services.impl.FacebookConstants.BUTTON_MESSAGE_BUTTON_LIST_MIN_SIZE;
import static com.capillary.social.services.impl.FacebookConstants.GENERIC_MESSAGE_BUTTON_LIST_MAX_SIZE;
import static com.capillary.social.services.impl.FacebookConstants.GENERIC_MESSAGE_BUTTON_LIST_MIN_SIZE;
import static com.capillary.social.services.impl.FacebookConstants.LIST_MESSAGE_BUTTON_LIST_MAX_SIZE;
import static com.capillary.social.services.impl.FacebookConstants.LIST_MESSAGE_BUTTON_LIST_MIN_SIZE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capillary.social.MessageType;

public class ButtonListCountValidator {

    private static Logger logger = LoggerFactory.getLogger(ButtonListCountValidator.class);

    private int buttonListSize;

    private MessageType messageType;

    public ButtonListCountValidator(int buttonListSize, MessageType messageType) {
        this.buttonListSize = buttonListSize;
        this.messageType = messageType;
    }

    public boolean validate() {
        boolean isValid = true;
        switch (messageType) {
            case genericMessage:
                isValid &= buttonListSize >= GENERIC_MESSAGE_BUTTON_LIST_MIN_SIZE
                           && buttonListSize <= GENERIC_MESSAGE_BUTTON_LIST_MAX_SIZE;
                break;
            case listMessage:
                isValid &= buttonListSize >= LIST_MESSAGE_BUTTON_LIST_MIN_SIZE
                           && buttonListSize <= LIST_MESSAGE_BUTTON_LIST_MAX_SIZE;
                break;
            case buttonMessage:
                isValid &= buttonListSize >= BUTTON_MESSAGE_BUTTON_LIST_MIN_SIZE
                           && buttonListSize <= BUTTON_MESSAGE_BUTTON_LIST_MAX_SIZE;
                break;
            default:
                isValid = false;
                logger.error("invalid message type");
                break;
        }
        return isValid;
    }

}
