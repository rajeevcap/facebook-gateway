package com.capillary.social.validator;

import junit.framework.Assert;

import org.junit.Test;

import com.capillary.social.FacebookEntityGenerator;
import com.capillary.social.FacebookMessageStub;
import com.capillary.social.ReceiptMessage;

public class AddressValidatorTest extends FacebookMessageStub {

    @Test
    public void shouldBeInvalidWhenStreetOneIsEmpty() {
        ReceiptMessage receiptMessage = FacebookEntityGenerator.generateReceiptMessage();
        receiptMessage.address = FacebookEntityGenerator.generateAddress();
        receiptMessage.address.streetOne = null;
        Assert.assertEquals(false, new FacebookReceiptMessageStub(receiptMessage).send("", "", 100));
    }
    
    @Test
    public void shouldBeInvalidWhenCityIsEmpty() {
        ReceiptMessage receiptMessage = FacebookEntityGenerator.generateReceiptMessage();
        receiptMessage.address = FacebookEntityGenerator.generateAddress();
        receiptMessage.address.city = null;
        Assert.assertEquals(false, new FacebookReceiptMessageStub(receiptMessage).send("", "", 100));
    }
    
    @Test
    public void shouldBeInvalidWhenPostalCodeIsEmpty() {
        ReceiptMessage receiptMessage = FacebookEntityGenerator.generateReceiptMessage();
        receiptMessage.address = FacebookEntityGenerator.generateAddress();
        receiptMessage.address.postalCode = null;
        Assert.assertEquals(false, new FacebookReceiptMessageStub(receiptMessage).send("", "", 100));
    }
    
    @Test
    public void shouldBeInvalidWhenStateIsEmpty() {
        ReceiptMessage receiptMessage = FacebookEntityGenerator.generateReceiptMessage();
        receiptMessage.address = FacebookEntityGenerator.generateAddress();
        receiptMessage.address.state = null;
        Assert.assertEquals(false, new FacebookReceiptMessageStub(receiptMessage).send("", "", 100));
    }
    
    @Test
    public void shouldBeInvalidWhenCountryIsEmpty() {
        ReceiptMessage receiptMessage = FacebookEntityGenerator.generateReceiptMessage();
        receiptMessage.address = FacebookEntityGenerator.generateAddress();
        receiptMessage.address.country = null;
        Assert.assertEquals(false, new FacebookReceiptMessageStub(receiptMessage).send("", "", 100));
    }
    
    @Test
    public void shouldBeValid() {
        ReceiptMessage receiptMessage = FacebookEntityGenerator.generateReceiptMessage();
        receiptMessage.address = FacebookEntityGenerator.generateAddress();
        Assert.assertEquals(true, new FacebookReceiptMessageStub(receiptMessage).send("", "", 100));
    }
     
}
