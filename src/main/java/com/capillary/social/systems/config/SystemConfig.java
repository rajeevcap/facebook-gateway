package com.capillary.social.systems.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SystemConfig {

	
	private static final Logger logger = LoggerFactory
			.getLogger(SystemConfig.class);
	// ======= Sytem Running Mode Config ======//
	@Value("${facebook.running.mode}")
	public String RUNNING_MODE = "prod";
	
	// =============== Facebook Level Configs ================//
	@Value("${facebook.thrift.timeout?:1}")
	public int FACEBOOK_TIME_OUT = 10;

	@Value("${facebook.thrift.service.max.thread?:10}")
	public int SERVICE_MAX_THREAD = 20;

	@Value("${facebook.recv.time.out?:10}")
	public int FACEBOOK_RECV_TIME_OUT = 30;

	public SystemConfig() {
		super();
		logger.info("asda");
	}
}
