package com.capillary.social.validator;

import static com.capillary.social.services.impl.FacebookConstants.URL_LENGTH_LIMIT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ButtonMessageUrlValidator extends MessageFieldValidator {

    private static Logger logger = LoggerFactory.getLogger(ButtonMessageUrlValidator.class);

    public ButtonMessageUrlValidator(String url) {
        super(url, URL_LENGTH_LIMIT);
    }

    public boolean validate() {
        return nullAndEmptyCheck() && lengthCheck();
    }

    private boolean nullAndEmptyCheck() {
        boolean urlValid = validateNotNullOrEmpty();
        if (!urlValid) {
            logger.debug("button message url is null or empty");
        }
        return urlValid;
    }

    private boolean lengthCheck() {
        boolean urlLengthValid = validateLength();
        if (!urlLengthValid) {
            logger.debug("button message url length is not valid");
        }
        return urlLengthValid;
    }

}
