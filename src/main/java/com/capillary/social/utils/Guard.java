package com.capillary.social.utils;

import java.util.List;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 5/9/17
 */
public class Guard {

	public static void notNull(Object object,String name){
		if(object ==null){
			throw new RuntimeException(name+" should not be null");
		}
	}

	public static void notNullOrEmpty(String object,String name){
		notNull(object,name);
		if(object.isEmpty()){
			throw new RuntimeException(name+" should not be empty");
		}
	}
	public static void notNullOrEmpty(List object, String name){
		notNull(object,name);
		if(object.isEmpty()){
			throw new RuntimeException(name+" should not be empty");
		}
	}
}
