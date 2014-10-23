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

    public FacebookLogin(){}

    public FacebookLogin(AccountContainerManager accountContainerManager){
        this.accountContainerManager = accountContainerManager;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle){
        view = inflater.inflate(R.layout.facebook, container, false);
        userInfoTextView = (TextView) view.findViewById(R.id.userInfoTextView);
        LoginButton loginButton = (LoginButton)view.findViewById(R.id.authButton);
        loginButton.setReadPermissions(Arrays.asList("user_location", "user_birthday", "user_likes", "email"));
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
            applicationPreference = getActivity().getSharedPreferences(APPLICATION_PREFERENCE, Context.MODE_PRIVATE);

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


    private String buildUserInfoDisplay(GraphUser user) {
        StringBuilder userInfo = new StringBuilder("");

        // Example: typed access (name)
        // - no special permissions required
        userInfo.append(String.format("Name: %s\n\n",
                user.getName()));

        // Example: typed access (birthday)
        // - requires user_birthday permission
        userInfo.append(String.format("Birthday: %s\n\n",
                user.getBirthday()));

        if(user.getProperty("gender") != null){
            userInfo.append(String.format("Gender: %s\n\n", user.getProperty("gender")));
        }

        // Example: partially typed access, to location field,
        // name key (location)
        // - requires user_location permission
        if(user.getLocation() != null){
            userInfo.append(String.format("Location: %s\n\n",
                    user.getLocation().getProperty("name")));
        }


        // Example: access via property name (locale)
        // - no special permissions required
        if(user.getProperty("locale") != null){
            userInfo.append(String.format("Locale: %s\n\n",
                    user.getProperty("locale")));
        }

        if(user.getId() != null){
            userInfo.append(String.format("UserId: %s\n\n", user.getId()));
        }

        if(user.getProperty("email") != null){
            userInfo.append(String.format("Email: %s\n\n", user.getProperty("email")));
        }


        // Example: access via key for array (languages)
        // - requires user_likes permission
        if(user.getProperty("languages") != null) {
            JSONArray languages = (JSONArray) user.getProperty("languages");
            if (languages.length() > 0) {
                ArrayList<String> languageNames = new ArrayList<String>();
                for (int i = 0; i < languages.length(); i++) {
                    JSONObject language = languages.optJSONObject(i);
                    // Add the language name to a list. Use JSON
                    // methods to get access to the name field.
                    languageNames.add(language.optString("name"));
                }
                userInfo.append(String.format("Languages: %s\n\n",
                        languageNames.toString()));
            }
        }

        return userInfo.toString();
    }









}
