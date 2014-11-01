package com.FitAndSocial.app.util;

import android.text.format.DateFormat;
import android.text.format.Time;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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


    public static long convertDateStringToLong(String sDate){
        java.text.DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = df.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }


    public static long convertTimeStringToLong(String sTime){
        java.text.DateFormat df = new SimpleDateFormat("HH:mm");
        Date date;
        long activityTime = 1L;
        try {
            date = df.parse(sTime);
            activityTime = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return activityTime;
    }

    /**
     *
     * @param value
     * @return an array contains the min and max value
     * separated by "-"
     */
    public static String[] parseSelectedValues(String value){
        value = value.replaceAll("[^\\d-]", "");
        String[] parts = value.split("-");
        return parts;
    }


    public static String getDateFromLong(long date){
        String pattern = "dd-MM-yyyy";
        java.text.DateFormat dateFormat = new SimpleDateFormat(pattern);
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    public static String getTimeFromLong(long date){
        String pattern = "HH:mm";
        java.text.DateFormat dateFormat = new SimpleDateFormat(pattern);
        String formattedTime = dateFormat.format(date);
        return formattedTime;
    }

    public static double round(double value, int places){
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }



}
