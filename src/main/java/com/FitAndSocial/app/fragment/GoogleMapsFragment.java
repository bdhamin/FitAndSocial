package com.FitAndSocial.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by mint on 31-7-14.
 */
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.FitAndSocial.app.mobile.R;

public class GoogleMapsFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        View view = inflater.inflate(R.layout.google_maps, container, false);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        setUserVisibleHint(true);
    }



}
