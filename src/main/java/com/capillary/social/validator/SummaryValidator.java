package com.capillary.social.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capillary.social.Summary;

public class SummaryValidator {

    private static Logger logger = LoggerFactory.getLogger(SummaryValidator.class);

    private Summary summary;

    public SummaryValidator(Summary summary) {
        this.summary = summary;
    }

    public boolean validate() {
        boolean isValid = summary.totalCost > 0;
        if (!isValid) {
            logger.debug("summary total cost is invalid");
        }
        return isValid;
    }
}
