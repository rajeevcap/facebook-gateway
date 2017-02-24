package com.capillary.social;

import static com.capillary.social.GatewayResponseType.sent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicStatusLine;

import com.capillary.social.services.api.FacebookMessage;
import com.capillary.social.services.impl.FacebookButtonMessage;
import com.capillary.social.services.impl.FacebookGenericMessage;
import com.capillary.social.services.impl.FacebookListMessage;
import com.capillary.social.services.impl.FacebookQuickReplyMessage;
import com.capillary.social.services.impl.FacebookReceiptMessage;
import com.capillary.social.services.impl.FacebookTextMessage;
import com.google.gson.JsonObject;

public class FacebookMessageStub {

    protected boolean getValidation(FacebookMessage facebookMessage, MessageType messageType) {
        return facebookMessage.send("recipientId", "pageId", 0).gatewayResponseType == sent;
    }
    
    protected boolean getValidation(FacebookMessage facebookMessage, MessageType messageType, String userId, String pageId, long orgId) {
        return facebookMessage.send(userId, pageId, orgId).gatewayResponseType == sent;
    }

    protected class FacebookTextMessageStub extends FacebookTextMessage {
        public FacebookTextMessageStub(TextMessage textMessage) {
            this.setTextMessage(textMessage);
        }

        @Override
        protected HttpResponse sendMessage(String pageId, long orgId, JsonObject payload)
                throws UnsupportedEncodingException, IOException, ClientProtocolException {
            return getDummyResponse(pageId, orgId, payload);
        }

        @Override
        protected boolean checkUserPolicy(String recipientId, String pageId) {
            return true;
        }
    }

    protected class FacebookButtonMessageStub extends FacebookButtonMessage {
        public FacebookButtonMessageStub(ButtonMessage buttonMessage) {
            this.setButtonMessage(buttonMessage);
        }

        @Override
        protected HttpResponse sendMessage(String pageId, long orgId, JsonObject payload)
                throws UnsupportedEncodingException, IOException, ClientProtocolException {
            return getDummyResponse(pageId, orgId, payload);
        }

        @Override
        protected boolean checkUserPolicy(String recipientId, String pageId) {
            return true;
        }

    }

    protected class FacebookGenericMessageStub extends FacebookGenericMessage {

        public FacebookGenericMessageStub(GenericMessage genericMessage) {
            this.setGenericMessage(genericMessage);
        }

        @Override
        protected HttpResponse sendMessage(String pageId, long orgId, JsonObject payload)
                throws UnsupportedEncodingException, IOException, ClientProtocolException {
            return getDummyResponse(pageId, orgId, payload);
        }

        @Override
        protected boolean checkUserPolicy(String recipientId, String pageId) {
            return true;
        }

    }

    protected class FacebookQuickReplyMessageStub extends FacebookQuickReplyMessage {

        public FacebookQuickReplyMessageStub(QuickReplyMessage quickReplyMessage) {
            this.setQuickReplyMessage(quickReplyMessage);
        }

        @Override
        protected HttpResponse sendMessage(String pageId, long orgId, JsonObject payload)
                throws UnsupportedEncodingException, IOException, ClientProtocolException {
            return getDummyResponse(pageId, orgId, payload);
        }

        @Override
        protected boolean checkUserPolicy(String recipientId, String pageId) {
            return true;
        }
    }

    protected class FacebookReceiptMessageStub extends FacebookReceiptMessage {

        public FacebookReceiptMessageStub(ReceiptMessage receiptMessage) {
            this.setReceiptMessage(receiptMessage);
        }

        @Override
        protected HttpResponse sendMessage(String pageId, long orgId, JsonObject payload)
                throws UnsupportedEncodingException, IOException, ClientProtocolException {
            return getDummyResponse(pageId, orgId, payload);
        }

        @Override
        protected boolean checkUserPolicy(String recipientId, String pageId) {
            return true;
        }
    }

    protected class FacebookListMessageStub extends FacebookListMessage {

        public FacebookListMessageStub(ListMessage listMessage) {
            this.setListMessage(listMessage);
        }

        @Override
        protected HttpResponse sendMessage(String pageId, long orgId, JsonObject payload)
                throws UnsupportedEncodingException, IOException, ClientProtocolException {
            return getDummyResponse(pageId, orgId, payload);
        }

        @Override
        protected boolean checkUserPolicy(String recipientId, String pageId) {
            return true;
        }
    }

    public HttpResponse getDummyResponse(String pageId, long orgId, JsonObject payload) {
        HttpResponseFactory factory = new DefaultHttpResponseFactory();
        HttpResponse response = factory.newHttpResponse((StatusLine) new BasicStatusLine(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "abc"), null);
        response.setEntity(getHttpEntity());
        return response;
    }

    public HttpEntity getHttpEntity() {
        HttpEntity entity = new HttpEntity() {

            @Override
            public void writeTo(OutputStream outstream) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public boolean isStreaming() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isRepeatable() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isChunked() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Header getContentType() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public long getContentLength() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public Header getContentEncoding() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public InputStream getContent() throws IOException, IllegalStateException {
                return new ByteArrayInputStream("message".getBytes());
            }

            @Override
            public void consumeContent() throws IOException {
                // TODO Auto-generated method stub

            }
        };
        return entity;
    }

}
