package com.capillary.social.validator;

import static com.capillary.social.services.impl.FacebookConstants.QUICK_REPLY_MESSAGE_PAYLOAD_LENGTH_LIMIT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuickReplyMessagePayloadValidator extends MessageFieldValidator {

    private static Logger logger = LoggerFactory.getLogger(QuickReplyMessagePayloadValidator.class);

    public QuickReplyMessagePayloadValidator(String payload) {
        super(payload, QUICK_REPLY_MESSAGE_PAYLOAD_LENGTH_LIMIT);
    }

    public boolean validate() {
        return nullAndEmptyCheck() && lengthCheck();
    }

    private boolean nullAndEmptyCheck() {
        boolean payloadNotNullOrEmpty = validateNotNullOrEmpty();
        if (!payloadNotNullOrEmpty) {
            logger.debug("quick reply message payload is null or empty");
        }
        return payloadNotNullOrEmpty;
    }

    private boolean lengthCheck() {
        boolean payloadLengthValid = validateLength();
        if (!payloadLengthValid) {
            logger.debug("quick reply message payload length exceeds the limit");
        }
        return payloadLengthValid;
    }

}
