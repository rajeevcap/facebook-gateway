package com.capillary.social.validator;

import static com.capillary.social.FacebookEntityGenerator.generateReceiptMessage;
import junit.framework.Assert;

import org.junit.Test;

import com.capillary.social.FacebookEntityGenerator;
import com.capillary.social.FacebookMessageStub;
import com.capillary.social.MessageType;
import com.capillary.social.ReceiptMessage;

public class ReceiptElementValidatorTest extends FacebookMessageStub {

    @Test
    public void shouldBeInvalidWhenTitleIsNotPresent() {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.receiptElementList = FacebookEntityGenerator.generateReceiptElementList();
        receiptMessage.receiptElementList.get(0).title = null;
        Assert.assertEquals(false,
                getValidation(new FacebookReceiptMessageStub(receiptMessage), MessageType.receiptMessage));
    }

    @Test
    public void shouldBeInvalidWhenPriceIsNegativePresent() {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.receiptElementList = FacebookEntityGenerator.generateReceiptElementList();
        receiptMessage.receiptElementList.get(0).price = -1;
        Assert.assertEquals(false,
                getValidation(new FacebookReceiptMessageStub(receiptMessage), MessageType.receiptMessage));
    }

    @Test
    public void shouldBeValid() {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.receiptElementList = FacebookEntityGenerator.generateReceiptElementList();
        Assert.assertEquals(true,
                getValidation(new FacebookReceiptMessageStub(receiptMessage), MessageType.receiptMessage));
    }

}
