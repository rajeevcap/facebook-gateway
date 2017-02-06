package com.capillary.social.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class ReceiptOrderNumberValidator {

    private static Logger logger = LoggerFactory.getLogger(ReceiptOrderNumberValidator.class);

    private String orderNumber;

    public ReceiptOrderNumberValidator(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public boolean validate() {
        if (Strings.isNullOrEmpty(orderNumber)) {
            logger.debug("receipt order number empty");
            return false;
        }
        return validateUnique();
    }

    public boolean validateUnique() {
        // to be continued..
        return true;
    }
}
