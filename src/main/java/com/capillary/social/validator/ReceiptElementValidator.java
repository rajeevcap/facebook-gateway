package com.capillary.social.validator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capillary.social.ReceiptElement;
import com.google.common.base.Strings;

public class ReceiptElementValidator {

    private static Logger logger = LoggerFactory.getLogger(ReceiptElementValidator.class);

    private List<ReceiptElement> receiptElementList = new ArrayList<ReceiptElement>();

    public ReceiptElementValidator(List<ReceiptElement> receiptElementList) {
        this.receiptElementList = receiptElementList;
    }

    public boolean validate() {
        boolean isValid = receiptElementList.size() <= 100;
        if (!isValid) {
            logger.debug("receipt element list size exceeds the limit");
            return false;
        }
        for (ReceiptElement receiptElement : receiptElementList) {
            if (!validate(receiptElement)) {
                logger.debug("invalid receipt element : " + receiptElement);
                isValid = false;
            }
        }
        return isValid;
    }

    private boolean validate(ReceiptElement receiptElement) {
        return !Strings.isNullOrEmpty(receiptElement.title) && receiptElement.price >= 0;
    }

}
