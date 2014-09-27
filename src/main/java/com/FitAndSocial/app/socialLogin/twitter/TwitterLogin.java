package com.FitAndSocial.app.socialLogin.twitter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.FitAndSocial.app.fragment.BaseFragment;
import com.FitAndSocial.app.mobile.R;

/**
 * Created by mint on 26-9-14.
 */
public class TwitterLogin extends BaseFragment{

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle){
        view = inflater.inflate(R.layout.facebook, container, false);
        return view;
    }


}
