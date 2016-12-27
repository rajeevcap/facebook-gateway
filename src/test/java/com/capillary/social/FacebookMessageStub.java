package com.capillary.social;

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

import com.capillary.social.services.impl.FacebookButtonMessage;
import com.capillary.social.services.impl.FacebookGenericMessage;
import com.capillary.social.services.impl.FacebookQuickReplyMessage;
import com.capillary.social.services.impl.FacebookReceiptMessage;
import com.capillary.social.services.impl.FacebookTextMessage;

public class FacebookMessageStub {

    protected class FacebookTextMessageStub extends FacebookTextMessage {
        public FacebookTextMessageStub(TextMessage textMessage) {
            super(textMessage);
        }

        @Override
        protected HttpResponse sendMessage(String pageId, int orgId, String payload)
                throws UnsupportedEncodingException, IOException, ClientProtocolException {
            return getDummyResponse(pageId, orgId, payload);
        }
    }

    protected class FacebookButtonMessageStub extends FacebookButtonMessage {
        public FacebookButtonMessageStub(ButtonMessage buttonMessage) {
            super(buttonMessage);
        }

        @Override
        protected HttpResponse sendMessage(String pageId, int orgId, String payload)
                throws UnsupportedEncodingException, IOException, ClientProtocolException {
            return getDummyResponse(pageId, orgId, payload);
        }

    }

    protected class FacebookGenericMessageStub extends FacebookGenericMessage {

        public FacebookGenericMessageStub(GenericMessage genericMessage) {
            super(genericMessage);
        }

        @Override
        protected HttpResponse sendMessage(String pageId, int orgId, String payload)
                throws UnsupportedEncodingException, IOException, ClientProtocolException {
            return getDummyResponse(pageId, orgId, payload);
        }

    }

    protected class FacebookQuickReplyMessageStub extends FacebookQuickReplyMessage {

        public FacebookQuickReplyMessageStub(QuickReplyMessage quickReplyMessage) {
            super(quickReplyMessage);
        }

        @Override
        protected HttpResponse sendMessage(String pageId, int orgId, String payload)
                throws UnsupportedEncodingException, IOException, ClientProtocolException {
            return getDummyResponse(pageId, orgId, payload);
        }
    }

    protected class FacebookReceiptMessageStub extends FacebookReceiptMessage {

        public FacebookReceiptMessageStub(ReceiptMessage receiptMessage) {
            super(receiptMessage);
        }

        @Override
        protected HttpResponse sendMessage(String pageId, int orgId, String payload)
                throws UnsupportedEncodingException, IOException, ClientProtocolException {
            return getDummyResponse(pageId, orgId, payload);
        }
    }

    public HttpResponse getDummyResponse(String pageId, int orgId, String payload) {
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
                return new ByteArrayInputStream("messge".getBytes());
            }

            @Override
            public void consumeContent() throws IOException {
                // TODO Auto-generated method stub

            }
        };
        return entity;
    }

}
