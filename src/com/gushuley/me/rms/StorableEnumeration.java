package com.gushuley.me.rms;

public interface StorableEnumeration {
	public abstract boolean hasNext();
	public abstract Storable nextStorable() throws StorableException;
	public void close();
}