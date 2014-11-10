package com.FitAndSocial.app.fragment;

/**
 * Created by mint on 31-7-14.
 */
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.util.Utils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GoogleMapsFragment extends BaseFragment implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener{

    private GoogleMap googleMap;
    private int markerCounter = 0;
    private ArrayList<LatLng> arrayPoints = null;
    private PolylineOptions polylineOptions;
    private Location start, end;
    private Geocoder geocoder;
    private TextView startStreet;
    private TextView endStreet;
    private String startStreetString;
    private String endStreetString;
    private String completeStartStreet;
    private String completeEndStreet;
    private String fullAddress;
    private String streetName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        View view = inflater.inflate(R.layout.google_maps, container, false);
        arrayPoints = new ArrayList<>();
        geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
        startStreet = (TextView)view.findViewById(R.id.start_point_street);
        endStreet = (TextView)view.findViewById(R.id.end_point_street);
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
            polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.RED);
            polylineOptions.width(5);
            arrayPoints.add(point);
            polylineOptions.addAll(arrayPoints);
            googleMap.addPolyline(polylineOptions);
            if(markerCounter == 1){
                fullAddress = getStreetNameIfAvailable(point.latitude, point.longitude, true);
                streetName = getStreetNameIfAvailable(point.latitude, point.longitude, false);

                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                marker.title(fullAddress);

                startStreet.setText(streetName);

                startStreetString = streetName;
                completeStartStreet = fullAddress;
            }
            if(markerCounter == 2){

                fullAddress = getStreetNameIfAvailable(point.latitude, point.longitude, true);
                streetName = getStreetNameIfAvailable(point.latitude, point.longitude, false);

                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                marker.title(getStreetNameIfAvailable(point.latitude, point.longitude, true));

                endStreet.setText(getStreetNameIfAvailable(point.latitude, point.longitude, false));

                endStreetString = streetName;
                completeEndStreet = fullAddress;

                showChosenDistance();
            }
            googleMap.addMarker(marker);
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
        startStreet.setText("");
        endStreet.setText("");
    }

    public List<String> getStreetInfo(){
        List<String> streetNames = new ArrayList<>();
        streetNames.add(startStreetString);
        streetNames.add(endStreetString);
        streetNames.add(completeStartStreet);
        streetNames.add(completeEndStreet);
        return streetNames;
    }

    private String getStreetNameIfAvailable(double latitude, double longitude, boolean isCompleteAddress){
        String finalAddress;
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(addresses != null && addresses.size() > 0){
                Address address = addresses.get(0);
                StringBuilder sb = new StringBuilder();
                if(isCompleteAddress){
                    sb.append(address.getAddressLine(0)).append(", ").append(address.getAddressLine(1));
                    finalAddress = sb.toString();
                    return finalAddress;
                }
                sb.append(address.getAddressLine(0));
                finalAddress = sb.toString();
                return finalAddress;
            }
        }catch (IOException e){
            Log.e("GoogleMapsFragment", "IOException");
            Log.i("GoogleMapsFragment", "IOException", e.getCause());
        }
        finalAddress = "Address not available";
        return finalAddress;
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
}