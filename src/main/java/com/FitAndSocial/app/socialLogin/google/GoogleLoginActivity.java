//package com.FitAndSocial.app.socialLogin.google;
//
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//
///**
// * Created by mint on 27-9-14.
// */
//public class GoogleLoginActivity extends FragmentActivity{
//
//    private GoogleLogin googleLogin;
//
//    @Override
//    public void onCreate(Bundle savedInstanceBundle){
//
//        if(savedInstanceBundle != null){
//            googleLogin = new GoogleLogin();
//            getSupportFragmentManager().beginTransaction()
//                    .add(android.R.id.content, googleLogin)
//                    .commit();
//        }else{
//            googleLogin = (GoogleLogin) getSupportFragmentManager()
//                    .findFragmentById(android.R.id.content);
//        }
//    }
//
//}
