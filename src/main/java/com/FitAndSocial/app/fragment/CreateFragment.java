package com.FitAndSocial.app.fragment;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.*;
import com.FitAndSocial.app.fragment.helper.EventHelperService;
import com.FitAndSocial.app.mobile.R;
import android.view.View;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import com.FitAndSocial.app.model.Event;
import com.FitAndSocial.app.util.ApplicationConstants;
import com.FitAndSocial.app.util.Utils;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mint on 13-7-14.
 */
public class CreateFragment extends BaseFragment implements OnDateSetListener, OnTimeSetListener {

    private View view;
    private TextView title;
    private String activityType;
    private String activityDistance;
    private String activityDuration;
    private String date;
    private String time;
    private GoogleMapsFragment googleMapsFragment;
    private double startLat;
    private double startLng;
    private double endLat;
    private double endLng;
    private String startStreet;
    private String endStreet;
    private String completeStartStreet;
    private String completeEndStreet;


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
        setFragmentTitle(ApplicationConstants.FRAGMENT_TITLE_CREATE_NEW_ACTIVITY);
        return view;
    }

    private void addRequiredFragments() {
        googleMapsFragment = new GoogleMapsFragment();
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
                    getChosenLocations();
                    processStreetNames();
                    Event event = createEvent();
                    Intent eventHelper = new Intent(CreateFragment.this.getActivity(), EventHelperService.class);
                    eventHelper.putExtra(ApplicationConstants.EVENT_TYPE, ApplicationConstants.EVENT_TYPE_CREATE_ACTION);
                    eventHelper.putExtra("eventModel", event);
                    CreateFragment.this.getActivity().startService(eventHelper);
                    new CreateEvent().execute();
                }else{
                    Toast.makeText(getActivity(), "Please make sure you have filled in all the fields.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Contains a list of names of the chosen start and end locations
     * The following indexes describe the values of the list
     * 0 = startStreet
     * 1 = endStreet
     * 2 = fullStartStreet
     * 3= fullEndStreet
     */

    private void processStreetNames(){
        List<String> streetNames = googleMapsFragment.getStreetInfo();
        if(streetNames != null && streetNames.size() > 0){
//            && streetNames.size() == 4
            startStreet = streetNames.get(0);
            endStreet = streetNames.get(1);
            completeStartStreet = streetNames.get(2);
            completeEndStreet = streetNames.get(3);
        }
    }

    private void getChosenLocations(){
        ArrayList<LatLng> locations = googleMapsFragment.getChosenLocations();
        if(locations != null && locations.size() > 0){
            startLat = locations.get(0).latitude;
            startLng = locations.get(0).longitude;
            endLat = locations.get(1).latitude;
            endLng = locations.get(1).longitude;
        }
    }

    private boolean requiredFieldsOk() {
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

    //TODO: remove empty value to final String or remove it all since its not really needed
    private Event createEvent(){
        Event event = new Event();
        String[] duration = Utils.parseSelectedValues(activityDuration);
        String authenticationKey = getLoggedInUserId();
        event.setTitle(title.getText().toString());
        event.setType(activityType);
        event.setDurationMin(Integer.valueOf(duration[0]));
        event.setDurationMax(Integer.valueOf(duration[1]));
        event.setDistance(Integer.valueOf(activityDistance));
        event.setActivityDate(Utils.convertDateStringToLong(date));
        event.setActivityTime(Utils.convertTimeStringToLong(time));
        event.setUser(authenticationKey);
        event.setStartLocationLatitude(startLat);
        event.setStartLocationMagnitude(startLng);
        event.setEndLocationLatitude(endLat);
        event.setEndLocationMagnitude(endLng);
        event.setStartStreetName(startStreet.equals("")? "" : startStreet);
        event.setEndStreetName(endStreet.equals("")? "" : endStreet);
        event.setCompleteStartAddress(completeStartStreet.equals("")? "" : completeStartStreet);
        event.setCompleteEndAddress(completeEndStreet.equals("")? "" : completeEndStreet);
        return event;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Fragment googleMap = getFragmentManager().findFragmentById(R.id.google_maps);
        if(googleMap !=null ){
            getActivity().getSupportFragmentManager().beginTransaction()
                .remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.google_maps)).commit();
        }
    }

    private class CreateEvent extends AsyncTask<String, Void, Boolean>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... Url){
            return true;
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
        }
    }
}