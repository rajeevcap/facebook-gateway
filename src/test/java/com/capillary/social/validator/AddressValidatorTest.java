package com.capillary.social.validator;

import static com.capillary.social.FacebookEntityGenerator.generateReceiptMessage;
import junit.framework.Assert;

import org.junit.Test;

import com.capillary.social.FacebookEntityGenerator;
import com.capillary.social.FacebookMessageStub;
import com.capillary.social.ReceiptMessage;

public class AddressValidatorTest extends FacebookMessageStub {

    @Test
    public void shouldBeInvalidWhenStreetOneIsEmpty() {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.address = FacebookEntityGenerator.generateAddress();
        receiptMessage.address.streetOne = null;
        Assert.assertEquals(true,
                new FacebookReceiptMessageStub(receiptMessage).send("", "", 100).toString().equals("{}"));
    }

    @Test
    public void shouldBeInvalidWhenCityIsEmpty() {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.address = FacebookEntityGenerator.generateAddress();
        receiptMessage.address.city = null;
        Assert.assertEquals(true,
                new FacebookReceiptMessageStub(receiptMessage).send("", "", 100).toString().equals("{}"));
    }

    @Test
    public void shouldBeInvalidWhenPostalCodeIsEmpty() {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.address = FacebookEntityGenerator.generateAddress();
        receiptMessage.address.postalCode = null;
        Assert.assertEquals(true,
                new FacebookReceiptMessageStub(receiptMessage).send("", "", 100).toString().equals("{}"));
    }

    @Test
    public void shouldBeInvalidWhenStateIsEmpty() {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.address = FacebookEntityGenerator.generateAddress();
        receiptMessage.address.state = null;
        Assert.assertEquals(true,
                new FacebookReceiptMessageStub(receiptMessage).send("", "", 100).toString().equals("{}"));
    }

    @Test
    public void shouldBeInvalidWhenCountryIsEmpty() {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.address = FacebookEntityGenerator.generateAddress();
        receiptMessage.address.country = null;
        Assert.assertEquals(true,
                new FacebookReceiptMessageStub(receiptMessage).send("", "", 100).toString().equals("{}"));
    }

    @Test
    public void shouldBeValid() {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.address = FacebookEntityGenerator.generateAddress();
        Assert.assertEquals(false,
                new FacebookReceiptMessageStub(receiptMessage).send("", "", 100).toString().equals("{}"));
    }

}