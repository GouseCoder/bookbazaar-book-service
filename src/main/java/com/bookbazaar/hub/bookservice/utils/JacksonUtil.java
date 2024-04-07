package com.bookbazaar.hub.bookservice.utils;

import java.lang.reflect.Field;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JacksonUtil {
	
	public static ObjectMapper mapper;
	
	static {
		
		mapper = new ObjectMapper();
	}
	
	//general method for converting List of objects to arrayNode by gouse
		public static <T> ArrayNode convertListToArrayNode(List<T> list, Class<T> clazz) {
	        ObjectMapper objectMapper = new ObjectMapper();
	        ArrayNode arrayNode = objectMapper.createArrayNode();

	        for (T obj : list) {
	            ObjectNode objectNode = objectMapper.createObjectNode();
	            Field[] fields = clazz.getDeclaredFields();

	            for (Field field : fields) {
	                field.setAccessible(true);
	                try {
	                    Object value = field.get(obj);
	                    if (value != null) {
	                        objectNode.put(field.getName(), String.valueOf(value));
	                    }
	                } catch (IllegalAccessException e) {
	                    e.printStackTrace();
	                }
	            }

	            arrayNode.add(objectNode);
	        }

	        return arrayNode;
	    }

}
