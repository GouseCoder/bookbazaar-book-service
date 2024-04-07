package com.bookbazaar.hub.bookservice.utils;

import com.fasterxml.jackson.databind.JsonNode;

public class ApiHttpResponse {
	
	private int statusCode;
	private JsonNode errorObject;
	private JsonNode dataObject;
	
	public ApiHttpResponse(int statusCode, JsonNode errorObject, JsonNode dataObject) {
		super();
		this.statusCode = statusCode;
		this.errorObject = errorObject;
		this.dataObject = dataObject;
	}

	public ApiHttpResponse(int statusCode, JsonNode errorObject) {
		super();
		this.statusCode = statusCode;
		this.errorObject = errorObject;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public JsonNode getErrorObject() {
		return errorObject;
	}

	public void setErrorObject(JsonNode errorObject) {
		this.errorObject = errorObject;
	}

	public JsonNode getDataObject() {
		return dataObject;
	}

	public void setDataObject(JsonNode dataObject) {
		this.dataObject = dataObject;
	}
	
	
	
}
