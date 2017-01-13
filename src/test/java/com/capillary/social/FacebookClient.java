package com.capillary.social;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.capillary.social.FacebookService;

public class FacebookClient {

    public static FacebookService.Client getFacebookServiceClient() {
        TTransport transport = new TSocket("192.168.33.1", 9232);
        try {
            transport.open();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
        TProtocol protocol = new TBinaryProtocol(transport);
        return (new FacebookService.Client(protocol));
    }

}
