package com.capillary.social.services.impl;

import static com.capillary.social.FacebookEntityGenerator.generateQuickReplyTextMessage;
import junit.framework.Assert;

import org.junit.Test;

import com.capillary.social.FacebookMessageStub;
import com.capillary.social.QuickReplyMessage;

public class FacebookQuickReplyMessageTest extends FacebookMessageStub {

    @Test
    public void shouldBeInvalidWhenMessageTextIsNull() {
        QuickReplyMessage quickReplyMessage = generateQuickReplyTextMessage();
        quickReplyMessage.text = null;
        Assert.assertEquals(false, getValidation(new FacebookQuickReplyMessageStub(quickReplyMessage)));
    }

    @Test
    public void shouldBeInvalidWhenMessageTextIsEmpty() {
        QuickReplyMessage quickReplyMessage = generateQuickReplyTextMessage();
        quickReplyMessage.text = "";
        Assert.assertEquals(false, getValidation(new FacebookQuickReplyMessageStub(quickReplyMessage)));
    }

    @Test
    public void shouldBeInvalidWhenMessageQuickReplyListIsEmpty() {
        QuickReplyMessage quickReplyMessage = generateQuickReplyTextMessage();
        quickReplyMessage.quickReplyList.clear();
        Assert.assertEquals(false, getValidation(new FacebookQuickReplyMessageStub(quickReplyMessage)));
    }

    @Test
    public void shouldBeInvalidWhenQuickReplyContentIsInvalid() {
        QuickReplyMessage quickReplyMessage = generateQuickReplyTextMessage();
        quickReplyMessage.quickReplyList.get(0).title = null;
        Assert.assertEquals(false, getValidation(new FacebookQuickReplyMessageStub(quickReplyMessage)));
    }

}
