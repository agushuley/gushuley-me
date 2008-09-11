package com.gushuley.me.ttp;

import java.util.Date;

public class DateTimeTag 
extends TypedTag 
{
	private final Date data;

	public DateTimeTag(short tag, Date data) {
		super(TypedTagPacket.TYPE_DATE_TIME, tag);
		this.data = data;
	}

	public Date getData() {
		return data;
	}
}
