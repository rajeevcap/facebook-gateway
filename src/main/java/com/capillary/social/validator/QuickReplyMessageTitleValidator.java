package com.capillary.social.validator;

import static com.capillary.social.services.impl.FacebookConstants.QUICK_REPLY_MESSAGE_TITLE_LIMIT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuickReplyMessageTitleValidator extends MessageFieldValidator {

    private static Logger logger = LoggerFactory.getLogger(QuickReplyMessageTitleValidator.class);

    public QuickReplyMessageTitleValidator(String field) {
        super(field, QUICK_REPLY_MESSAGE_TITLE_LIMIT);
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
            logger.debug("quick reply message title length exceeds the limit");
        }
        return titleLengthValid;
    }

}
