package com.capillary.social.validator;

import static com.capillary.social.FacebookEntityGenerator.generateQuickReplyTextMessage;
import static com.capillary.social.services.impl.FacebookConstants.QUICK_REPLY_LIST_SIZE_LIMIT;
import static com.capillary.social.services.impl.FacebookConstants.QUICK_REPLY_MESSAGE_PAYLOAD_LENGTH_LIMIT;
import static com.capillary.social.services.impl.FacebookConstants.QUICK_REPLY_MESSAGE_TITLE_LIMIT;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.capillary.social.FacebookEntityGenerator;
import com.capillary.social.FacebookMessageStub;
import com.capillary.social.MessageType;
import com.capillary.social.QuickReply;
import com.capillary.social.QuickReplyMessage;

public class QuickReplyListValidatorTest extends FacebookMessageStub {

    @Test
    public void shouldBeInvalidWhenQuickReplyListSizeExceedsLimit() {
        QuickReplyMessage quickReplyMessage = generateQuickReplyTextMessage();
        List<QuickReply> quickReplyList = new ArrayList<QuickReply>();
        QuickReply quickReply = new QuickReply();
        for (int i = 0; i < QUICK_REPLY_LIST_SIZE_LIMIT + 1; i++) {
            quickReplyList.add(quickReply);
        }
        quickReplyMessage.quickReplyList = quickReplyList;
        Assert.assertEquals(false,
                getValidation(new FacebookQuickReplyMessageStub(quickReplyMessage), MessageType.quickReplyMessage));
    }

    @Test
    public void shouldBeInvalidWhenQuickReplyListSizeIsEmpty() {
        QuickReplyMessage quickReplyMessage = generateQuickReplyTextMessage();
        quickReplyMessage.quickReplyList.clear();
        Assert.assertEquals(false,
                getValidation(new FacebookQuickReplyMessageStub(quickReplyMessage), MessageType.quickReplyMessage));
    }

    @Test
    public void shouldBeInvalidWhenQuickReplyListSizeIsNull() {
        QuickReplyMessage quickReplyMessage = new QuickReplyMessage();
        quickReplyMessage.quickReplyList = null;
        Assert.assertEquals(false,
                getValidation(new FacebookQuickReplyMessageStub(quickReplyMessage), MessageType.quickReplyMessage));
    }

    @Test
    public void shouldBeInvalidWhenQuickReplyContentTypeIsNull() {
        QuickReplyMessage quickReplyMessage = generateQuickReplyTextMessage();
        quickReplyMessage.quickReplyList.get(0).contentType = null;
        Assert.assertEquals(false,
                getValidation(new FacebookQuickReplyMessageStub(quickReplyMessage), MessageType.quickReplyMessage));
    }

    @Test
    public void shouldBeInvalidWhenQuickReplyMessageTitleIsNull() {
        QuickReplyMessage quickReplyMessage = generateQuickReplyTextMessage();
        quickReplyMessage.quickReplyList.get(0).title = null;
        Assert.assertEquals(false,
                getValidation(new FacebookQuickReplyMessageStub(quickReplyMessage), MessageType.quickReplyMessage));
    }

    @Test
    public void shouldBeInvalidWhenQuickReplyMessageTitleExceedsLimit() {
        QuickReplyMessage quickReplyMessage = generateQuickReplyTextMessage();
        quickReplyMessage.quickReplyList.get(0).title = FacebookEntityGenerator
                .generateRandomString(QUICK_REPLY_MESSAGE_TITLE_LIMIT + 1);
        Assert.assertEquals(false,
                getValidation(new FacebookQuickReplyMessageStub(quickReplyMessage), MessageType.quickReplyMessage));
    }

    @Test
    public void shouldBeInvalidWhenQuickReplyMessagePayloadIsNull() {
        QuickReplyMessage quickReplyMessage = generateQuickReplyTextMessage();
        quickReplyMessage.quickReplyList.get(0).payload = null;
        Assert.assertEquals(false,
                getValidation(new FacebookQuickReplyMessageStub(quickReplyMessage), MessageType.quickReplyMessage));
    }

    @Test
    public void shouldBeInvalidWhenQuickReplyMessagePayloadSizeExceedsLimit() {
        QuickReplyMessage quickReplyMessage = generateQuickReplyTextMessage();
        quickReplyMessage.quickReplyList.get(0).payload = FacebookEntityGenerator
                .generateRandomString(QUICK_REPLY_MESSAGE_PAYLOAD_LENGTH_LIMIT + 1);
        Assert.assertEquals(false,
                getValidation(new FacebookQuickReplyMessageStub(quickReplyMessage), MessageType.quickReplyMessage));
    }

}
