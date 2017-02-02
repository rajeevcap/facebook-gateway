package com.capillary.social.services.impl;

import static com.capillary.social.FacebookEntityGenerator.generateButtonMessage;
import static com.capillary.social.GatewayResponseType.sent;
import junit.framework.Assert;

import org.apache.thrift.TException;
import org.junit.Ignore;
import org.junit.Test;

import com.capillary.social.ButtonMessage;
import com.capillary.social.FacebookClient;
import com.capillary.social.FacebookException;
import com.capillary.social.FacebookMessageStub;

public class FacebookButtonMessageTest extends FacebookMessageStub {

    @Ignore
    @Test
    public void shouldBeInvalidWhenNoButtonIsPresent() {
        ButtonMessage buttonMessage = generateButtonMessage();
        buttonMessage.buttonList.clear();
        Assert.assertEquals(false, getValidation(new FacebookButtonMessageStub(buttonMessage)));
    }

    @Ignore
    @Test
    public void shouldBeInvalidWhenMessageContentIsInvalid() {
        ButtonMessage buttonMessage = generateButtonMessage();
        buttonMessage.text = null;
        Assert.assertEquals(false, getValidation(new FacebookButtonMessageStub(buttonMessage)));
    }

    @Ignore
    @Test
    public void shouldBeValidWhenMessageContentIsValidAndResponseIsOK() {
        ButtonMessage buttonMessage = generateButtonMessage();
        Assert.assertEquals(true, getValidation(new FacebookButtonMessageStub(buttonMessage)));
    }

    @Ignore("test by actually sending message")
    @Test
    public void shouldBeValidWhenMessageIsSuccessfullySent() throws FacebookException, TException {
        ButtonMessage buttonMessage = generateButtonMessage();
        FacebookClient.getFacebookServiceClient().sendButtonMessage("1307450979317568", buttonMessage,
                "127834024337613", 0, "gvbj");
        Assert.assertEquals(
                true,
                FacebookClient.getFacebookServiceClient().sendButtonMessage("1307450979317568", buttonMessage,
                        "127834024337613", 0, "gvbj").gatewayResponseType == sent);
    }
}
