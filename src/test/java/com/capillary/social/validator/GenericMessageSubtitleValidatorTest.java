package com.capillary.social.validator;

import junit.framework.Assert;

import org.junit.Test;

public class GenericMessageSubtitleValidatorTest {

    @Test
    public void genericMessageNormalLengthSubtitleValidator() {
        String normalSubtitle = "ababababababababab";
        Assert.assertEquals(true, new GenericMessageSubtitleValidator(normalSubtitle).validate());
    }

    @Test
    public void genericMessageLargeLengthSubtitleValidator() {
        String largeSubtitle = "ababababababababababababababababababababababababababababababababababababababababababababab";
        Assert.assertEquals(false, new GenericMessageSubtitleValidator(largeSubtitle).validate());
    }
}
