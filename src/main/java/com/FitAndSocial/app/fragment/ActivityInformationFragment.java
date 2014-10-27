package com.FitAndSocial.app.fragment;

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
import com.FitAndSocial.app.util.ApplicationConstants;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    private ProgressDialog pDialog;
    private boolean isParticipation;

    private TextView participationButton;
    private final String ACTIVITY_ID = "activityId";
    private final String USER_ID = "userId";
    private final String CANCEL_PARTICIPATION = "Cancel Participation";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        view = inflater.inflate(R.layout.activity_information, container, false);
        enableViewPagerSwipe(false);
        setActionbarNavigationMode(0);
        setFragmentTitle(ApplicationConstants.FRAGMENT_TITLE_ACTIVITY_DETAILS);
        Bundle bundle = this.getArguments();
        this.selectedSearchResult = (HashMap<String, String>)bundle.getSerializable("activity");
        this.isParticipation = bundle.getBoolean("participation");
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
    }

    private void attachButtonListener() {
        Button participateButton = (Button)view.findViewById(R.id.participate_button);
        participateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isParticipation){
                    String participateUrl = ApplicationConstants.SERVER_BASE_ADDRESS+ApplicationConstants.SERVER_ADDRESS_ACTION_PARTICIPATION_REQUEST;
                    new SendParticipationRequest().execute(participateUrl);
                }else{
                    String cancelParticipationUrl = ApplicationConstants.SERVER_BASE_ADDRESS+ApplicationConstants.SERVER_ADDRESS_ACTION_CANCEL_PARTICIPATION;
                    new SendParticipationRequest().execute(cancelParticipationUrl);
                }
            }
        });
    }

    private void loadRequiredFragments() {
        GoogleMapsFragment maps = new GoogleMapsFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
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
                return false;
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate(ACTIVITY_ID, Long.valueOf(selectedSearchResult.get("id")));
                jsonObject.accumulate(USER_ID, authenticationKey);
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

            String participationSuccess = "Participation Success";
            String cancelParticipation = "Successfully removed participation";


            if(success){
                Toast.makeText(getActivity(), isParticipation? participationSuccess : cancelParticipation, Toast.LENGTH_SHORT).show();
                disposeFragment();
            }else{
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                disposeFragment();
            }
        }

    }
}