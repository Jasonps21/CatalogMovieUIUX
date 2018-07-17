package com.example.jason.catalogmovieuiux.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {

    private static String formatDate(String date, String format) {
        String result = "";

        DateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date oldDate = oldFormat.parse(date);
            DateFormat newFormat = new SimpleDateFormat(format);
            result = newFormat.format(oldDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getLongDate(String date) {
        return formatDate(date, "E, MMM d yyyy");
    }

}
