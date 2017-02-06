package com.capillary.social.services.impl;

import static com.capillary.social.services.impl.FacebookConstants.ADDRESS;
import static com.capillary.social.services.impl.FacebookConstants.AMOUNT;
import static com.capillary.social.services.impl.FacebookConstants.ATTACHMENT;
import static com.capillary.social.services.impl.FacebookConstants.CITY;
import static com.capillary.social.services.impl.FacebookConstants.COUNTRY;
import static com.capillary.social.services.impl.FacebookConstants.CURRENCY;
import static com.capillary.social.services.impl.FacebookConstants.ELEMENTS;
import static com.capillary.social.services.impl.FacebookConstants.ID;
import static com.capillary.social.services.impl.FacebookConstants.IMAGE_URL;
import static com.capillary.social.services.impl.FacebookConstants.MERCHANT_NAME;
import static com.capillary.social.services.impl.FacebookConstants.MESSAGE;
import static com.capillary.social.services.impl.FacebookConstants.NAME;
import static com.capillary.social.services.impl.FacebookConstants.ORDER_NUMBER;
import static com.capillary.social.services.impl.FacebookConstants.ORDER_URL;
import static com.capillary.social.services.impl.FacebookConstants.PAYLOAD;
import static com.capillary.social.services.impl.FacebookConstants.PAYMENT_METHOD;
import static com.capillary.social.services.impl.FacebookConstants.POSTAL_CODE;
import static com.capillary.social.services.impl.FacebookConstants.PRICE;
import static com.capillary.social.services.impl.FacebookConstants.QUANTITY;
import static com.capillary.social.services.impl.FacebookConstants.RECEIPT;
import static com.capillary.social.services.impl.FacebookConstants.RECIPIENT;
import static com.capillary.social.services.impl.FacebookConstants.RECIPIENT_NAME;
import static com.capillary.social.services.impl.FacebookConstants.SHIPPING_COST;
import static com.capillary.social.services.impl.FacebookConstants.STATE;
import static com.capillary.social.services.impl.FacebookConstants.STREET_1;
import static com.capillary.social.services.impl.FacebookConstants.STREET_2;
import static com.capillary.social.services.impl.FacebookConstants.SUBTITLE;
import static com.capillary.social.services.impl.FacebookConstants.SUBTOTAL;
import static com.capillary.social.services.impl.FacebookConstants.SUMMARY;
import static com.capillary.social.services.impl.FacebookConstants.TEMPLATE;
import static com.capillary.social.services.impl.FacebookConstants.TEMPLATE_TYPE;
import static com.capillary.social.services.impl.FacebookConstants.TIME_STAMP;
import static com.capillary.social.services.impl.FacebookConstants.TITLE;
import static com.capillary.social.services.impl.FacebookConstants.TOTAL_COST;
import static com.capillary.social.services.impl.FacebookConstants.TOTAL_TAX;
import static com.capillary.social.services.impl.FacebookConstants.TYPE;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.capillary.social.Adjustment;
import com.capillary.social.ReceiptElement;
import com.capillary.social.ReceiptMessage;
import com.capillary.social.services.api.FacebookMessage;
import com.capillary.social.validator.ReceiptMessageValidator;
import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Component
public class FacebookReceiptMessage extends FacebookMessage {

    private static Logger logger = LoggerFactory.getLogger(FacebookReceiptMessage.class);

    private ReceiptMessage receiptMessage;

    private boolean isNotNull(Object obj) {
        return obj != null;
    }

    @Override
    public boolean validateMessage() {
        logger.info("validating receipt message : " + receiptMessage);
        return new ReceiptMessageValidator(receiptMessage).validate();
    }

    @Override
    public JsonObject messagePayload(String recipientId) {
        JsonObject messageJson = new JsonObject();
        JsonObject recipientBody = new JsonObject();
        recipientBody.addProperty(ID, recipientId);
        messageJson.add(RECIPIENT, recipientBody);
        messageJson.add(MESSAGE, getMessageBody());
        return messageJson;
    }

    private JsonObject getMessageBody() {
        JsonObject messageBody = new JsonObject();
        messageBody.add(ATTACHMENT, getAttachmentBody());
        return messageBody;
    }

    private JsonObject getAttachmentBody() {
        JsonObject attachmentBody = new JsonObject();
        attachmentBody.addProperty(TYPE, TEMPLATE);
        attachmentBody.add(PAYLOAD, getPayloadBody());
        return attachmentBody;
    }

