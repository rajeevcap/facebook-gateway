package com.capillary.social.validator;

import junit.framework.Assert;

import org.junit.Test;

public class ButtonMessageTitleValidatorTest {

    @Test
    public void buttonMessageTitleNotNullValidator() {
        String nullTitle = null;
        Assert.assertEquals(false, new ButtonMessageTitleValidator(nullTitle).validate());
    }

    @Test
    public void buttonMessageTitleNotEmptyValidator() {
        String emptyTitle = "";
        Assert.assertEquals(false, new ButtonMessageTitleValidator(emptyTitle).validate());
    }

    @Test
    public void buttonMessageNormalLengthTitleValidator() {
        String normalTitle = "ababababababababab";
        Assert.assertEquals(true, new ButtonMessageTitleValidator(normalTitle).validate());
    }

    @Test
    public void buttonMessageLargeLengthTitleValidator() {
        String largeTitle = "abababababababababababababababababab";
        Assert.assertEquals(false, new ButtonMessageTitleValidator(largeTitle).validate());
    }

}
