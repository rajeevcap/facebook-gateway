package com.capillary.social.external.impl;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.capillary.social.systems.config.SystemConfig;
import com.capillary.social.FacebookException;
import com.capillary.social.FacebookService.Iface;

public class FacebookServiceListener implements Iface {

	private static Logger logger = LoggerFactory
			.getLogger(FacebookServiceListener.class);

	@Autowired
	private SystemConfig systemConfig;

	@Override
	public boolean isAlive() throws TException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int sendMessage(String pageID, String recipientID) throws FacebookException,
			TException {
		// TODO Auto-generated method stub
		return 20;
	}

}
