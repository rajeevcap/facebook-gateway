package com.capillary.social.validator;

import static com.capillary.social.services.impl.FacebookConstants.BUTTON_TITLE_LENGTH_LIMIT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ButtonMessageTitleValidator extends MessageFieldValidator {

    private static Logger logger = LoggerFactory.getLogger(ButtonMessageTitleValidator.class);

    public ButtonMessageTitleValidator(String title) {
        super(title, BUTTON_TITLE_LENGTH_LIMIT);
    }

    public boolean validate() {
        return nullAndEmptyCheck() && lengthCheck();
    }

    private boolean nullAndEmptyCheck() {
        boolean validTitle = validateNotNullOrEmpty();
        if (validTitle) {
            logger.debug("button message title is null or empty");
        }
        return validTitle;
    }

    private boolean lengthCheck() {
        boolean titleLengthValid = validateLength();
        if (!titleLengthValid) {
            logger.debug("button message title length exceeds the limit");
        }
        return titleLengthValid;
    }

}
