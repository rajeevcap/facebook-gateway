package com.capillary.social.validator;

import junit.framework.Assert;

import org.apache.thrift.TException;
import org.junit.Ignore;
import org.junit.Test;

import com.capillary.social.FacebookClient;
import com.capillary.social.FacebookEntityGenerator;
import com.capillary.social.FacebookException;
import com.capillary.social.FacebookMessageStub;
import com.capillary.social.ReceiptMessage;

public class ReceiptMessageValidatorTest extends FacebookMessageStub {

    @Test
    public void shouldBeInvalidWhenNoRecipientIsProvided() throws FacebookException, TException {
        ReceiptMessage receiptMessage = FacebookEntityGenerator.generateReceiptMessage();
        receiptMessage.recipientName = null;
        Assert.assertEquals(false, new FacebookReceiptMessageStub(receiptMessage).send("", "", 100));
    }
    
    @Test
    public void shouldBeInvalidWhenOrderNumberIsNotPresent() throws FacebookException, TException {
        ReceiptMessage receiptMessage = FacebookEntityGenerator.generateReceiptMessage();
        receiptMessage.orderNumber = null;
        Assert.assertEquals(false, new FacebookReceiptMessageStub(receiptMessage).send("", "", 100));
    }
    
    @Ignore("un-ignore when unique order number is implemented")
    @Test
    public void shouldBeInvalidWhenOrderIsNotUnique() throws FacebookException, TException {
        ReceiptMessage receiptMessage = FacebookEntityGenerator.generateReceiptMessage();
        // set already present receipt number
        Assert.assertEquals(false, new FacebookReceiptMessageStub(receiptMessage).send("", "", 100));
    }
    
    @Test
    public void shouldBeInvalidWhenCurrencyIsNotPresent() throws FacebookException, TException {
        ReceiptMessage receiptMessage = FacebookEntityGenerator.generateReceiptMessage();
        receiptMessage.currency = null;
        Assert.assertEquals(false, new FacebookReceiptMessageStub(receiptMessage).send("", "", 100));
    }
    
    @Test
    public void shouldBeInvalidWhenPaymentMethodIsNotPresent() throws FacebookException, TException {
        ReceiptMessage receiptMessage = FacebookEntityGenerator.generateReceiptMessage();
        receiptMessage.paymentMethod = null;
        Assert.assertEquals(false, new FacebookReceiptMessageStub(receiptMessage).send("", "", 100));
    }
    
    @Test
    public void shouldBeInvalidWhenSummaryIsNotPresent() throws FacebookException, TException {
        ReceiptMessage receiptMessage = FacebookEntityGenerator.generateReceiptMessage();
        receiptMessage.summary = null;
        Assert.assertEquals(false, new FacebookReceiptMessageStub(receiptMessage).send("", "", 100));
    }

    @Ignore
    @Test
    public void shouldBeValidWhenMessageIsSent() throws FacebookException, TException {
        ReceiptMessage receiptMessage = FacebookEntityGenerator.generateReceiptMessage();
        Assert.assertEquals(true, FacebookClient.getFacebookServiceClient().sendReceiptMessage("1307450979317568", receiptMessage,
                "127834024337613", 0));
    }
    
}
