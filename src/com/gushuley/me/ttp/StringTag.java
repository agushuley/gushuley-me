package com.gushuley.me.ttp;

public class StringTag 
extends TypedTag {
	private final String data;

	public StringTag(short tag, String data) {
		super(TypedTagPacket.TYPE_STRING, tag);
		this.data = data;
	}

	public String getData() {
		return data;
	}
}
