package com.bookbazaar.hub.bookservice.utils;

public class ResponseConstants {

	// signup
	public static final int EMAIL_ALREADY_REGISTRED = 30;
	public static final int EMAIL_SENT_WITH_OTP = 200;
	public static final int OTP_NOT_SENT = 220;
	public static final int OTP_EXPIRED = 420;
	public static final int OTP_VALID = 200;
	public static final int OTP_INVALID = 502;
	public static final int REGENERATE_OTP = 301;
	public static final int USER_ADDED_IN_DB = 200;
	public static final int USER_NOT_ADDED_IN_DB = 250;
	public static final int INTERNAL_SERVER_ERROR = 0;
	public static final int INVALID_USER = 31;

	// login
	public static final int USER_SECCESFULLY_LOGGED_IN = 200;
	public static final int USER_SECCESFULLY_LOGGED_OUT = 200;
	
	//Book processing
	public static final int BOOK_ADDED_SUCCESSFULLY = 201;
	
	//cart processing
	public static final int PRODUCT_ALREADY_IN_CART = 202;
	public static final int ADDED_TO_CART = 203;
	
	//profile update
	public static final int PASSWORD_CHANGED = 204;
	public static final int ADDRESS_CHANGED = 205;
	public static final int PHONE_CHANGED = 206;

}
