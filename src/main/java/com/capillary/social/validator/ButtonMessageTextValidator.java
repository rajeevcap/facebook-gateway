package com.capillary.social.validator;

import static com.capillary.social.services.impl.FacebookConstants.MESSAGE_TEXT_LIMIT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ButtonMessageTextValidator extends MessageFieldValidator {

    private static Logger logger = LoggerFactory.getLogger(ButtonMessageTextValidator.class);

    public ButtonMessageTextValidator(String text) {
        super(text, MESSAGE_TEXT_LIMIT);
    }

    public boolean validate() {
        return nullAndEmptyCheck() && lengthCheck() && utf8Check();
    }

    private boolean nullAndEmptyCheck() {
        boolean textNotNullOrEmpty = validateNotNullOrEmpty();
        if (!textNotNullOrEmpty) {
            logger.debug("button message text is null or empty");
        }
        return textNotNullOrEmpty;
    }

    private boolean lengthCheck() {
        boolean textLengthValid = validateLength();
        if (!textLengthValid) {
            logger.debug("button message text length is invalid");
        }
        return textLengthValid;
    }

    private boolean utf8Check() {
        boolean textEncodingValid = validateEncoding();
        if (!textEncodingValid) {
            logger.debug("button message text encoding is invalid");
            return false;
        }
        return true;
    }

}
