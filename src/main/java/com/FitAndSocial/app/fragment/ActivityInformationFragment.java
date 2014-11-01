package com.FitAndSocial.app.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.FitAndSocial.app.fragment.helper.EventHelperService;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.util.ApplicationConstants;
import com.FitAndSocial.app.util.MapActivityLocationBounds;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mint on 31-7-14.
 */
public class ActivityInformationFragment extends BaseFragment{

    private View view;
    private HashMap<String, String> selectedSearchResult;
    private TextView title;
    private TextView type;
    private TextView distance;
    private TextView duration;
    private TextView date;
    private TextView time;
    private TextView membersTotal;
    private ImageView memberOne;
    private ImageView memberTwo;
    private ImageView memberThree;
    private TextView memberOneName;
    private TextView memberTwoName;
    private  TextView memberThreeName;
    private TextView members;
    private TextView showMore;
    private PolylineOptions polylineOptions;
    private boolean isParticipation;
    private int activityPosition;

    private TextView participationButton;
    private final String ACTIVITY_ID = "activityId";
    private final String USER_ID = "userId";
    private final String CANCEL_PARTICIPATION = "Cancel Participation";
    private GoogleMap googleMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        view = inflater.inflate(R.layout.activity_information, container, false);
        enableViewPagerSwipe(false);
        setActionbarNavigationMode(0);
        setFragmentTitle(ApplicationConstants.FRAGMENT_TITLE_ACTIVITY_DETAILS);
        Bundle bundle = this.getArguments();
        this.selectedSearchResult = (HashMap<String, String>)bundle.getSerializable("activity");
        this.isParticipation = bundle.getBoolean("participation");
        this.activityPosition = bundle.getInt("position");
        initTextViews();
        initEventDetails();
        loadMapsIfNeeded();
//        loadRequiredFragments();
        attachButtonListener();
        return view;
    }

    private void initTextViews() {

        title = (TextView)view.findViewById(R.id.title);
        type = (TextView)view.findViewById(R.id.typeName);
        distance = (TextView)view.findViewById(R.id.km);
        duration = (TextView)view.findViewById(R.id.dTime);
        date = (TextView)view.findViewById(R.id.aDate);
        time = (TextView)view.findViewById(R.id.aTime);
        membersTotal = (TextView)view.findViewById(R.id.members);
        memberOne = (ImageView) view.findViewById(R.id.memberOne);
        memberTwo = (ImageView) view.findViewById(R.id.memberTwo);
        memberThree = (ImageView) view.findViewById(R.id.memberThree);
        showMore = (TextView) view.findViewById(R.id.showMore);
        memberOneName = (TextView) view.findViewById(R.id.memberOneName);
        memberTwoName = (TextView) view.findViewById(R.id.memberTwoName);
        memberThreeName = (TextView) view.findViewById(R.id.memberThreeName);
        members = (TextView)view.findViewById(R.id.members);
        if(!isParticipation){
            participationButton = (TextView)view.findViewById(R.id.participate_button);
            participationButton.setText(CANCEL_PARTICIPATION);
        }
    }

    private void initEventDetails() {

        title.setText(selectedSearchResult.get(ApplicationConstants.KEY_TITLE));
        type.setText(selectedSearchResult.get(ApplicationConstants.KEY_TYPE));
        distance.setText(selectedSearchResult.get(ApplicationConstants.KEY_DISTANCE));
        duration.setText(selectedSearchResult.get(ApplicationConstants.KEY_DURATION));
        date.setText(selectedSearchResult.get(ApplicationConstants.KEY_DATE));
        time.setText(selectedSearchResult.get(ApplicationConstants.KEY_TIME));
        membersTotal.setText("members " + selectedSearchResult.get(ApplicationConstants.KEY_MEMBERS_TOTAL));
        if(selectedSearchResult.get("member_0_name") != "" && selectedSearchResult.get("member_0_name") != null){
            memberOneName.setText(selectedSearchResult.get("member_0_name"));
            memberOne.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.friends));

            if(selectedSearchResult.get("member_1_name") != "" && selectedSearchResult.get("member_1_name") != null){
                memberTwoName.setText(selectedSearchResult.get("member_1_name"));
                memberTwo.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.friends));
            }else{
                setVisibility(1);
            }
            if(selectedSearchResult.get("member_2_name") != "" && selectedSearchResult.get("member_2_name") != null){
                memberThreeName.setText(selectedSearchResult.get("member_2_name"));
                memberThree.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.friends));
                showMore.setVisibility(View.VISIBLE);
                showMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopup(selectedSearchResult);
                    }
                });
            }else{
                setVisibility(2);
            }
        }else{
            showMore.setVisibility(View.INVISIBLE);
            memberOneName.setText("");
            memberTwoName.setText("");
            memberThreeName.setText("");
            memberOne.setImageDrawable(null);
            memberTwo.setImageDrawable(null);
            memberThree.setImageDrawable(null);
        }

    }

    private void showPopup(HashMap<String, String> stringStringHashMap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Activity Members");

        ListView modeList = new ListView(getActivity());
        String[] stringArray = new String[Integer.valueOf(stringStringHashMap.get(ApplicationConstants.KEY_MEMBERS_TOTAL))];
        for(int i =0; i<Integer.valueOf(stringStringHashMap.get(ApplicationConstants.KEY_MEMBERS_TOTAL)); i++){
            stringArray[i] = stringStringHashMap.get("member_"+i+"_name");
        }
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
        modeList.setAdapter(modeAdapter);

        builder.setView(modeList);

        builder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        final Dialog dialog = builder.create();

        dialog.show();
    }

    private void setVisibility(int members){
        switch (members){
            case 0:
                break;
            case 1:
                showMore.setVisibility(View.INVISIBLE);
                memberTwoName.setText("");
                memberThreeName.setText("");
                memberTwo.setImageDrawable(null);
                memberThree.setImageDrawable(null);
                break;
            case 2:
                showMore.setVisibility(View.INVISIBLE);
                memberThreeName.setText("");
                memberThree.setImageDrawable(null);
                break;
        }
    }

    private void attachButtonListener() {
        Button participateButton = (Button)view.findViewById(R.id.participate_button);
        participateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String authenticationKey = getLoggedInUserId();
                Intent eventHelperIntent = new Intent(ActivityInformationFragment.this.getActivity(), EventHelperService.class);
                eventHelperIntent.putExtra(ACTIVITY_ID, Long.valueOf(selectedSearchResult.get("id")));
                eventHelperIntent.putExtra(USER_ID, authenticationKey);

                if(isParticipation){
                    eventHelperIntent.putExtra(ApplicationConstants.EVENT_TYPE, ApplicationConstants.EVENT_TYPE_PARTICIPATE_ACTION);
                }else{
                    eventHelperIntent.putExtra(ApplicationConstants.EVENT_TYPE, ApplicationConstants.EVENT_TYPE_PARTICIPATE_CANCEL_ACTION);
                }
                ActivityInformationFragment.this.getActivity().startService(eventHelperIntent);
                disposeFragment();
            }
        });
    }

    private void loadMapsIfNeeded() {
        if(googleMap == null) {
            googleMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.activity_info_map_container)).getMap();

            if(googleMap != null){
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        googleMap.setMyLocationEnabled(true);
        drawLineBasedOnChosenLocation(Double.parseDouble(selectedSearchResult.get(ApplicationConstants.START_LAT)),
                Double.parseDouble(selectedSearchResult.get(ApplicationConstants.START_LNG)),
                Double.parseDouble(selectedSearchResult.get(ApplicationConstants.END_LAT)),
                Double.parseDouble(selectedSearchResult.get(ApplicationConstants.END_LNG)));
    }


    public void drawLineBasedOnChosenLocation(double startLat, double startLon, double endLat, double endLon){
        LatLng startPoint = new LatLng(startLat, startLon);
        LatLng endPoint = new LatLng(endLat, endLon);
        ArrayList<LatLng> arrayPoints = new ArrayList<>();
        arrayPoints.add(startPoint);
        arrayPoints.add(endPoint);
        final MarkerOptions startPointMarker = new MarkerOptions();
        startPointMarker.position(startPoint);
        final MarkerOptions endPointMarker = new MarkerOptions();
        endPointMarker.position(endPoint);
        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.RED);
        polylineOptions.width(5);
        polylineOptions.addAll(arrayPoints);
        googleMap.addPolyline(polylineOptions);
        googleMap.addMarker(startPointMarker);
        googleMap.addMarker(endPointMarker);
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(MapActivityLocationBounds.createBoundsWithMinDiagonal(startPointMarker, endPointMarker), 5));
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (googleMap != null)
            setUpMap();

        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.activity_info_map_container)).getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null)
                setUpMap();
        }
    }

    /**** The mapfragment's id must be removed from the FragmentManager
     **** or else if the same it is passed on the next time then
     **** app will crash ****/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (googleMap != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.activity_info_map_container)).commit();
            googleMap = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        setUserVisibleHint(true);
    }

    private void disposeFragment() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment activityInformation = getActivity().getSupportFragmentManager().findFragmentById(R.id.create_fragment_container);
        if(activityInformation != null){
            transaction.remove(activityInformation);
            transaction.commit();
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}