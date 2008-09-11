package com.gushuley.me.ttp;

public class BinaryTag 
extends TypedTag
{
	private final byte[] data;

	public BinaryTag(short tag, byte[] data) {
		super(TypedTagPacket.TYPE_BINARY, tag);
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}
}
