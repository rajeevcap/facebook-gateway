package com.capillary.social.services.impl;

import static com.capillary.social.FacebookEntityGenerator.generateTextMessage;
import static com.capillary.social.GatewayResponseType.sent;
import junit.framework.Assert;

import org.apache.thrift.TException;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capillary.social.FacebookClient;
import com.capillary.social.FacebookException;
import com.capillary.social.FacebookMessageStub;
import com.capillary.social.MessageType;
import com.capillary.social.TextMessage;

public class FacebookTextMessageTest extends FacebookMessageStub {

    private static Logger logger = LoggerFactory.getLogger(FacebookTextMessageTest.class);

    @Test
    public void shouldBeInvalidWhenTextIsNotPresent() throws FacebookException, TException {
        TextMessage textMessage = generateTextMessage();
        textMessage.text = null;
        Assert.assertEquals(false, getValidation(new FacebookTextMessageStub(textMessage), MessageType.textMessage));
    }

    @Ignore
    @Test
    public void shouldBeValidWhenMessageIsSuccessfullySent() throws FacebookException, TException {
        TextMessage textMessage = generateTextMessage();
        logger.debug("generated message : " + textMessage);
        Assert.assertEquals(
                true,
                FacebookClient.getFacebookServiceClient().sendTextMessage("1307450979317568", textMessage,
                        "127834024337613", 0, "abcd").gatewayResponseType == sent);
    }

}
