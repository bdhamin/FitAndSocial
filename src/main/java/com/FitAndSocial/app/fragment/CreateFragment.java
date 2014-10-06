package com.FitAndSocial.app.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.net.URI;

/**
 * Created by mint on 13-7-14.
 */
public class CreateFragment extends BaseFragment implements OnDateSetListener, OnTimeSetListener {

    private View view;
    private String createEvent = "http://192.168.2.9:9000/createActivity";
    private TextView title;
    private String activityType;
    private String activityDistance;
    private String activityDuration;
    private String date;
    private String time;
    private ProgressDialog pDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        view = inflater.inflate(R.layout.create, container, false);
        addRequiredFragments();
        title = (TextView)view.findViewById(R.id.title);
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
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setCallBack(this);
        datePickerFragment.show(getActivity().getSupportFragmentManager(), "Date Picker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        TextView dateField = (TextView)view.findViewById(R.id.select_date_button);
        String formattedDate = Utils.formatDate(day, month, year);
        dateField.setText(formattedDate);
        date = formattedDate;
    }

    private void showTimePicker(){
        TimePickerFragment time = new TimePickerFragment();
        time.setOnTimeSetListener(this);
        time.show(getActivity().getSupportFragmentManager(), "Time Picker");
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        TextView timeField = (TextView)view.findViewById(R.id.select_time_button);
        String formattedTime = Utils.formatTime(hour, minute);
        timeField.setText(formattedTime);
        time = formattedTime;
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
                activityType = spinner.getItemAtPosition(position).toString();
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
                activityDistance = spinner.getItemAtPosition(position).toString();
                activityDistance = activityDistance.replaceAll("\\D+", "");
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
                activityDuration = spinner.getItemAtPosition(position).toString();
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
                boolean check = requiredFieldsOk();
                if(check){
                    new CreateEvent().execute(createEvent);
                }else{
                    Toast.makeText(getActivity(), "Please make sure you have filled in all the fields.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean requiredFieldsOk() {

        System.out.println("Activity Distance: "+activityDistance);


        if(!title.getText().toString().trim().equals("") && !activityType.equals("Activity Type")
                && !activityDistance.equals("Distance (KM)") && !activityDistance.trim().equals("") && !activityDuration.equals("Duration")
                && !date.equals("Set Date") && !date.equals("") && !time.equals("Set Time") && !time.equals("")){
            return true;
        }
        return false;
    }

    private void disposeCurrentFragment(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.create_activity_container);
        if(currentFragment != null){
            transaction.remove(currentFragment);
            transaction.commit();
        }
    }

    private class CreateEvent extends AsyncTask<String, Void, Boolean>{

        Event event = new Event();
        String[] duration = Utils.parseSelectedValues(activityDuration);

        @Override
        protected void onPreExecute(){
            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle("Progressing your request");
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... Url){
            Gson gson = new Gson();
            String authenticationKey = getLoggedInUserId();

            if (authenticationKey.trim().isEmpty()) {
                pDialog.dismiss();
                System.out.println("NO LOGGED IN USER FOUND!");
                return false;
            }

            event.setTitle(title.getText().toString());
            event.setType(activityType);
            event.setDurationMin(Integer.valueOf(duration[0]));
            event.setDurationMax(Integer.valueOf(duration[1]));
            event.setDistance(Integer.valueOf(activityDistance));
            event.setActivityDate(Utils.convertDateStringToLong(date));
            event.setActivityTime(Utils.convertTimeStringToLong(time));
            event.setUser(authenticationKey);
            event.setStartLocationLatitude(10);
            event.setStartLocationMagnitude(20);
            event.setEndLocationLatitude(10);
            event.setEndLocationMagnitude(20);


            String json = gson.toJson(event);

            try{
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Url[0]);
                StringEntity stringEntity = new StringEntity(json);
                httpPost.setEntity(stringEntity);
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpClient.execute(httpPost);
                StatusLine statusLine = httpResponse.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    pDialog.dismiss();
                    return true;
                }else{
                    pDialog.dismiss();
                    System.out.println("Something went wrong while creating activity");
                    return false;
                }
            }catch (Exception e){
                pDialog.dismiss();
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success){
            getActivity().getSupportFragmentManager().popBackStack();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            Fragment lastActivity = fragmentManager.findFragmentById(R.id.last_activity_fragment_container);

            if(lastActivity != null){
                Activities activities = new Activities();
                transaction.add(R.id.activities_container, activities);
                transaction.remove(lastActivity);
                transaction.commit();
            }
            disposeCurrentFragment();

            if(success){
                Toast.makeText(getActivity(), "Activity Created Successfully!", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(), "Error occurred while trying to create the activity", Toast.LENGTH_LONG).show();
            }
        }
    }
}