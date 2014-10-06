package com.FitAndSocial.app.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.FitAndSocial.app.mobile.R;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.NameValuePair;
//import org.apache.http.StatusLine;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by mint on 31-7-14.
 */
public class ActivityInformationFragment extends BaseFragment{

    private View view;
    private HashMap<String, String> selectedSearchResult;

    private String participateUrl = "http://192.168.2.9:9000/participationRequest";
    private String cancelParticipationUrl = "http://192.168.2.9:9000/cancelParticipation";
    private final String KEY_ACTIVITY_ID = "id";
    private final String KEY_TITLE = "title";
    private final String KEY_TYPE = "type";
    private final String KEY_DISTANCE = "distance";
    private final String KEY_DURATION = "duration";
    private final String KEY_DATE = "date";
    private final String KEY_TIME = "time";
    private final String KEY_MEMBERS_TOTAL="members_total";

    private TextView title;
    private TextView type;
    private TextView distance;
    private TextView duration;
    private TextView date;
    private TextView time;
    private TextView membersTotal;

    private ProgressDialog pDialog;
    private boolean isParticipation;

    private TextView participationButton;


    /**
     *
     * @param selectedSearchResult
     * @param isParticipation
     * First parameter contains the selected activity information and the
     * second one is a boolean to determine if which action should be triggered
     * when clicking the button.
     * When true it means its a participation where as false means cancel participation
     */
    public ActivityInformationFragment(HashMap<String, String> selectedSearchResult, boolean isParticipation){
        this.selectedSearchResult = selectedSearchResult;
        this.isParticipation = isParticipation;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        view = inflater.inflate(R.layout.activity_information, container, false);
        enableViewPagerSwipe(false);
        setActionbarNavigationMode(0);
        setFragmentTitle("Event Details");
        initTextViews();
        initEventDetails();
        loadRequiredFragments();
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
        if(!isParticipation){
            participationButton = (TextView)view.findViewById(R.id.participate_button);
            participationButton.setText("Cancel Participation");
        }
    }

    private void initEventDetails() {

        title.setText(selectedSearchResult.get(KEY_TITLE));
        type.setText(selectedSearchResult.get(KEY_TYPE));
        distance.setText(selectedSearchResult.get(KEY_DISTANCE));
        duration.setText(selectedSearchResult.get(KEY_DURATION));
        date.setText(selectedSearchResult.get(KEY_DATE));
        time.setText(selectedSearchResult.get(KEY_TIME));
        membersTotal.setText("members " + selectedSearchResult.get(KEY_MEMBERS_TOTAL));
    }

    private void attachButtonListener() {
        Button participateButton = (Button)view.findViewById(R.id.participate_button);
        participateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isParticipation){
                    new SendParticipationRequest().execute(participateUrl);
                }else{
                    new SendParticipationRequest().execute(cancelParticipationUrl);
                }
            }
        });
    }

    private void loadRequiredFragments() {
//        Activities activityInfo = new Activities();
//        activityInfo.isActivityInformation(true);
        GoogleMapsFragment maps = new GoogleMapsFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.add(R.id.activities_info_container, activityInfo);
        transaction.add(R.id.activity_info_map_container, maps);
        transaction.commit();
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

    private class SendParticipationRequest extends AsyncTask<String, Void, Boolean>{

        @Override
        protected void onPreExecute(){
            String participate = "Participate";
            String cancelParticipation = "Cancelling Participation";
            String createMessage = "Saving...";
            String updateMessage = "Updating...";


            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle(isParticipation? participate : cancelParticipation);
            pDialog.setMessage(isParticipation? createMessage : updateMessage);
            pDialog.setIndeterminate(true);
            pDialog.show();
        }


        @Override
        protected Boolean doInBackground(String... Url) {

            String authenticationKey = getLoggedInUserId();
            if(authenticationKey.trim().isEmpty()){
                pDialog.dismiss();
                System.out.println("User Id Cannot Be Empty");
                return false;
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("activityId", Long.valueOf(selectedSearchResult.get("id")));
                jsonObject.accumulate("userId", authenticationKey);
                StringEntity stringEntity = new StringEntity(jsonObject.toString());

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Url[0]);
                httpPost.setEntity(stringEntity);
                httpPost.setHeader("Content-type", "application/json");

                HttpResponse httpResponse = httpClient.execute(httpPost);
                StatusLine statusLine = httpResponse.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    pDialog.dismiss();
                    return true;
                }else{
                    pDialog.dismiss();
                    return false;
                }
            } catch (JSONException|UnsupportedEncodingException|ClientProtocolException e) {
                e.printStackTrace();
                pDialog.dismiss();
                return false;

            }catch (IOException ioException){
                pDialog.dismiss();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success){

            String createParticipation = "Participation Success";
            String cancelParticipation = "Successfully removed participation";


            if(success){
                Toast.makeText(getActivity(), isParticipation? createParticipation : cancelParticipation, Toast.LENGTH_SHORT).show();
                disposeFragment();
            }else{
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                disposeFragment();
            }
        }

    }
}