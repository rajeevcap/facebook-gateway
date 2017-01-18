package com.capillary.social.validator;

import static com.capillary.social.FacebookEntityGenerator.generateReceiptMessage;
import junit.framework.Assert;

import org.apache.thrift.TException;
import org.junit.Ignore;
import org.junit.Test;

import com.capillary.social.FacebookException;
import com.capillary.social.FacebookMessageStub;
import com.capillary.social.ReceiptMessage;

public class ReceiptMessageValidatorTest extends FacebookMessageStub {

    @Test
    public void shouldBeInvalidWhenNoRecipientIsProvided() throws FacebookException, TException {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.recipientName = null;
        Assert.assertEquals(true,
                new FacebookReceiptMessageStub(receiptMessage).send("", "", 100).toString().equals("{}"));
    }

    @Test
    public void shouldBeInvalidWhenOrderNumberIsNotPresent() throws FacebookException, TException {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.orderNumber = null;
        Assert.assertEquals(true,
                new FacebookReceiptMessageStub(receiptMessage).send("", "", 100).toString().equals("{}"));
    }

    @Ignore("un-ignore when unique order number is implemented")
    @Test
    public void shouldBeInvalidWhenOrderIsNotUnique() throws FacebookException, TException {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        // set already present receipt number
        Assert.assertEquals(true,
                new FacebookReceiptMessageStub(receiptMessage).send("", "", 100).toString().equals("{}"));
    }

    @Test
    public void shouldBeInvalidWhenCurrencyIsNotPresent() throws FacebookException, TException {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.currency = null;
        Assert.assertEquals(true,
                new FacebookReceiptMessageStub(receiptMessage).send("", "", 100).toString().equals("{}"));
    }

    @Test
    public void shouldBeInvalidWhenPaymentMethodIsNotPresent() throws FacebookException, TException {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.paymentMethod = null;
        Assert.assertEquals(true,
                new FacebookReceiptMessageStub(receiptMessage).send("", "", 100).toString().equals("{}"));
    }

    @Test
    public void shouldBeInvalidWhenSummaryIsNotPresent() throws FacebookException, TException {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.summary = null;
        Assert.assertEquals(true,
                new FacebookReceiptMessageStub(receiptMessage).send("", "", 100).toString().equals("{}"));
    }

    

}
