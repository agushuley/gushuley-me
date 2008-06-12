package com.gushuley.me.date;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.gushuley.me.Tools;

public class TZDate extends Date {
	private final TimeZone tz;

	public TZDate(long date, TimeZone tz) {
		super(date);
		this.tz = tz;
	}

	public TZDate(long date) {
		this(date, Calendar.getInstance().getTimeZone());
	}

	public TZDate(Calendar cal) {
		this(cal.getTime().getTime(), cal.getTimeZone());
	}
	
	public TZDate() {
		this(Calendar.getInstance());
	}	

	public TimeZone getTz() {
		return tz;
	}
	
	private static final char[] SPLIT_SYMBOLS = new char[]{'.', ',', '/', '*', '#'};
	private static final byte[] MONTH_DAYS = new byte[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	private static final byte[] MONTH_DAYS_LEAP = new byte[]{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	
	private static class DateHolder {
		int year;
		int day;
		int month;
	}
	
	private static DateHolder parseInternal(final String date) throws InvalidDateException {
		int firstIndex = -1;
        int lastIndex = -1;
        final DateHolder d = new DateHolder();

        for (int i = 0; i < SPLIT_SYMBOLS.length; i++) {
        	final int index = date.indexOf(SPLIT_SYMBOLS[i]);
        	if (index != -1) {
        		if (firstIndex >= 0) {
        			if(index < firstIndex) {
        				firstIndex = index;
        			}
        		} else {
        			firstIndex = index;
        		}
        	}
        }
        for (int i = 0; i < SPLIT_SYMBOLS.length; i++) {
        	final int index = date.indexOf(SPLIT_SYMBOLS[i], firstIndex + 1);
        	if (index != -1) {
        		if (lastIndex >= 0) {
        			if(index < lastIndex) {
        				lastIndex = index;
        			} 
        		} else {
        			lastIndex = index;
        		}
            }
        }
        if (firstIndex + 1 == 0 || lastIndex + 1 == 0 || firstIndex == lastIndex) {
        	throw new InvalidDateException("Нет всех компонент даты");
        }
        try {
        	d.day = Integer.parseInt(date.substring(0, firstIndex));
        } catch (NumberFormatException e) {
        	throw new InvalidDateException("Ошибка дня");
        }
        try {
        	d.month = Integer.parseInt(date.substring(firstIndex + 1, lastIndex));
        } catch (NumberFormatException e) {
        	throw new InvalidDateException("Ошибка месяца");
        }
        try {
        	d.year = Integer.parseInt(date.substring(lastIndex + 1, date.length()));
        } catch (NumberFormatException e) {
        	throw new InvalidDateException("Ошибка года");
        }
        if (d.year <= 50) {
        	d.year += 2000;
        } else if (d.year > 50 && d.year < 100) {
        	d.year += 1900;
        }
        if (d.month > 12 || d.month < 1) {
        	throw new InvalidDateException("Месяц должен быть между 1 и 12");
        }
        byte maxDay = MONTH_DAYS[d.month - 1];
        if ((d.year % 4 == 0 && d.year % 100 != 0) || d.year % 400 == 0) {
        	maxDay = MONTH_DAYS_LEAP[d.month - 1];
        }
        if (d.day > maxDay || d.day < 1) {
        	throw new InvalidDateException("День должен быть между 1 и " + maxDay);
        }
		return d;
	}
	
	public static String normalizeDate(final String date) throws InvalidDateException {
		if (Tools.isEmpty(date)) {
            return null;
        } 
		
        final DateHolder d = parseInternal(date);
        return "" + d.day + '.' + d.month + '.' + d.year;
	}
	
	public static TZDate parseDate(final String date) throws InvalidDateException {
		if (Tools.isEmpty(date)) {
            return new TZDate();
        } 
		
		final DateHolder d = parseInternal(date);
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, d.year);
        cal.set(Calendar.MONTH, d.month - 1);
        cal.set(Calendar.DATE, d.day);
        return new TZDate(cal);
	}
}
