package com.gmail.mostafa.ma.saleh.yawa.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mostafa on 11/11/2017.
 */

public class StringUtils {
    private StringUtils() {
    }

    public static String formatCurrentDate(String format) {
        return formatDate(format, System.currentTimeMillis());
    }

    public static String formatDate(String format, Date date) {
        return formatDate(format, date.getTime());
    }

    public static String formatDate(String format, long date) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(date);
    }

    public static String format(String pattern, Object... args) {
        return String.format(Locale.getDefault(), pattern, args);
    }
}
