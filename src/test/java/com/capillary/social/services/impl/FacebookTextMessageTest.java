package com.capillary.social.services.impl;

import static com.capillary.social.FacebookEntityGenerator.generateTextMessage;
import junit.framework.Assert;

import org.apache.thrift.TException;
import org.junit.Test;

import com.capillary.social.FacebookException;
import com.capillary.social.FacebookMessageStub;
import com.capillary.social.TextMessage;

public class FacebookTextMessageTest extends FacebookMessageStub {

    @Test
    public void shouldBeInvalidWhenTextIsNotPresent() throws FacebookException, TException {
        TextMessage textMessage = generateTextMessage();
        textMessage.text = null;
        Assert.assertEquals(false, getValidation(new FacebookTextMessageStub(textMessage)));
    }

}
