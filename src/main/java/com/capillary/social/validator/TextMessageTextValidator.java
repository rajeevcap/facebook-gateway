package com.capillary.social.validator;

import static com.capillary.social.services.impl.FacebookConstants.MESSAGE_TEXT_LIMIT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextMessageTextValidator extends MessageFieldValidator {

    private static Logger logger = LoggerFactory.getLogger(TextMessageTextValidator.class);

    public TextMessageTextValidator(String text) {
        super(text, MESSAGE_TEXT_LIMIT);
    }

    public boolean validate() {
        return nullAndEmptyCheck() && lengthCheck();
    }

    private boolean nullAndEmptyCheck() {
        boolean textNotNullOrEmpty = validateNotNullOrEmpty();
        if (!textNotNullOrEmpty) {
            logger.debug("message text is null or empty");
        }
        return textNotNullOrEmpty;
    }

    private boolean lengthCheck() {
        boolean textLengthValid = validateLength();
        if (!textLengthValid) {
            logger.debug("message text exceeds the limit");
        }
        return textLengthValid;
    }

}
