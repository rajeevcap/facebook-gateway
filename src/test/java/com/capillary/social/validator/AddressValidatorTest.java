package com.capillary.social.validator;

import static com.capillary.social.FacebookEntityGenerator.generateReceiptMessage;

import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.common.base.Preconditions;
import junit.framework.Assert;

import org.junit.Test;

import com.capillary.social.FacebookEntityGenerator;
import com.capillary.social.FacebookMessageStub;
import com.capillary.social.MessageType;
import com.capillary.social.ReceiptMessage;

public class AddressValidatorTest extends FacebookMessageStub {

    @Test
    public void shouldBeInvalidWhenStreetOneIsEmpty() {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.address = FacebookEntityGenerator.generateAddress();
        receiptMessage.address.streetOne = null;
        Assert.assertEquals(false,
                getValidation(new FacebookReceiptMessageStub(receiptMessage), MessageType.receiptMessage));
    }

    @Test
    public void shouldBeInvalidWhenCityIsEmpty() {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.address = FacebookEntityGenerator.generateAddress();
        receiptMessage.address.city = null;
        Assert.assertEquals(false,
                getValidation(new FacebookReceiptMessageStub(receiptMessage), MessageType.receiptMessage));
    }

    @Test
    public void shouldBeInvalidWhenPostalCodeIsEmpty() {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.address = FacebookEntityGenerator.generateAddress();
        receiptMessage.address.postalCode = null;
        Assert.assertEquals(false,
                getValidation(new FacebookReceiptMessageStub(receiptMessage), MessageType.receiptMessage));
    }

    @Test
    public void shouldBeInvalidWhenStateIsEmpty() {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.address = FacebookEntityGenerator.generateAddress();
        receiptMessage.address.state = null;
        Assert.assertEquals(false,
                getValidation(new FacebookReceiptMessageStub(receiptMessage), MessageType.receiptMessage));
    }

    @Test
    public void shouldBeInvalidWhenCountryIsEmpty() {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.address = FacebookEntityGenerator.generateAddress();
        receiptMessage.address.country = null;
        Assert.assertEquals(false,
                getValidation(new FacebookReceiptMessageStub(receiptMessage), MessageType.receiptMessage));
    }

    @Test
    public void shouldBeValid() {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.address = FacebookEntityGenerator.generateAddress();
        Assert.assertEquals(true,
                getValidation(new FacebookReceiptMessageStub(receiptMessage), MessageType.receiptMessage));
    }

    @Test
    public void testEnum(){
        String abc = Preconditions.checkNotNull("ABC", "Null entity", "null value1");
        String str = Preconditions.checkNotNull("api.adwords.", "Null property key prefix for", this);
        Assert.assertEquals(null, OfflineCredentials.Api.ADWORDS);
    }

}
