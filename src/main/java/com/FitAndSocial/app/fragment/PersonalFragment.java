package com.FitAndSocial.app.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.FitAndSocial.app.mobile.R;
import android.support.v4.app.FragmentManager;

/**
 * Created by mint on 4-7-14.
 */
public class PersonalFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceBundle){
        View view = layoutInflater.inflate(R.layout.personal, container, false);
        loadRequiredFragments();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }

    private void loadRequiredFragments() {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        UserGeneralInformation userGeneralInformation = new UserGeneralInformation();
        UserActivitiesSummary userActivitiesSummary = new UserActivitiesSummary();
        fragmentTransaction.add(R.id.personal_general_info_container, userGeneralInformation);
        fragmentTransaction.add(R.id.personal_activities_summary_container, userActivitiesSummary);
        fragmentTransaction.commit();
    }
}