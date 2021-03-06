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
public class HomeFragment extends BaseFragment{

    private static final String NO_ACTIVITIES_FRAGMENT_ID = "no_activities_fragment";

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceBundle){

        View view = layoutInflater.inflate(R.layout.home, container, false);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NoActivities noActivities = new NoActivities();
        Activities activities = new Activities();
        fragmentTransaction.add(R.id.activities_container, activities, "activities");
        fragmentTransaction.add(R.id.no_activities_fragment_container, noActivities, NO_ACTIVITIES_FRAGMENT_ID);
        fragmentTransaction.commit();
        setFragmentTitle(getUsername());
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }
}
