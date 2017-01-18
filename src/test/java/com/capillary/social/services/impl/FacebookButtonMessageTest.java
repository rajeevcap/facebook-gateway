package com.capillary.social.services.impl;

import static com.capillary.social.FacebookEntityGenerator.generateButtonMessage;
import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.apache.thrift.TException;

import com.capillary.social.ButtonMessage;
import com.capillary.social.FacebookException;
import com.capillary.social.FacebookClient;
import com.capillary.social.FacebookMessageStub;

public class FacebookButtonMessageTest extends FacebookMessageStub {

    @Test
    public void shouldBeInvalidWhenNoButtonIsPresent() {
        ButtonMessage buttonMessage = generateButtonMessage();
        buttonMessage.buttonList.clear();
        Assert.assertEquals(true, new FacebookButtonMessageStub(buttonMessage)
                .send("", "", 100)
                .toString()
                .equals("{}"));
    }

    @Test
    public void shouldBeInvalidWhenMessageContentIsInvalid() {
        ButtonMessage buttonMessage = generateButtonMessage();
        buttonMessage.text = null;
        Assert.assertEquals(true, new FacebookButtonMessageStub(buttonMessage)
                .send("", "", 100)
                .toString()
                .equals("{}"));
    }

    @Test
    public void shouldBeValidWhenMessageContentIsValidAndResponseIsOK() {
        ButtonMessage buttonMessage = generateButtonMessage();
        Assert.assertEquals(false, new FacebookButtonMessageStub(buttonMessage)
                .send("", "", 100)
                .toString()
                .equals("{}"));
    }

    @Ignore("test by actually sending message")
    @Test
    public void shouldBeValidWhenMessageIsSuccessfullySent() throws FacebookException, TException {
        ButtonMessage buttonMessage = generateButtonMessage();
        Assert.assertEquals(
                false,
                FacebookClient
                        .getFacebookServiceClient()
                        .sendButtonMessage("1307450979317568", buttonMessage, "127834024337613", 0)
                        .toString()
                        .equals("{}"));
    }
}
