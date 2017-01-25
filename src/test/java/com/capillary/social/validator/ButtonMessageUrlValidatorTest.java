package com.capillary.social.validator;

import junit.framework.Assert;

import org.junit.Test;

public class ButtonMessageUrlValidatorTest {

    @Test
    public void buttonMessageUrlNotNullValidator() {
        String nullUrl = null;
        Assert.assertEquals(false, new ButtonMessageUrlValidator(nullUrl).validate());
    }

    @Test
    public void buttonMessageUrlNotEmptyValidator() {
        String emptyUrl = "";
        Assert.assertEquals(false, new ButtonMessageUrlValidator(emptyUrl).validate());
    }

    @Test
    public void buttonMessageNormalUrlValidator() {
        String normalUrl = "normalUrl";
        Assert.assertEquals(true, new ButtonMessageUrlValidator(normalUrl).validate());
    }

}
