package com.gushuley.me.ttp;

public abstract class TypedTag {
	private final short tag;
	private final byte type;
	
	public TypedTag(byte type, short tag) {
		this.type = type;
		this.tag = tag;
	}

	public short getTag() {
		return tag;
	}

	public byte getType() {
		return type;
	}
}
