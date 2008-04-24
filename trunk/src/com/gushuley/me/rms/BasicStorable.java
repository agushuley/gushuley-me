package com.gushuley.me.rms;


public abstract class BasicStorable implements Storable {
	private int recordId;

	public BasicStorable() {
		super();
	}

	public int getRecordId() {
		return recordId;
	}

	public boolean isKey(Object key) {
		return false;
	}

	public void setRecordId(int id) {
		this.recordId = id;
	}
}