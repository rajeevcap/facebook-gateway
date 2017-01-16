package com.capillary.social.validator;

import static com.capillary.social.services.impl.FacebookConstants.GENERIC_MESSAGE_ELEMENT_LIST_MAX_SIZE;
import static com.capillary.social.services.impl.FacebookConstants.GENERIC_MESSAGE_ELEMENT_LIST_MIN_SIZE;
import static com.capillary.social.services.impl.FacebookConstants.LIST_MESSAGE_ELEMENT_LIST_MAX_SIZE;
import static com.capillary.social.services.impl.FacebookConstants.LIST_MESSAGE_ELEMENT_LIST_MIN_SIZE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capillary.social.MessageType;
import com.capillary.social.services.impl.FacebookConstants;

public class ElementListCountValidator {

    private static Logger logger = LoggerFactory.getLogger(ButtonMessageTextValidator.class);

    private int elementListSize;

    private MessageType messageType;

    public ElementListCountValidator(int elementListSize, MessageType messageType) {
        this.elementListSize = elementListSize;
        this.messageType = messageType;
    }

    public boolean validate() {
        boolean isValid = true;
        switch (messageType) {
            case genericMessage:
                isValid &= elementListSize >= GENERIC_MESSAGE_ELEMENT_LIST_MIN_SIZE
                           && elementListSize <= GENERIC_MESSAGE_ELEMENT_LIST_MAX_SIZE;
                break;
            case listMessage:
                isValid &= elementListSize >= LIST_MESSAGE_ELEMENT_LIST_MIN_SIZE
                           && elementListSize <= LIST_MESSAGE_ELEMENT_LIST_MAX_SIZE;
                break;
            default :
                isValid = false;
                logger.error("invalid message type");
                break;
        }
        return isValid;
    }

}
