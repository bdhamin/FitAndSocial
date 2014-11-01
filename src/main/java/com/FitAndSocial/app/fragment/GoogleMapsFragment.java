package com.FitAndSocial.app.fragment;

/**
 * Created by mint on 31-7-14.
 */
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.util.Utils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;

import java.util.ArrayList;

public class GoogleMapsFragment extends BaseFragment implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener{

    private GoogleMap googleMap;
    private int markerCounter = 0;
    private ArrayList<LatLng> arrayPoints = null;
    private PolylineOptions polylineOptions;
    private Location start, end;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        View view = inflater.inflate(R.layout.google_maps, container, false);
        arrayPoints = new ArrayList<>();
        try{
            loadMapsIfNeeded();
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    private void loadMapsIfNeeded() {
        if(googleMap == null){
            googleMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.google_maps)).getMap();
            if(googleMap != null){
                setUpMap();
            }
        }
    }

    private void setUpMap(){
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);
    }


    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        setUserVisibleHint(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMapsIfNeeded();
    }

    @Override
    public void onMapClick(LatLng point) {
        if(markerCounter < 2){
            markerCounter++;
            MarkerOptions marker = new MarkerOptions();
            marker.position(point);
            googleMap.addMarker(marker);
            polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.RED);
            polylineOptions.width(5);
            arrayPoints.add(point);
            polylineOptions.addAll(arrayPoints);
            googleMap.addPolyline(polylineOptions);
            if(markerCounter == 2){
                showChosenDistance();
            }
        }else{
            Toast.makeText(getActivity(), "Long click on the map to reset the marker",Toast.LENGTH_LONG).show();
        }
    }

    private void showChosenDistance() {
        start = new Location("Start Point");
        end = new Location("End Point");

        start.setLatitude(arrayPoints.get(0).latitude);
        start.setLongitude(arrayPoints.get(0).longitude);

        end.setLatitude(arrayPoints.get(1).latitude);
        end.setLongitude(arrayPoints.get(1).longitude);

        double distance = start.distanceTo(end);
        double distanceInKm = distance/1000;

        Toast.makeText(getActivity(), "Distance is: " + Utils.round(distanceInKm, 2), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapLongClick(LatLng point) {
        googleMap.clear();
        arrayPoints.clear();
        markerCounter = 0;
    }

    public ArrayList<LatLng> getChosenLocations(){
        return arrayPoints;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (googleMap != null)
            setUpMap();

        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.google_maps)).getMap();
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
                    .remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.google_maps)).commit();
            googleMap = null;
        }
    }

}