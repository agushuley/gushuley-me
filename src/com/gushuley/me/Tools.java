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
		return rightPad("" + cal.get(Calendar.DATE), 2, '0') + "." + rightPad("" + (cal.get(Calendar.MONTH) + 1), 2, '0') + "." + rightPad("" + cal.get(Calendar.YEAR), 4, '0');
	}

	public static String toDateTime(TZDate date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.setTimeZone(date.getTz());
		return rightPad("" + cal.get(Calendar.DATE), 2, '0') + "." + rightPad("" + (cal.get(Calendar.MONTH) + 1), 2, '0') + "." + rightPad("" + cal.get(Calendar.YEAR), 4, '0') + 
			" " + rightPad("" + cal.get(Calendar.HOUR_OF_DAY), 2, '0') + ":" + rightPad("" + cal.get(Calendar.MINUTE), 2, '0');
	}
	
	public static String rightPad(final String original, final int len, final char aChar) {
		final StringBuffer b = new StringBuffer();
		for (int i = 0; i < Math.max(0, len - original.length()); i++) {
			b.append(aChar);
		}
		b.append(original);
		return b.toString();
	}
}
