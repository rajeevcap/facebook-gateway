package com.capillary.social.validator;

import java.io.UnsupportedEncodingException;

import com.google.common.base.Strings;

public class MessageFieldValidator {

    private final String field;

    private final int fieldLengthLimit;

    public MessageFieldValidator(String field, int fieldLengthLimit) {
        this.field = field;
        this.fieldLengthLimit = fieldLengthLimit;
    }

    public boolean validateNotNullOrEmpty() {
        return !Strings.isNullOrEmpty(field);
    }

    public boolean validateLength() {
        return field.length() <= fieldLengthLimit;
    }

    public boolean validateEncoding() {
        try {
            @SuppressWarnings("unused")
            byte[] bytes = field.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        return true;
    }

}
