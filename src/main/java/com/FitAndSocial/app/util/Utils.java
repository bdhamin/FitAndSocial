package com.FitAndSocial.app.util;

import android.text.format.DateFormat;
import android.text.format.Time;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by mint on 12-7-14.
 */
public final class Utils {

    private Utils(){}

    public static String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toString();
    }

    /**
     *
     * @param dayOfMonth
     * @param month
     * @param year
     * @return formatted date
     */

    public static String formatDate(int dayOfMonth, int month, int year){
        Time activityDate = new Time();
        activityDate.set(dayOfMonth, month, year);
        long dateToMillis = activityDate.toMillis(true);
        String dateFormatter = DateFormat.format("dd-MM-yyyy", dateToMillis).toString();
        return dateFormatter;
    }

    public static String formatTime(int hour, int minute){

        final String AM = "AM";
        final String PM = "PM";
        String timeSet = "";
        String hourToString = String.valueOf(hour);
        String minuteToString = String.valueOf(minute);
        String minutes = "";

        if(hour > 12){
            timeSet = PM;
        }else if(hour == 0){
            hourToString = "0" + hour;
            timeSet = AM;
        }else if(hour == 12){
            timeSet = PM;
        }else if(hour < 10) {
            hourToString = "0" + hour;
        }else{
            timeSet = AM;
        }


        if(minute < 10){
            minutes = "0" + minute;
        }else{
            minutes = minuteToString;
        }

        String formattedTime = new StringBuilder().append(hourToString).append(":").append(minutes).append(" ").append(timeSet).toString();
        return formattedTime;
    }




}
