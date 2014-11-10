package com.FitAndSocial.app.fragment;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import java.util.Calendar;

/**
 * Created by mint on 19-7-14.
 */
public class DatePickerFragment extends DialogFragment{

    private OnDateSetListener onDateSet;

    public DatePickerFragment(){}

    public void setCallBack(OnDateSetListener onDate){
        onDateSet = onDate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),onDateSet, year, month, day);
    }

}
