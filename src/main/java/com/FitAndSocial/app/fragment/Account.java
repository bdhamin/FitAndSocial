package com.FitAndSocial.app.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import com.FitAndSocial.app.fragment.helper.AccountContainerManager;
import com.FitAndSocial.app.fragment.helper.NonSwipeableViewPager;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.socialLogin.facebook.FacebookLogin;
import com.FitAndSocial.app.socialLogin.google.GoogleLogin;

/**
 * Created by mint on 29-9-14.
 */
public class Account extends BaseFragment implements AccountContainerManager{

    private View view;
    SharedPreferences applicationPreference;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceBundle){
        view = layoutInflater.inflate(R.layout.account, container, false);
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

    private void configureTabMode() {
        if(isLoggedIn()){
            manageLoginContainer();
            setActionbarNavigationMode(2);
            setFragmentTitle("");
            enableViewPagerSwipe(true);
        }else{
            setActionbarNavigationMode(0);
            setFragmentTitle("Account");
            enableViewPagerSwipe(false);
        }
    }

    private void manageLoginContainer() {
        String connectedWith = applicationPreference.getString("loggedIn", "");
        switch (connectedWith){
            case "facebook":
                view.findViewById(R.id.google_login_container).setVisibility(View.GONE);
                break;
            case "google":
                view.findViewById(R.id.facebook_login_container).setVisibility(View.GONE);
                break;
        }
    }

    private boolean isLoggedIn() {
        applicationPreference = getActivity().getSharedPreferences(APPLICATION_PREFERENCE, Context.MODE_PRIVATE);
        if(applicationPreference.contains("loggedIn")){
            String loginType = applicationPreference.getString("loggedIn", "");
            if(loginType != null && !loginType.equals("")){
                return true;
            }
        }
        return false;
    }


    public void setUserVisibleHint(boolean visibleHint){
        super.setUserVisibleHint(visibleHint);
    }

    @Override
    public void manageContainers(String loginType, boolean isLogin) {
        if(isLogin){
            configureTabMode();
        }else{
            configureTabMode();
            view.findViewById(R.id.google_login_container).setVisibility(View.VISIBLE);
            view.findViewById(R.id.facebook_login_container).setVisibility(View.VISIBLE);
        }
    }
}