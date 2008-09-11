package com.gushuley.me.ttp;

public class ByteTag 
extends TypedTag 
{
	private final byte data;

	public ByteTag(short tag, byte data) {
		super(TypedTagPacket.TYPE_BYTE, tag);
		this.data = data;
	}

	public byte getData() {
		return data;
	}
}
