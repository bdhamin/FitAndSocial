package com.FitAndSocial.app.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import java.util.Calendar;

/**
 * Created by mint on 23-7-14.
 */
public class TimePickerFragment extends DialogFragment{

    OnTimeSetListener onTimeSetListener;

    public TimePickerFragment(){}

    public void setOnTimeSetListener(OnTimeSetListener onTimeSetListener){
        this.onTimeSetListener = onTimeSetListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceBundle){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), onTimeSetListener, hour, min, false);
    }



}
