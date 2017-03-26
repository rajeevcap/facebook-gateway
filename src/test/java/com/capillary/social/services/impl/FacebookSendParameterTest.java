package com.capillary.social.services.impl;

import static com.capillary.social.FacebookEntityGenerator.generateTextMessage;
import junit.framework.Assert;

import org.apache.thrift.TException;
import org.junit.Test;

import com.capillary.social.FacebookException;
import com.capillary.social.FacebookMessageStub;
import com.capillary.social.MessageType;

public class FacebookSendParameterTest extends FacebookMessageStub {

    @Test
    public void shouldBeInvalidWhenUserIdIsNull() throws FacebookException, TException {
        Assert.assertEquals(false, getValidation(new FacebookTextMessageStub(generateTextMessage()), MessageType.textMessage, null, "pageId", 100));
    }
    
    @Test
    public void shouldBeInvalidWhenUserIdIsEmpty() throws FacebookException, TException {
        Assert.assertEquals(false, getValidation(new FacebookTextMessageStub(generateTextMessage()), MessageType.textMessage, "", "pageId", 100));
    }
    
    @Test
    public void shouldBeInvalidWhenPageIdIsNull() throws FacebookException, TException {
        Assert.assertEquals(false, getValidation(new FacebookTextMessageStub(generateTextMessage()), MessageType.textMessage, "recipientId", null, 100));
    }
    
    @Test
    public void shouldBeInvalidWhenPageIdIsEmpty() throws FacebookException, TException {
        Assert.assertEquals(false, getValidation(new FacebookTextMessageStub(generateTextMessage()), MessageType.textMessage, "recipientId", "", 100));
    }
    
    @Test
    public void shouldBeInvalidWhenOrgIdIsNegative() throws FacebookException, TException {
        Assert.assertEquals(false, getValidation(new FacebookTextMessageStub(generateTextMessage()), MessageType.textMessage, "recipientId", "pageId", -1));
    }
    
    @Test
    public void shouldBeValidWhenSendParamsAreValid() throws FacebookException, TException {
        Assert.assertEquals(true, getValidation(new FacebookTextMessageStub(generateTextMessage()), MessageType.textMessage, "recipientId", "pageId", 100));
    }
    
}
