package com.capillary.social.services.impl;

import junit.framework.Assert;

import org.apache.thrift.TException;
import org.junit.Ignore;
import org.junit.Test;

import com.capillary.social.FacebookClient;
import com.capillary.social.FacebookEntityGenerator;
import com.capillary.social.FacebookException;
import com.capillary.social.FacebookMessageStub;
import com.capillary.social.QuickReplyMessage;

public class FacebookQuickReplyMessageTest extends FacebookMessageStub {

    @Test
    public void shouldBeInvalidWhenMessageTextIsNull() {
        QuickReplyMessage quickReplyMessage = FacebookEntityGenerator.generateQuickReplyTextMessage();
        quickReplyMessage.text = null;
        Assert.assertEquals(false, new FacebookQuickReplyMessageStub(quickReplyMessage).send("", "", 100));
    }

    @Test
    public void shouldBeInvalidWhenMessageTextIsEmpty() {
        QuickReplyMessage quickReplyMessage = FacebookEntityGenerator.generateQuickReplyTextMessage();
        quickReplyMessage.text = "";
        Assert.assertEquals(false, new FacebookQuickReplyMessageStub(quickReplyMessage).send("", "", 100));
    }

    @Test
    public void shouldBeInvalidWhenMessageQuickReplyListIsEmpty() {
        QuickReplyMessage quickReplyMessage = FacebookEntityGenerator.generateQuickReplyTextMessage();
        quickReplyMessage.quickReplyList.clear();
        Assert.assertEquals(false, new FacebookQuickReplyMessageStub(quickReplyMessage).send("", "", 100));
    }

    @Test
    public void shouldBeInvalidWhenQuickReplyContentIsInvalid() {
        QuickReplyMessage quickReplyMessage = FacebookEntityGenerator.generateQuickReplyTextMessage();
        quickReplyMessage.quickReplyList.get(0).title = null;
        Assert.assertEquals(false, new FacebookQuickReplyMessageStub(quickReplyMessage).send("", "", 100));
    }

    @Ignore("test by actually sending the message")
    @Test
    public void shouldBeValidWhenQuickReplyTextMessageIsSentSuccessfully() throws FacebookException, TException {
        QuickReplyMessage quickReplyMessage = FacebookEntityGenerator.generateQuickReplyTextMessage();
        Assert.assertEquals(
                true,
                FacebookClient.getFacebookServiceClient().sendQuickReplyMessage("1307450979317568", quickReplyMessage,
                        "127834024337613", 0));
    }

    @Ignore("test by actually sending the message")
    @Test
    public void shouldBevalidWhenQuickReplyLocationMessageIsSentSuccessfully() throws FacebookException, TException {
        QuickReplyMessage quickReplyMessage = FacebookEntityGenerator.generateQuickReplyLocationMessage();
        Assert.assertEquals(
                true,
                FacebookClient.getFacebookServiceClient().sendQuickReplyMessage("1307450979317568", quickReplyMessage,
                        "127834024337613", 100));
    }

}
