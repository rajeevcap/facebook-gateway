package com.capillary.social.systems.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 16/10/17
 */
public class LockHolder {
	private static final ConcurrentMap<String,Object> locks = new ConcurrentHashMap<>();
	public static void lock(String key){
		Object lock = new Object();
		Object existingLock  = locks.putIfAbsent(key,lock);
		if(existingLock !=null){
			throw new RuntimeException("could not get the lock");
		}
	}
	public static void release(String key){
		locks.remove(key);
	}
}
