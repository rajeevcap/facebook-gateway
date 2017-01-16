package com.capillary.social.services.impl;

import junit.framework.Assert;

import org.apache.thrift.TException;
import org.junit.Ignore;
import org.junit.Test;

import com.capillary.social.FacebookClient;
import com.capillary.social.FacebookEntityGenerator;
import com.capillary.social.FacebookException;
import com.capillary.social.FacebookMessageStub;
import com.capillary.social.ListMessage;

public class FacebookListMessageValidatorTest extends FacebookMessageStub {

    @Ignore
    @Test
    public void shouldNotBeValid() {

    }

    @Test
    public void shouldBeValid() throws FacebookException, TException {
        ListMessage listMessage = FacebookEntityGenerator.generateListMessage();
        System.out.println("listmessage : " + listMessage);
        FacebookClient
                .getFacebookServiceClient()
                .sendListMessage("1307450979317568", listMessage, "127834024337613", 0)
                .toString()
                .equals("{}");
        Assert.assertEquals(false, new FacebookListMessageStub(listMessage).send("", "", 100).toString().equals("{}"));
    }
}
