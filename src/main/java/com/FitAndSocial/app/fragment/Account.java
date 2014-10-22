package com.FitAndSocial.app.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.FitAndSocial.app.fragment.helper.AccountContainerManager;
import com.FitAndSocial.app.fragment.helper.CreateAccount;
import com.FitAndSocial.app.fragment.helper.CreateAccountHelper;
import com.FitAndSocial.app.fragment.helper.NonSwipeableViewPager;
import com.FitAndSocial.app.gcm.DeviceRegistration;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.socialLogin.facebook.FacebookLogin;
import com.FitAndSocial.app.socialLogin.google.GoogleLogin;
import com.facebook.model.GraphUser;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.model.people.Person;
import android.view.View.OnClickListener;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mint on 29-9-14.
 */
public class Account extends BaseFragment implements AccountContainerManager{

    private View view;
    SharedPreferences applicationPreference;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private SharedPreferences accounts;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceBundle){
        view = layoutInflater.inflate(R.layout.account, container, false);

        accounts = getActivity().getSharedPreferences(REGISTERED_USERS, Context.MODE_PRIVATE);
        applicationPreference = getActivity().getSharedPreferences(APPLICATION_PREFERENCE, Context.MODE_PRIVATE);
        editor = accounts.edit();
        manager = getFragmentManager();
        transaction = manager.beginTransaction();

        FacebookLogin facebookLogin = new FacebookLogin(this);
        GoogleLogin googleLogin = new GoogleLogin(this);

        transaction.add(R.id.facebook_login_container, facebookLogin);
        transaction.add(R.id.google_login_container, googleLogin);

        transaction.commit();
        configureTabMode();
        return view;
    }

    public void setUserVisibleHint(boolean visibleHint){
        super.setUserVisibleHint(visibleHint);
    }

    @Override
    public void processLoggedInGoogleUser(Person user, String email) {
        if(user != null && !user.getId().equals("")){
            if(!accountExist(user.getId())){
                CreateAccountHelper createAccountHelper = new CreateAccount();
                createAccountHelper.createAccountUsingGoogle(user, email);
                createAccountHelper.saveUserInfoToLocalDB(getActivity().getApplicationContext() ,user.getId(), user.getDisplayName(), new Date().getTime());
                addUserToSharedPreferences(user.getId());
                configureLoginSharedPreferences(false, user.getId());
                configureTabMode();
                new DeviceRegistration(this.getActivity());
            }else{
                processLoggedInUserInformation(user.getName().getGivenName());
                configureLoginSharedPreferences(false, user.getId());
                configureTabMode();
            }
        }
    }

    @Override
    public void processLoggedInFacebookUser(GraphUser user) {
        if(user != null && !user.getId().equals("")){
            if(!accountExist(user.getId())){
                CreateAccountHelper createAccountHelper = new CreateAccount();
                createAccountHelper.createAccountUsingFacebook(user);
                createAccountHelper.saveUserInfoToLocalDB(getActivity().getApplicationContext(), user.getId(), user.getFirstName() + " " + user.getLastName(), new Date().getTime());
                addUserToSharedPreferences(user.getId());
                configureLoginSharedPreferences(true, user.getId());
                configureTabMode();
                new DeviceRegistration(this.getActivity());
            }else{
                processLoggedInUserInformation(user.getFirstName());
                configureLoginSharedPreferences(true, user.getId());
                configureTabMode();
            }
        }
    }

    private boolean accountExist(String userId) {
        if (accounts.contains("users")) {
            Set<String> existingUsers = accounts.getStringSet("users", null);
            return existingUsers.contains(userId) ? true : false;
        }
        return false;
    }

    private void addUserToSharedPreferences(String userId){

        Set<String> users;
        if(accounts.contains("users")){
            users = accounts.getStringSet("users", null);
            users.add(userId);
        }else{
            users = new HashSet<>();
            users.add(userId);
        }
        editor.putStringSet("users", users);
        editor.commit();
    }

    private void configureLoginSharedPreferences(boolean isFacebookLogin, String userId){
        SharedPreferences.Editor sharedEditor = applicationPreference.edit();
        if(isFacebookLogin){
            sharedEditor.putString("loginType", "facebook");
        }else{
            sharedEditor.putString("loginType", "google");
        }
        sharedEditor.putString("userId", userId);
        sharedEditor.commit();
    }

    private void configureTabMode() {
        if(isLoggedIn()){
            manageLoginContainer();
            setActionbarNavigationMode(2);
            setFragmentTitle(getUsername());
            enableViewPagerSwipe(true);
        }else{
            setActionbarNavigationMode(0);
            setFragmentTitle("Account");
            enableViewPagerSwipe(false);
        }
    }

    private boolean isLoggedIn() {
        if(applicationPreference.contains("loginType")){
            String loginType = applicationPreference.getString("loginType", "");
            if(loginType != null && !loginType.equals("")){
                return true;
            }
        }
        return false;
    }

    private void manageLoginContainer() {
        String connectedWith = applicationPreference.getString("loginType", "");
        switch (connectedWith){
            case "facebook":
                view.findViewById(R.id.google_login_container).setVisibility(View.GONE);
                view.findViewById(R.id.facebook_login_container).setVisibility(View.VISIBLE);
                break;
            case "google":
                view.findViewById(R.id.facebook_login_container).setVisibility(View.GONE);
                view.findViewById(R.id.google_login_container).setVisibility(View.VISIBLE);
                break;
            default:
                view.findViewById(R.id.facebook_login_container).setVisibility(View.VISIBLE);
                view.findViewById(R.id.google_login_container).setVisibility(View.VISIBLE);
                break;
        }
    }




    @Override
    public void processLogoutUser() {
        SharedPreferences.Editor editor = applicationPreference.edit();
        editor.remove("loginType");
        editor.remove("userId");
        editor.commit();
        configureTabMode();
        view.findViewById(R.id.google_login_container).setVisibility(View.VISIBLE);
        view.findViewById(R.id.facebook_login_container).setVisibility(View.VISIBLE);

    }

    @Override
    public void processLoggedInUserInformation(String username) {
        applicationPreference = getActivity().getSharedPreferences(APPLICATION_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor info = applicationPreference.edit();
        info.putString("username", username);
        info.commit();
    }
}


