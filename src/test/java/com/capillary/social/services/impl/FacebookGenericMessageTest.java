package com.capillary.social.services.impl;

import static com.capillary.social.FacebookEntityGenerator.generateGenericMessage;
import junit.framework.Assert;

import org.apache.thrift.TException;
import org.junit.Ignore;
import org.junit.Test;

import com.capillary.social.FacebookClient;
import com.capillary.social.FacebookException;
import com.capillary.social.FacebookMessageStub;
import com.capillary.social.GenericMessage;

public class FacebookGenericMessageTest extends FacebookMessageStub {

    @Test
    public void shouldBeInvalidWhenNoElementIsPresent() throws FacebookException, TException {
        GenericMessage genericMessage = generateGenericMessage();
        genericMessage.elementList = null;
        Assert.assertEquals(true,
                new FacebookGenericMessageStub(genericMessage).send("", "", 100).toString().equals("{}"));
    }

    @Test
    public void shouldBeInvalidWhenInvalidElementIsPresent() throws FacebookException, TException {
        GenericMessage genericMessage = generateGenericMessage();
        genericMessage.elementList.get(0).title = null;
        Assert.assertEquals(true,
                new FacebookGenericMessageStub(genericMessage).send("", "", 100).toString().equals("{}"));
    }

    @Test
    public void shouldBeValidWhenElementsAreValidAndResponseIsOk() throws FacebookException, TException {
        GenericMessage genericMessage = generateGenericMessage();
        Assert.assertEquals(false,
                new FacebookGenericMessageStub(genericMessage).send("", "", 100).toString().equals("{}"));
    }

    @Ignore("test by actually sending message")
    @Test
    public void shouldBeValidWhenMessageIsSentSuccessfully() throws FacebookException, TException {
        GenericMessage genericMessage = generateGenericMessage();
        Assert.assertEquals(
                false,
                FacebookClient
                        .getFacebookServiceClient()
                        .sendGenericMessage("1307450979317568", genericMessage, "127834024337613", 0)
                        .toString()
                        .equals("{}"));
    }

}
