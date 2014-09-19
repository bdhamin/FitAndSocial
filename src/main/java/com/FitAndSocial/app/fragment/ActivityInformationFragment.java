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

    private String participateUrl = "http://192.168.2.9:9000/participateRequest";

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


    public ActivityInformationFragment(HashMap<String, String> selectedSearchResult){
        this.selectedSearchResult = selectedSearchResult;
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
                new SendParticipationRequest().execute(participateUrl);
            }
        });
    }

    private String assembleUrl() {
        String activityId = selectedSearchResult.get("id");
        String userId = "228";
        StringBuilder sb = new StringBuilder();
        sb.append(participateUrl).append("activityId=").append(activityId).append("&").append("userId=").append(userId);
        return sb.toString();
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
            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle("Participate");
            pDialog.setMessage("Saving...");
            pDialog.setIndeterminate(true);
            pDialog.show();
        }


        @Override
        protected Boolean doInBackground(String... Url) {

//            List<NameValuePair> nameValuePairs = new ArrayList<>();
//            nameValuePairs.add(new BasicNameValuePair("activityId", selectedSearchResult.get("id")));
//            nameValuePairs.add(new BasicNameValuePair("userId", "231"));
//
//            HttpClient client = new DefaultHttpClient();
//            HttpPost post = new HttpPost(Url[0]);
//
//            try {
//                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//                HttpResponse response = client.execute(post);
//                StatusLine statusLine = response.getStatusLine();
//
//                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
//                    pDialog.dismiss();
//                    return true;
//                }else{
//                    pDialog.dismiss();
//                    return false;
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                pDialog.dismiss();
                return false;
//            }
        }

        @Override
        protected void onPostExecute(Boolean success){
            if(success){
                Toast.makeText(getActivity(), "Participation Success", Toast.LENGTH_SHORT).show();
                disposeFragment();
            }else{
                Toast.makeText(getActivity(), "Participation Failed", Toast.LENGTH_SHORT).show();
                disposeFragment();
            }

        }

    }
}