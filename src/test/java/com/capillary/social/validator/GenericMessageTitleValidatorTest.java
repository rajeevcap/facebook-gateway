package com.capillary.social.validator;

import junit.framework.Assert;

import org.junit.Test;

public class GenericMessageTitleValidatorTest {

    @Test
    public void genericMessageTitleNotNullValidator() {
        String nullTitle = null;
        Assert.assertEquals(false, new GenericMessageTitleValidator(nullTitle).validate());
    }

    @Test
    public void genericMessageTitleNotEmptyValidator() {
        String emptyTitle = "";
        Assert.assertEquals(false, new GenericMessageTitleValidator(emptyTitle).validate());
    }

    @Test
    public void genericMessageNormalLengthTitleValidator() {
        String normalTitle = "ababababababababab";
        Assert.assertEquals(true, new GenericMessageTitleValidator(normalTitle).validate());
    }

    @Test
    public void genericMessageLargeLengthTitleValidator() {
        String largeTitle = "abababababababababababababababababababababababababababababababababababababababababababababababababababababab";
        Assert.assertEquals(false, new GenericMessageTitleValidator(largeTitle).validate());
    }

}
