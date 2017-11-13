package com.capillary.social.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jdk.nashorn.api.scripting.JSObject;

import java.util.LinkedList;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 6/11/17
 */
public class FBFilter {
	public static enum Operator {EQUAL,
		NOT_EQUAL,
		GREATER_THAN,
		GREATER_THAN_OR_EQUAL,
		LESS_THAN,
		LESS_THAN_OR_EQUAL,
		IN_RANGE, NOT_IN_RANGE,
		CONTAIN, NOT_CONTAIN,
		IN,
		NOT_IN,
		STARTS_WITH,
		ANY,
		ALL,
		AFTER,
		BEFORE,
		NONE}

	private JsonArray _filters = new JsonArray();

	public void addFilter(String field, Operator operator,Object value){
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("field",field);
		jsonObject.addProperty("operator",operator.name());
		jsonObject.addProperty("value",value.toString());
		_filters.add(jsonObject);
	}

	public String toString(){
		return _filters.toString();
	}
}
