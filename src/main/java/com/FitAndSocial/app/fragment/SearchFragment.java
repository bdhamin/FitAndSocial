package com.FitAndSocial.app.fragment;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.*;
import android.view.View;
import com.FitAndSocial.app.fragment.helper.NonSwipeableViewPager;
import com.FitAndSocial.app.mobile.FitAndSocial;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.util.Utils;
import com.actionbarsherlock.app.ActionBar;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by mint on 25-7-14.
 */
public class SearchFragment extends BaseFragment implements OnDateSetListener, OnTimeSetListener{

    private View view;
    private String activityTypeName;
    private String distance;
    private String duration;
    private String radius;
    private String startDate;
    private String startTime;
    private final String KEY_ACTIVITY_TYPE = "activityType";
    private final String KYE_DISTANCE = "distance";
    private final String KEY_Min_DURATION = "durationMin";
    private final String KEY_Max_DURATION = "durationMax";
    private final String KEY_Min_RADIUS = "radiusMin";
    private final String KEY_Max_RADIUS = "radiusMax";
    private final String KEY_START_DATE = "startDate";
    private final String KEY_START_TIME = "time";
    private String urlAddress = "http://192.168.2.9:9000/searchActivities?";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        view = inflater.inflate(R.layout.search, container, false);
        removeUnnecessaryFragments();
        activityTypeSpinnerListener();
        activityDistanceSpinnerListener();
        activityDurationSpinnerListener();
        activityRadiusSpinnerListener();
        showDateDialogListener();
        showTimeDialogListener();
        searchActivityButtonListener();
        enableViewPagerSwipe(false);
        setActionbarNavigationMode(0);
        setFragmentTitle("Search");
        return view;
    }

    private void removeUnnecessaryFragments() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment noActivities = fragmentManager.findFragmentById(R.id.no_activities_fragment_container);

        if(noActivities != null){
            fragmentTransaction.remove(noActivities);
            fragmentTransaction.commit();
        }
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
        String formattedDate = Utils.formatDate(day, month, year);
        startDate = formattedDate;
        System.out.println(formattedDate);
        date.setText(formattedDate);
    }

    private void showTimePicker(){
        TimePickerFragment time = new TimePickerFragment();
        time.setOnTimeSetListener(this);
        time.show(getActivity().getSupportFragmentManager(), "Time Picker");
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        TextView time = (TextView)view.findViewById(R.id.select_time_button);
        startTime = Utils.formatTime(hour, minute);
        startTime = startTime.replaceAll("[^\\d:]", "");
        time.setText(Utils.formatTime(hour, minute));
    }

    @Override
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
                activityTypeName = spinner.getItemAtPosition(position).toString();
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
                distance = spinner.getItemAtPosition(position).toString();

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
                duration = spinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void activityRadiusSpinnerListener() {
        final Spinner spinner = (Spinner) view.findViewById(R.id.activity_radius_spinner);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position);
                radius = spinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void searchActivityButtonListener() {
        final Button search = (Button)view.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!activityTypeName.equals("Activity Type") && !distance.equals("Distance (KM)")
                        && !duration.equals("Duration") && !radius.equals("Radius")){

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Fragment searchFragment = fragmentManager.findFragmentById(R.id.create_fragment_container);
                        SearchResultFragment searchResultFragment = new SearchResultFragment(assembleUrl());
                        fragmentTransaction.remove(searchFragment);
                        fragmentTransaction.add(R.id.activities_container, searchResultFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                }else{
                    Toast.makeText(getActivity(), "All the search fields are required!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void disposeCurrentFragment(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.create_activity_container);
        if(currentFragment != null){
            transaction.remove(currentFragment);
            transaction.commit();
        }
    }

    /**
     *
     * @return a string represents the full address to the backend.
     * The final address look something like the following
     * http://localhost:9000/searchActivities?activityType=
     * Cycling&distance=7&durationMin=20&durationMax=30&radiusMin=20&
     * radiusMax=30&startDate=16-8-2014&time=12:58
     */
    private String assembleUrl(){

        String minDuration = "";
        String maxDuration = "";
        String minRadius = "";
        String maxRadius = "";

        /**
         * Duration has start and end value so we need to extract
         * the value in min and max value which represent from to
         */
        String[] durationParts = Utils.parseSelectedValues(duration);
        minDuration = durationParts[0];
        maxDuration = durationParts[1];

        /**
         * Duration has start and end value so we need to extract
         * the value in min and max value which represent from to
         */
        String[] radiusParts = Utils.parseSelectedValues(duration);
        minRadius = radiusParts[0];
        maxRadius = radiusParts[1];

        /**
         * Trim and remove the KM letters from distance
         */
        distance = distance.replaceAll("\\D+", "");

        String typeName = "";
        try {
            typeName = URLEncoder.encode(activityTypeName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Invalid Encoding Exception!");
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(urlAddress).append(KEY_ACTIVITY_TYPE).append("=").append(typeName).append("&")
                .append(KYE_DISTANCE).append("=").append(distance).append("&")
                .append(KEY_Min_DURATION).append("=").append(minDuration).append("&")
                .append(KEY_Max_DURATION).append("=").append(maxDuration).append("&")
                .append(KEY_Min_RADIUS).append("=").append(minRadius).append("&")
                .append(KEY_Max_RADIUS).append("=").append(maxRadius).append("&")
                .append(KEY_START_DATE).append("=").append(startDate).append("&")
                .append(KEY_START_TIME).append("=").append(startTime);

        return sb.toString();
    }
}