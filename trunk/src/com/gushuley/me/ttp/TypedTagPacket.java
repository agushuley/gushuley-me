package com.gushuley.me.ttp;

import com.gushuley.java.utils.*;

public class TypedTagPacket 
{
	public static final short RESULT_CODE_TAG = 1;
	public static final short ERROR_TEXT_TAG = 2;
	public static final short MESSAGE_TAG = 3;
	
	public static final byte TYPE_BYTE = 1;
	public static final byte TYPE_SHORT = 2;
	public static final byte TYPE_INT = 3;
	public static final byte TYPE_STRING = 4;
	public static final byte TYPE_DATE_TIME = 5;
	public static final byte TYPE_LIST = 6;
	public static final byte TYPE_BINARY = 7;
	
	public static final short ERROR_CODE_BAD_INPUT_PACKET = 1;
	public static final short ERROR_CODE_BAD_UNKNOWN_MESSAGE = 2;
	public static final short ERROR_CODE_BAD_GENERAL_ERROR = 99;
	
	private short resultCode;
	private short message;
	private String errorText;
	private final List tags = new ArrayList();
	
	public String getErrorText() {
		return errorText;
	}
	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}
	
	public List getTags() {
		return tags;
	}
	
	public short getResultCode() {
		return resultCode;
	}
	public void setResultCode(short resultCode) {
		this.resultCode = resultCode;
	}
	
	public short getMessage() {
		return message;
	}
	public void setMessage(short message) {
		this.message = message;
	} 
}
