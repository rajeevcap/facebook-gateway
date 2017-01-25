package com.capillary.social.validator;

import static com.capillary.social.services.impl.FacebookConstants.GENERIC_MESSAGE_TITLE_LIMIT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericMessageTitleValidator extends MessageFieldValidator {

    private static Logger logger = LoggerFactory.getLogger(GenericMessageTitleValidator.class);

    public GenericMessageTitleValidator(String title) {
        super(title, GENERIC_MESSAGE_TITLE_LIMIT);
    }

    public boolean validate() {
        return nullAndEmptyCheck() && lengthCheck();
    }

    private boolean nullAndEmptyCheck() {
        boolean titleNotNullOrEmpty = validateNotNullOrEmpty();
        if (!titleNotNullOrEmpty) {
            logger.debug("generic message title is null or empty");
        }
        return titleNotNullOrEmpty;
    }

    private boolean lengthCheck() {
        boolean titleLengthValid = validateLength();
        if (!titleLengthValid) {
            logger.debug("generic message title length exceeds the limit");
        }
        return titleLengthValid;
    }

}
