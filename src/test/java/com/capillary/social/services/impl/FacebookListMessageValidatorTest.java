package com.capillary.social.services.impl;

import static com.capillary.social.FacebookEntityGenerator.generateButton;
import static com.capillary.social.FacebookEntityGenerator.generateListMessage;
import junit.framework.Assert;

import org.apache.thrift.TException;
import org.junit.Ignore;
import org.junit.Test;

import com.capillary.social.FacebookClient;
import com.capillary.social.FacebookException;
import com.capillary.social.FacebookMessageStub;
import com.capillary.social.ListMessage;
import com.capillary.social.MessageType;

public class FacebookListMessageValidatorTest extends FacebookMessageStub {

    @Ignore
    @Test
    public void shouldBeInvalidWhenElementListSizeIsLessThanMinLimit() { // < 2
        ListMessage listMessage = generateListMessage();
        listMessage.elementList.remove(listMessage.elementList.size() - 1);
        Assert.assertEquals(false, getValidation(new FacebookListMessageStub(listMessage), MessageType.listMessage));
    }
    @Ignore
    @Test
    public void shouldBeInvalidWhenElementListSizeIsMoreThanMaxLimit() { // > 4
        ListMessage listMessage = generateListMessage();
        for (int i = 0; i < 3; i++)
            listMessage.elementList.add(listMessage.elementList.get(0));
        Assert.assertEquals(false, getValidation(new FacebookListMessageStub(listMessage), MessageType.listMessage));
    }
    @Ignore
    @Test
    public void shouldBeInvalidWhenAnyElementHasMoreThanOneButton() {
        ListMessage listMessage = generateListMessage();
        listMessage.elementList.get(0).buttonList.add(generateButton());
        Assert.assertEquals(false, getValidation(new FacebookListMessageStub(listMessage), MessageType.listMessage));
    }
    @Ignore
    @Test
    public void shouldBeInvalidWhenPayloadHasMoreThanOneButton() {
        ListMessage listMessage = generateListMessage();
        listMessage.buttonList.add(generateButton());
        Assert.assertEquals(false, getValidation(new FacebookListMessageStub(listMessage), MessageType.listMessage));
    }
    @Ignore
    @Test
    public void shouldBeInvalidWhenTopElementStyleIsLargeAndImageUrlForFirstElementIsNull() {
        ListMessage listMessage = generateListMessage();
        listMessage.elementList.get(0).imageUrl = null;
        Assert.assertEquals(false, getValidation(new FacebookListMessageStub(listMessage), MessageType.listMessage));
    }

    //@Ignore
    @Test
    public void shouldBeValidWhenListMessageIsSentSuccessfully() throws FacebookException, TException {
        ListMessage listMessage = generateListMessage();
        Assert.assertEquals(false,
                FacebookClient
                        .getFacebookServiceClient()
                        .sendListMessage("", listMessage, "", 0, "requestId")
                        .toString()
                        .equals("{}"));
    }
}
