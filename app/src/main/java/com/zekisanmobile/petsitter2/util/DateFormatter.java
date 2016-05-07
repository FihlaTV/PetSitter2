package com.zekisanmobile.petsitter2.util;

import java.text.Format;
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

    public static String formattedDatePeriodForView(Date dateStart, Date dateFinal) {
        return formattedDateForView(dateStart)
                + " - "
                + formattedDateForView(dateFinal);
    }

    public static String formattedTimePeriodForView(String timeStart, String timeFinal) {
        return timeStart + " - " + timeFinal;
    }
}
