package com.gushuley.me;


import java.util.Calendar;

import com.gushuley.me.date.TZDate;


/**
 * 
 * @author andriy
 */
public class Tools {
    public static boolean isEmpty(String icon) {
        if (icon == null) return true;
        if (icon.trim().equals("")) return true;
        return false;
    }

    /***
     * Return second parameter if first is null
     * @param f
     * @param s
     * @return
     */
    public static String isNull(String f, String s) {
        if (f == null) return s;
        return f;
    }

	public static String veryShortDate(TZDate date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.setTimeZone(date.getTz());
		return cal.get(Calendar.DATE) + "." + (cal.get(Calendar.MONTH) + 1) + " "
			+ cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
	}

	public static String toDate(TZDate date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.setTimeZone(date.getTz());
		return cal.get(Calendar.DATE) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.YEAR);
	}

	public static String toDateTime(TZDate date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.setTimeZone(date.getTz());
		return cal.get(Calendar.DATE) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.YEAR) + 
			" " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
	}
}
