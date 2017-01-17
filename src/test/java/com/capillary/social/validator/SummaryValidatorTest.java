package com.capillary.social.validator;

import static com.capillary.social.FacebookEntityGenerator.generateReceiptMessage;
import junit.framework.Assert;

import org.junit.Test;

import com.capillary.social.FacebookEntityGenerator;
import com.capillary.social.FacebookMessageStub;
import com.capillary.social.ReceiptMessage;
import com.capillary.social.Summary;

public class SummaryValidatorTest extends FacebookMessageStub {

    @Test
    public void shouldBeInvalidWhenTotalCostIsZero() {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.summary = new Summary();
        Assert.assertEquals(true,
                new FacebookReceiptMessageStub(receiptMessage).send("", "", 100).toString().equals("{}"));
    }

    @Test
    public void shouldBeValid() {
        ReceiptMessage receiptMessage = generateReceiptMessage();
        receiptMessage.summary = FacebookEntityGenerator.generateSummary();
        Assert.assertEquals(false,
                new FacebookReceiptMessageStub(receiptMessage).send("", "", 100).toString().equals("{}"));
    }

}
