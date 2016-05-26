package com.zekisanmobile.petsitter2.util;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

    public static String formattedDateForAPI(Date date) {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static String formattedDateForView(Date date) {
        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    public static String formattedStringDateToView(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date input = inputFormat.parse(date);
            return outputFormat.format(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String formattedDatePeriodForView(Date dateStart, Date dateFinal) {
        return formattedDateForView(dateStart)
                + " - "
                + formattedDateForView(dateFinal);
    }

    public static String formattedTimePeriodForView(String timeStart, String timeFinal) {
        return timeStart + " - " + timeFinal;
    }
}