    private JsonElement getPayloadBody() {
        JsonObject payloadBody = new JsonObject();
        payloadBody.addProperty(TEMPLATE_TYPE, RECEIPT);
        payloadBody.addProperty(RECIPIENT_NAME, receiptMessage.recipientName);
        if (!Strings.isNullOrEmpty(receiptMessage.merchantName))
            payloadBody.addProperty(MERCHANT_NAME, receiptMessage.merchantName);
        payloadBody.addProperty(ORDER_NUMBER, receiptMessage.orderNumber);
        payloadBody.addProperty(CURRENCY, receiptMessage.currency);
        payloadBody.addProperty(PAYMENT_METHOD, receiptMessage.paymentMethod);
        if (!Strings.isNullOrEmpty(receiptMessage.orderUrl))
            payloadBody.addProperty(ORDER_URL, receiptMessage.orderUrl);
        if (!Strings.isNullOrEmpty(receiptMessage.timestamp))
            payloadBody.addProperty(TIME_STAMP, receiptMessage.timestamp);
        if (isNotNull(receiptMessage.receiptElementList))
            payloadBody.addProperty(ELEMENTS, getReceiptElementListBody().toString());
        if (isNotNull(receiptMessage.address))
            payloadBody.add(ADDRESS, getAddressBody());
        payloadBody.add(SUMMARY, getSummaryBody());
        if (isNotNull(receiptMessage.adjustmentList))
            payloadBody.addProperty(FacebookConstants.ADJUSTMENTS, getAdjustmentListBody().toString());
        return payloadBody;
    }

    private List<JsonObject> getReceiptElementListBody() {
        List<JsonObject> receiptElementBodyList = new ArrayList<JsonObject>();
        for (ReceiptElement receiptElement : receiptMessage.receiptElementList) {
            receiptElementBodyList.add(getReceiptElementBody(receiptElement));
        }
        return receiptElementBodyList;
    }

    private JsonObject getReceiptElementBody(ReceiptElement receiptElement) {
        JsonObject receiptJson = new JsonObject();
        receiptJson.addProperty(TITLE, receiptElement.title);
        if (!Strings.isNullOrEmpty(receiptElement.subtitle))
            receiptJson.addProperty(SUBTITLE, receiptElement.subtitle);
        if (receiptElement.quantity > 0)
            receiptJson.addProperty(QUANTITY, receiptElement.quantity);
        receiptJson.addProperty(PRICE, receiptElement.price);
        if (!Strings.isNullOrEmpty(receiptElement.currency))
            receiptJson.addProperty(CURRENCY, receiptElement.currency);
        if (!Strings.isNullOrEmpty(receiptElement.imageUrl))
            receiptJson.addProperty(IMAGE_URL, receiptElement.imageUrl);
        return receiptJson;
    }

    private JsonObject getAddressBody() {
        JsonObject addressBody = new JsonObject();
        addressBody.addProperty(STREET_1, receiptMessage.address.streetOne);
        if (!Strings.isNullOrEmpty(receiptMessage.address.streetTwo))
            addressBody.addProperty(STREET_2, receiptMessage.address.streetTwo);
        addressBody.addProperty(CITY, receiptMessage.address.city);
        addressBody.addProperty(POSTAL_CODE, receiptMessage.address.postalCode);
        addressBody.addProperty(STATE, receiptMessage.address.state);
        addressBody.addProperty(COUNTRY, receiptMessage.address.country);
        return addressBody;
    }

    private JsonObject getSummaryBody() {
        JsonObject summaryJson = new JsonObject();
        if (receiptMessage.summary.subtotal > 0)
            summaryJson.addProperty(SUBTOTAL, receiptMessage.summary.subtotal);
        if (receiptMessage.summary.shippingCost > 0)
            summaryJson.addProperty(SHIPPING_COST, receiptMessage.summary.shippingCost);
        if (receiptMessage.summary.totalTax > 0)
            summaryJson.addProperty(TOTAL_TAX, receiptMessage.summary.totalTax);
        summaryJson.addProperty(TOTAL_COST, receiptMessage.summary.totalCost);
        return summaryJson;
    }

    private List<JsonObject> getAdjustmentListBody() {
        List<JsonObject> adjustmentBodyList = new ArrayList<JsonObject>();
        for (Adjustment adjustment : receiptMessage.adjustmentList) {
            adjustmentBodyList.add(getAdjustment(adjustment));
        }
        return adjustmentBodyList;
    }

    private JsonObject getAdjustment(Adjustment adjustment) {
        JsonObject adjustmentJson = new JsonObject();
        if (Strings.isNullOrEmpty(adjustment.name))
            adjustmentJson.addProperty(NAME, adjustment.name);
        if (adjustment.amount >= 0)
            adjustmentJson.addProperty(AMOUNT, adjustment.amount);
        return adjustmentJson;
    }

    public void setReceiptMessage(ReceiptMessage receiptMessage) {
        this.receiptMessage = receiptMessage;
    }

}
