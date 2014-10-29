package com.FitAndSocial.app.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
    private int activityPosition;

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
        this.activityPosition = bundle.getInt("position");
        System.out.println("position: " + activityPosition);
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
//                SearchResultFragment searchResultFragment = (SearchResultFragment)
//                        getActivity().getSupportFragmentManager().findFragmentById(R.id.create_fragment_container);
//                if(searchResultFragment != null){
//                    searchResultFragment.removeActivityFromList(activityPosition);
//                }
                disposeFragment();
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
}