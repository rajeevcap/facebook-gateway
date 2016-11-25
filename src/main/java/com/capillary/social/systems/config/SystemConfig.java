package com.capillary.social.systems.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SystemConfig {

	// ======= Sytem Running Mode Config ======//
	@Value("${facebook.running.mode}")
	public String RUNNING_MODE;

	// =============== Facebook Level Configs ================//
	@Value("${facebook.thrift.timeout}")
	public int FACEBOOK_TIME_OUT;

	@Value("${facebook.thrift.service.max.thread}")
	public int SERVICE_MAX_THREAD;

	@Value("${facebook.recv.time.out}")
	public int FACEBOOK_RECV_TIME_OUT;


}
