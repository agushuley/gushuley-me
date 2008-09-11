package com.gushuley.me.ttp;

public class ShortTag 
extends TypedTag {
	private final short data;

	public ShortTag(short tag, short data) {
		super(TypedTagPacket.TYPE_SHORT, tag);
		this.data = data;
	}

	public short getData() {
		return data;
	}
}
