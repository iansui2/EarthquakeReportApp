package com.appinventor.android.earthquakereportapp.variables;

import android.annotation.SuppressLint;

import com.appinventor.android.earthquakereportapp.ui.EarthquakeActivity;
import com.appinventor.android.earthquakereportapp.ui.EarthquakeAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.content.Intent;

import static android.content.Intent.getIntent;

public class Global {
    public static Date currentDate = Calendar.getInstance().getTime();
    public static String currentFormattedDate = formatCurrentDate(currentDate);
    public static String tommorowFormattedDate = formatTommorowDate();

    public static final String orderBy = "time";
    public static final int minMag = 0;
    public static final int maxMag = 10;
    public static final String startTime = currentFormattedDate;
    public static final String endTime = tommorowFormattedDate;

    private static String formatCurrentDate(Date dateObject) {
        // Format the date to month, day and year
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(dateObject);
    }

    private static String formatTommorowDate() {
        // Format the date to month, day and year
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +1);
        return dateFormat.format(calendar.getTime());
}
}
