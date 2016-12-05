package com.capillary.social.validator;

import static com.capillary.social.services.impl.FacebookConstants.MESSAGE_PAYLOAD_LENGTH_LIMIT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ButtonMessagePayloadValidator extends MessageFieldValidator {

    private static Logger logger = LoggerFactory.getLogger(ButtonMessageTextValidator.class);

    public ButtonMessagePayloadValidator(String payload) {
        super(payload, MESSAGE_PAYLOAD_LENGTH_LIMIT);
    }

    public boolean validate() {
        return nullAndEmptyCheck() && lengthCheck();
    }

    private boolean nullAndEmptyCheck() {
        boolean payloadNotNullOrEmpty = validateNotNullOrEmpty();
        if (!payloadNotNullOrEmpty) {
            logger.debug("button message payload is null or empty");
        }
        return payloadNotNullOrEmpty;
    }

    private boolean lengthCheck() {
        boolean payloadLengthValid = validateLength();
        if (!payloadLengthValid) {
            logger.debug("button message payload length exceeds the limit");
        }
        return payloadLengthValid;
    }
}
