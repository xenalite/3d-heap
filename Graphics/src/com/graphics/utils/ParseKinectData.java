package com.graphics.utils;

import java.util.HashMap;
import java.util.Map;

public class ParseKinectData {

	public static Map<String, String> parse(String data){
		
		Map<String, String> dic = new HashMap<String, String>();
		
		String[] keysValues = data.split(":");
		
		for(int i = 0; i < keysValues.length; i+=2){
			String key = keysValues[i];
			String value = keysValues[i+1];
			dic.put(key, value);
		}
		
		return dic;
	}
	
}
