package com.FitAndSocial.app.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.*;
import com.FitAndSocial.app.mobile.R;
import android.view.View;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import com.FitAndSocial.app.model.Event;
import com.FitAndSocial.app.util.Utils;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.StatusLine;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.net.URI;

/**
 * Created by mint on 13-7-14.
 */
public class CreateFragment extends BaseFragment implements OnDateSetListener, OnTimeSetListener {

    private View view;
    private String createEvent = "http://192.168.2.9:9000/createActivity";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        view = inflater.inflate(R.layout.create, container, false);
        addRequiredFragments();
        activityTypeSpinnerListener();
        activityDistanceSpinnerListener();
        activityDurationSpinnerListener();
        showDateDialogListener();
        showTimeDialogListener();
        createActivityButtonListener();
        enableViewPagerSwipe(false);
        setActionbarNavigationMode(0);
        setFragmentTitle("Create New Activity");
        return view;
    }

    private void addRequiredFragments() {
        GoogleMapsFragment googleMapsFragment = new GoogleMapsFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.google_map_container, googleMapsFragment);
        transaction.commit();
    }

    private void showTimeDialogListener() {
        Button selectTime = (Button)view.findViewById(R.id.select_time_button);
        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });
    }

    private void showDateDialogListener() {
        Button selectDate = (Button)view.findViewById(R.id.select_date_button);
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        date.setCallBack(this);
        date.show(getActivity().getSupportFragmentManager(), "Date Picker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        TextView date = (TextView)view.findViewById(R.id.select_date_button);
        date.setText(Utils.formatDate(day, month, year));
    }

    private void showTimePicker(){
        TimePickerFragment time = new TimePickerFragment();
        time.setOnTimeSetListener(this);
        time.show(getActivity().getSupportFragmentManager(), "Time Picker");
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        TextView time = (TextView)view.findViewById(R.id.select_time_button);
        time.setText(Utils.formatTime(hour, minute));
    }

    public void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        setUserVisibleHint(true);
    }

    private void activityTypeSpinnerListener() {
        final Spinner spinner = (Spinner) view.findViewById(R.id.activity_type_spinner);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position);
                /**
                 * To get the text value from the spinner use the following code
                 * String value = spinner.getItemAtPosition(position).toString();
                 */
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void activityDistanceSpinnerListener() {
        final Spinner spinner = (Spinner) view.findViewById(R.id.activity_distance_spinner);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               spinner.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void activityDurationSpinnerListener() {
        final Spinner spinner = (Spinner) view.findViewById(R.id.activity_duration_spinner);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void createActivityButtonListener() {
        Button create = (Button)view.findViewById(R.id.create_button);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CreateEvent().execute(createEvent);




//                Toast.makeText(getActivity(), "Activity Created", Toast.LENGTH_SHORT).show();
//                getActivity().getSupportFragmentManager().popBackStack();
//                disposeCurrentFragment();
            }
        });
    }

    private void disposeCurrentFragment(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.create_activity_container);
        String fragmentName = "NONE";
        if(currentFragment != null){
            fragmentName = currentFragment.getClass().getName();
        }
        if(currentFragment != null){
            transaction.remove(currentFragment);
            transaction.commit();
        }
    }

    private class CreateEvent extends AsyncTask<String, Void, Boolean>{

        Event event = new Event();


        @Override
        protected void onPreExecute(){
            event.setActivityTypeName("Running");
            event.setDuration(5);
            event.setDistance(10);
            event.setDate("13-10-2014");
            event.setTime("18:20");
            event.setUserId(10);

        }

        @Override
        protected Boolean doInBackground(String... Url){

//            try{
//                HttpClient httpClient = new DefaultHttpClient();
//                HttpPost httpPost = new HttpPost(Url[0]);
//                String json = "";
//
////                JSONObject object = new JSONObject();
////                object.accumulate("name", event.getActivityTypeName());
////                object.accumulate("duration", event.getDuration());
////                object.accumulate("distance", event.getDistance());
////                object.accumulate("date", event.getDate());
////                object.accumulate("time", event.getTime());
////                object.accumulate("user", event.getUserId());
////
////                json = object.toString();
//
//                StringEntity stringEntity = new StringEntity(json);
//                httpPost.setEntity(stringEntity);
//                httpPost.setHeader("Content-type", "application/json");
//
//                HttpResponse httpResponse = httpClient.execute(httpPost);
//                StatusLine statusLine = httpResponse.getStatusLine();
//                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
//                    return true;
//                }else{
//                    return false;
//                }
//
//            }catch (Exception e){
                return false;
//            }
        }

        @Override
        protected void onPostExecute(Boolean success){

            if(success){
                System.out.println("You rock!");
            }

        }



    }




}