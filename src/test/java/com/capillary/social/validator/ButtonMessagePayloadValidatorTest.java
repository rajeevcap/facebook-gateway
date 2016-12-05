package com.capillary.social.validator;

import junit.framework.Assert;

import org.junit.Test;

public class ButtonMessagePayloadValidatorTest {

    @Test
    public void buttonMessagePayloadNotNullValidator() {
        String nullPayload = null;
        Assert.assertEquals(false, new ButtonMessagePayloadValidator(nullPayload).validate());
    }

    @Test
    public void buttonMessagePayloadNotEmptyValidator() {
        String emptyPayload = "";
        Assert.assertEquals(false, new ButtonMessagePayloadValidator(emptyPayload).validate());
    }

    @Test
    public void buttonMessageNormalLengthPayloadValidator() {
        String normalPayload = "ababababababababab";
        Assert.assertEquals(true, new ButtonMessagePayloadValidator(normalPayload).validate());
    }

    @Test
    public void buttonMessageLargeLengthPayloadValidator() {
        String largePayload = "abababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababab";
        Assert.assertEquals(false, new ButtonMessagePayloadValidator(largePayload).validate());
    }

}
