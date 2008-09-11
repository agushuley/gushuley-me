package com.gushuley.me.ttp;

public class TTPacketException 
extends Exception 
{
	public TTPacketException(String string, Exception e) {
		super(string + " " + e);
	}

	public TTPacketException(String string) {
		super(string);
	}
}
