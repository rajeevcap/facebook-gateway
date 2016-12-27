package com.capillary.social.validator;

import static com.capillary.social.services.impl.FacebookConstants.MESSAGE_TEXT_LIMIT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuickReplyMessageTextValidator extends MessageFieldValidator {

    private static Logger logger = LoggerFactory.getLogger(QuickReplyMessageTextValidator.class);

    public QuickReplyMessageTextValidator(String field) {
        super(field, MESSAGE_TEXT_LIMIT);
    }

    public boolean validate() {
        return nullAndEmptyCheck() && lengthCheck();
    }

    private boolean nullAndEmptyCheck() {
        boolean textNotNullOrEmpty = validateNotNullOrEmpty();
        if (!textNotNullOrEmpty) {
            logger.debug("quick reply message text is null or empty");
        }
        return textNotNullOrEmpty;
    }

    private boolean lengthCheck() {
        boolean textLengthValid = validateLength();
        if (!textLengthValid) {
            logger.debug("quick reply message text length is invalid");
        }
        return textLengthValid;
    }
}
