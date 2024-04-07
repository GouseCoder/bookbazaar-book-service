package com.bookbazaar.hub.bookservice.utils;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class CommonsUtils {
	
public ObjectNode createResultNode() {
		
		ObjectNode resultNode = JacksonUtil.mapper.createObjectNode();
		ObjectNode dataObject = JacksonUtil.mapper.createObjectNode();
		ObjectNode errorObject = JacksonUtil.mapper.createObjectNode();
		
		resultNode.set(AppConstants.DATA_OBJECT, dataObject);
		resultNode.set(AppConstants.ERROR_OBJECT, errorObject);
		return resultNode;
		
	}
	
	public int getErrorCode(JsonNode resultNode) {
		
		JsonNode errObject = resultNode.get(AppConstants.ERROR_OBJECT);
		int errorCode = errObject.has(AppConstants.ERROR_CODE) ? errObject.get(AppConstants.ERROR_CODE).asInt() : 200;
		return errorCode;
		
	}
	
	public int getStatusCode(JsonNode resultNode) {
		
		int statusCode = resultNode.has(AppConstants.STATUS_CODE) ? resultNode.get(AppConstants.STATUS_CODE).asInt() : 200;
		return statusCode;
		
	}
	
	public void setErrorResponse(ObjectNode resultNode, int errorCode, String errorReason) {
		ObjectNode errorObject = (ObjectNode) resultNode.get(AppConstants.DATA_OBJECT);
		resultNode.put(AppConstants.ERROR_CODE, errorCode);
		errorObject.put(AppConstants.ERROR_CODE, errorCode);
	    errorObject.put(AppConstants.ERROR_REASON, errorReason);
	}

}
