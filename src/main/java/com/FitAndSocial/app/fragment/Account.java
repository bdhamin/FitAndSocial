package com.FitAndSocial.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import com.FitAndSocial.app.fragment.helper.AccountContainerManager;
import com.FitAndSocial.app.fragment.helper.ProcessRegistration;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.model.FASAccount;
import com.FitAndSocial.app.socialLogin.facebook.FacebookLogin;
import com.FitAndSocial.app.socialLogin.google.GoogleLogin;
import com.facebook.model.GraphUser;
import com.google.android.gms.plus.model.people.Person;
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
                FASAccount account = createAccountUsingGoogle(user, email);
                //Create User account
                Intent processRegistration = new Intent(this.getActivity(), ProcessRegistration.class);
                processRegistration.putExtra("account", account);
                processRegistration.putExtra("registrationPart", "account");
                getActivity().startService(processRegistration);

                //Register User Device to receive notification from GCM
                Intent deviceRegistration = new Intent(this.getActivity(), ProcessRegistration.class);
                deviceRegistration.putExtra("registrationPart", "deviceRegistration");
                getActivity().startService(deviceRegistration);

                addUserToSharedPreferences(user.getId());
                configureLoginSharedPreferences(false, user.getId());
                configureTabMode();
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
                FASAccount account = createAccountUsingFacebook(user);

                //Create User account
                Intent processRegistration = new Intent(this.getActivity(), ProcessRegistration.class);
                processRegistration.putExtra("account", account);
                processRegistration.putExtra("registrationPart", "account");
                getActivity().startService(processRegistration);

                //Register User Device to receive notification from GCM
                Intent deviceRegistration = new Intent(this.getActivity(), ProcessRegistration.class);
                deviceRegistration.putExtra("registrationPart", "deviceRegistration");
                getActivity().startService(deviceRegistration);

                addUserToSharedPreferences(user.getId());
                configureLoginSharedPreferences(true, user.getId());
                configureTabMode();
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
//            return existingUsers.contains(userId) ? true : false;
            return existingUsers.contains(userId);
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
        /**
         * Used apply instead of commit because apply do what commit does only
         * it do it in the background
         */
        sharedEditor.apply();
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
        /**
         * Used apply instead of commit because apply do what commit does only
         * it do it in the background
         */
        editor.apply();
        configureTabMode();
        view.findViewById(R.id.google_login_container).setVisibility(View.VISIBLE);
        view.findViewById(R.id.facebook_login_container).setVisibility(View.VISIBLE);

    }

    @Override
    public void processLoggedInUserInformation(String username) {
        applicationPreference = getActivity().getSharedPreferences(APPLICATION_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor info = applicationPreference.edit();
        info.putString("username", username);
        /**
         * Used apply instead of commit because apply do what commit does only
         * it do it in the background
         */
        info.apply();
    }

    private FASAccount createAccountUsingFacebook(GraphUser user){
        FASAccount account = new FASAccount();
        account.setProviderType("FACEBOOK");
        account.setProviderKey(user.getId());
        account.setAge(30);
        account.setGender(user.getProperty("gender").toString());
        account.setFirstName(user.getFirstName());
        account.setLastName(user.getLastName());
        account.setUserEmail(user.getProperty("email").toString());
        account.setDetails("Please update this section with details about your self");
        account.setNickname("New User");
        account.setActivitiesOfInterest("Swimming, Running, Cycling, Climbing");
        return account;
    }

    private FASAccount createAccountUsingGoogle(Person person, String email){
        FASAccount account = new FASAccount();
        account.setProviderType("GOOGLE");
        account.setProviderKey(person.getId());
        account.setAge(30);
        account.setGender(person.getGender() == 0 ? "male" : "female");
        account.setFirstName(person.getDisplayName());
        account.setLastName(person.getDisplayName());
        account.setUserEmail(email);
        account.setDetails("Please update this section with details about your self");
        account.setNickname("New User");
        account.setActivitiesOfInterest("Swimming, Running, Cycling, Climbing");
        return account;
    }
}


