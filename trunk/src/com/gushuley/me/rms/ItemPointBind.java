package com.gushuley.me.rms;

public class ItemPointBind {
	final private byte tag;
	final private ItemPoint point;
	final private boolean expired;
	
	public ItemPointBind(byte tag, ItemPoint point) {
		this(tag, point, false);
	}
	
	public ItemPointBind(byte tag, ItemPoint point, boolean expired) {
		this.tag = tag;
		this.point = point;
		this.expired = expired;
	}
	
	public short getTag() {
		return tag;
	}
	
	public ItemPoint getPoint() {
		return point;
	}

	public boolean isExpired() {
		return expired;
	}
}
