package com.FitAndSocial.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.FitAndSocial.app.fragment.BaseFragment;

/**
 * Created by mint on 11-9-14.
 */

import android.view.View;
import com.FitAndSocial.app.mobile.R;

public class ActivitiesSummary extends BaseFragment{

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        view = inflater.inflate(R.layout.activities_summary, null);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
    }


}
