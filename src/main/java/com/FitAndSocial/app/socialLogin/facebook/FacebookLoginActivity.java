package com.FitAndSocial.app.socialLogin.facebook;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

/**
 * Created by mint on 26-9-14.
 */
public class FacebookLoginActivity extends FragmentActivity{

    private FacebookLogin facebookLogin;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            facebookLogin = new FacebookLogin();
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, facebookLogin)
                    .commit();
        }else{
            facebookLogin = (FacebookLogin) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }


    }




}
