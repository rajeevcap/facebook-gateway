package com.capillary.social.utils;

import java.util.Collection;
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
	public static void notFalse(boolean bool,String nale){
		if(bool==false){
			throw new RuntimeException(bool+" should not be false");
		}
	}
	public static void sizeGreaterThan(Collection collection, int minsize , String name){
		if(collection.size()<minsize){
			throw new RuntimeException("size of "+name+" should be at least "+minsize);
		}
	}
}
