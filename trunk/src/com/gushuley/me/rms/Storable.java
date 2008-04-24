package com.gushuley.me.rms;

public interface Storable {
	ItemPointBind[] getBindings();	
	int getRecordId();
	void setRecordId(int id);
	boolean isKey(Object key);
}
