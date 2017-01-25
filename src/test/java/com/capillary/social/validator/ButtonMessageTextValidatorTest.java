package com.capillary.social.validator;

import junit.framework.Assert;

import org.junit.Test;

public class ButtonMessageTextValidatorTest {

    @Test
    public void buttonMessageTextNotNullValidator() {
        String nullText = null;
        Assert.assertEquals(false, new ButtonMessageTextValidator(nullText).validate());
    }

    @Test
    public void buttonMessageTextNotEmptyValidator() {
        String emptyText = "";
        Assert.assertEquals(false, new ButtonMessageTextValidator(emptyText).validate());
    }

    @Test
    public void buttonMessageNormalLengthTextValidator() {
        String normalText = "ababababababababab";
        Assert.assertEquals(true, new ButtonMessageTextValidator(normalText).validate());
    }

    @Test
    public void buttonMessageLargeLengthTextValidator() {
        String largeText = "abababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababab";
        Assert.assertEquals(false, new ButtonMessageTextValidator(largeText).validate());
    }

}
