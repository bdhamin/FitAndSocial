package com.FitAndSocial.app.socialLogin.facebook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import com.FitAndSocial.app.fragment.Account;
import com.FitAndSocial.app.fragment.BaseFragment;
import android.view.View;
import com.FitAndSocial.app.fragment.helper.AccountContainerManager;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.util.ApplicationConstants;
import com.facebook.*;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by mint on 26-9-14.
 */
public class FacebookLogin extends BaseFragment{

    private View view;
    private static final String TAG = "FacebookLogin";
    private UiLifecycleHelper uiHelper;
    private TextView userInfoTextView;
    private AccountContainerManager accountContainerManager;
    private final String USER_LOCATION = "user_location";
    private final String USER_BIRTHDAY = "user_birthday";
    private final String USER_LIKES = "user_likes";
    private final String USER_EMAIL = "email";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle){
        view = inflater.inflate(R.layout.facebook, container, false);
        userInfoTextView = (TextView) view.findViewById(R.id.userInfoTextView);
        LoginButton loginButton = (LoginButton)view.findViewById(R.id.authButton);
        loginButton.setReadPermissions(Arrays.asList(USER_LOCATION, USER_BIRTHDAY, USER_LIKES, USER_EMAIL));
        loginButton.setFragment(this);
        return view;
    }

    private Session.StatusCallback callback = new Session.StatusCallback(){

        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private void onSessionStateChange(Session session, final SessionState state, Exception exception){
        if(state.isOpened()){
            Log.d(TAG, "Logged in...");
            userInfoTextView.setVisibility(View.VISIBLE);
            applicationPreference = getActivity().getSharedPreferences(ApplicationConstants.APPLICATION_PREFERENCE, Context.MODE_PRIVATE);

            // Request user data and show the results
            Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

                @Override
                public void onCompleted(GraphUser user, Response response) {
                   accountContainerManager.processLoggedInFacebookUser(user);
                }
            });

        }else if(state.isClosed()){
            Log.d(TAG, "Logged out...");
            accountContainerManager.processLogoutUser();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);

    }

    @Override
    public void onResume() {
        super.onResume();

        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    public void setFragment(Fragment fragment){
        try{
            this.accountContainerManager = (AccountContainerManager) fragment;
        }catch (ClassCastException e){
            System.out.println("Class Cast Exception");
        }
    }


}
