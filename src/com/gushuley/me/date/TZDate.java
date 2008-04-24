package com.gushuley.me.date;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TZDate extends Date {
	private final TimeZone tz;

	public TZDate(long date, TimeZone tz) {
		super(date);
		this.tz = tz;
	}

	public TZDate(long date) {
		this(date, Calendar.getInstance().getTimeZone());
	}
	
	public TZDate() {
		this(Calendar.getInstance().getTime().getTime(), Calendar.getInstance().getTimeZone());
	}	

	public TimeZone getTz() {
		return tz;
	}
}
