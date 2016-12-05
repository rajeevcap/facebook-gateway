package com.capillary.social.services.impl;

import junit.framework.Assert;

import org.apache.thrift.TException;
import org.junit.Ignore;
import org.junit.Test;

import com.capillary.social.FacebookClient;
import com.capillary.social.FacebookEntityGenerator;
import com.capillary.social.FacebookException;
import com.capillary.social.FacebookMessageStub;
import com.capillary.social.GenericMessage;

public class FacebookGenericMessageTest extends FacebookMessageStub {

    @Test
    public void shouldBeInvalidWhenNoElementIsPresent() throws FacebookException, TException {
        GenericMessage genericMessage = FacebookEntityGenerator.generateGenericMessage();
        genericMessage.elementList = null;
        Assert.assertEquals(false, new FacebookGenericMessageStub(genericMessage).send("", "", 100));
    }

    @Test
    public void shouldBeInvalidWhenInvalidElementIsPresent() throws FacebookException, TException {
        GenericMessage genericMessage = FacebookEntityGenerator.generateGenericMessage();
        genericMessage.elementList.get(0).title = null;
        Assert.assertEquals(false, new FacebookGenericMessageStub(genericMessage).send("", "", 100));
    }

    @Test
    public void shouldBeValidWhenElementsAreValidAndResponseIsOk() throws FacebookException, TException {
        GenericMessage genericMessage = FacebookEntityGenerator.generateGenericMessage();
        Assert.assertEquals(true, new FacebookGenericMessageStub(genericMessage).send("", "", 100));
    }

    @Ignore("test by actually sending message")
    @Test
    public void shouldBeValidWhenMessageIsSentSuccessfully() throws FacebookException, TException {
        GenericMessage genericMessage = FacebookEntityGenerator.generateGenericMessage();
        Assert.assertEquals(
                false,
                FacebookClient.getFacebookServiceClient().sendGenericMessage("1307450979317568", genericMessage,
                        "127834024337613", 0));
    }

}
