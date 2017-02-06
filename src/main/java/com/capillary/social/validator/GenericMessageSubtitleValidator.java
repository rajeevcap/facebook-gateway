package com.capillary.social.validator;

import static com.capillary.social.services.impl.FacebookConstants.GENERIC_MESSAGE_SUBTITLE_LIMIT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericMessageSubtitleValidator extends MessageFieldValidator {

    private static Logger logger = LoggerFactory.getLogger(GenericMessageSubtitleValidator.class);

    public GenericMessageSubtitleValidator(String subtitle) {
        super(subtitle, GENERIC_MESSAGE_SUBTITLE_LIMIT);
    }

    public boolean validate() {
        boolean subtitleLengthValid = validateLength();
        if (!subtitleLengthValid) {
            logger.debug("button message subtitle length exceeds the limit");
        }
        return subtitleLengthValid;
    }

}
