package com.capillary.social.validator;

import junit.framework.Assert;

import org.junit.Test;

import com.capillary.social.FacebookEntityGenerator;
import com.capillary.social.FacebookMessageStub;
import com.capillary.social.ReceiptMessage;
import com.capillary.social.Summary;

public class SummaryValidatorTest extends FacebookMessageStub {

    @Test
    public void shouldBeInvalidWhenTotalCostIsZero() {
        ReceiptMessage receiptMessage = FacebookEntityGenerator.generateReceiptMessage();
        receiptMessage.summary = new Summary();
        Assert.assertEquals(false, new FacebookReceiptMessageStub(receiptMessage).send("", "", 100));
    }
    
    @Test
    public void shouldBeValid() {
        ReceiptMessage receiptMessage = FacebookEntityGenerator.generateReceiptMessage();
        receiptMessage.summary = FacebookEntityGenerator.generateSummary();
        Assert.assertEquals(true, new FacebookReceiptMessageStub(receiptMessage).send("", "", 100));
    }
    
}
