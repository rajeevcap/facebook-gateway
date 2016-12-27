package com.capillary.social.services.impl;

import junit.framework.Assert;

import org.apache.thrift.TException;
import org.junit.Ignore;
import org.junit.Test;

import com.capillary.social.FacebookClient;
import com.capillary.social.FacebookEntityGenerator;
import com.capillary.social.FacebookException;
import com.capillary.social.FacebookMessageStub;
import com.capillary.social.TextMessage;

public class FacebookTextMessageTest extends FacebookMessageStub {

    @Test
    public void shouldBeInvalidWhenTextIsNotPresent() throws FacebookException, TException {
        TextMessage textMessage = FacebookEntityGenerator.generateTextMessage();
        textMessage.text = null;
        Assert.assertEquals(false, new FacebookTextMessageStub(textMessage).send("", "", 100));
    }

    @Ignore("test by actually sending message")
    @Test
    public void shouldBeValidWhenMessageIsActuallySent() throws FacebookException, TException {
        TextMessage textMessage = FacebookEntityGenerator.generateTextMessage();
        Assert.assertEquals(
                FacebookClient.getFacebookServiceClient().sendTextMessage("1307450979317568", textMessage,
                        "127834024337613", 0), true);
    }

}
