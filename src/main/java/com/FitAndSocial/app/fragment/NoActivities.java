package com.FitAndSocial.app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import com.FitAndSocial.app.mobile.R;
import android.view.View;
import android.support.v4.app.FragmentManager;

/**
 * Created by mint on 12-7-14.
 */
public class NoActivities extends BaseFragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.no_activities, container, false);
        createButtonListener();
        searchButtonListener();
        enableViewPagerSwipe(true);
        setActionbarNavigationMode(2);
        return view;
    }

    private void searchButtonListener() {
        TextView search = (TextView)view.findViewById(R.id.search_activity_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SearchFragment search = new SearchFragment();
                removeUnusedFragments(fragmentManager, fragmentTransaction);
                fragmentTransaction.add(R.id.create_fragment_container, search, "search_fragment");
                fragmentTransaction.commit();
            }
        });
    }

    private void createButtonListener(){
        TextView create = (TextView)view.findViewById(R.id.create_activity_button);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                CreateFragment create = new CreateFragment();
                removeUnusedFragments(fragmentManager, fragmentTransaction);
                fragmentTransaction.add(R.id.create_fragment_container, create, "create_fragment");
                fragmentTransaction.commit();
            }
        });
    }

    private void removeUnusedFragments(FragmentManager fragmentManager, FragmentTransaction fragmentTransaction){

        Fragment noActivities = fragmentManager.findFragmentById(R.id.no_activities_fragment_container);
        Fragment lastActivity = fragmentManager.findFragmentById(R.id.last_activity_fragment_container);
        Fragment createOrSearch = fragmentManager.findFragmentById(R.id.create_fragment_container);
        Fragment activities = fragmentManager.findFragmentById(R.id.activities_container);

        if(createOrSearch != null){fragmentTransaction.remove(createOrSearch);}
        if(lastActivity != null){fragmentTransaction.remove(lastActivity);}
        if(activities != null){fragmentTransaction.remove(activities);}
        fragmentTransaction.remove(noActivities);
        fragmentTransaction.addToBackStack(null);
    }

    @Override
    public void onSaveInstanceState(Bundle onState){
        super.onSaveInstanceState(onState);
        setUserVisibleHint(true);
    }








}
