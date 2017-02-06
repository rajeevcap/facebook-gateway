package com.capillary.social.validator;

import com.capillary.social.ReceiptMessage;
import com.google.common.base.Strings;

public class ReceiptMessageValidator {

    private ReceiptMessage receiptMessage;

    public ReceiptMessageValidator(ReceiptMessage receiptMessage) {
        this.receiptMessage = receiptMessage;
    }

    private boolean validateNotNullOrEmpty(String field) {
        return !Strings.isNullOrEmpty(field);
    }

    private boolean isNotNull(Object obj) {
        return obj != null;
    }

    public boolean validate() {
        boolean isValid = true;
        isValid &= validateNotNullOrEmpty(receiptMessage.recipientName);
        isValid &= new ReceiptOrderNumberValidator(receiptMessage.orderNumber).validate();
        isValid &= new CountryCodeValidator(receiptMessage.currency).validate();
        isValid &= validateNotNullOrEmpty(receiptMessage.paymentMethod);
        if (isNotNull(receiptMessage.receiptElementList))
            isValid &= new ReceiptElementValidator(receiptMessage.receiptElementList).validate();
        if (isNotNull(receiptMessage.address))
            isValid &= new AddressValidator(receiptMessage.address).validate();
        if (isNotNull(receiptMessage.summary))
            isValid &= new SummaryValidator(receiptMessage.summary).validate();
        else
            isValid = false;
        return isValid;
    }

}
