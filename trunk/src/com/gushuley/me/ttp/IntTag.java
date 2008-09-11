package com.gushuley.me.ttp;

public class IntTag 
extends TypedTag {
	private final int data;

	public IntTag(short tag, int data) {
		super(TypedTagPacket.TYPE_INT, tag);
		this.data = data;
	}

	public int getData() {
		return data;
	}
}
