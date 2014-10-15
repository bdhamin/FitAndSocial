package com.FitAndSocial.app.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.FitAndSocial.app.mobile.R;

/**
 * Created by mint on 15-10-14.
 */
public class NotificationContainerFragment extends BaseFragment{

    private View view;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceBundle){

        view = layoutInflater.inflate(R.layout.notification_container_layout, container, false);
        manageFragments();
        return view;

    }



    private void manageFragments(){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        NotificationsListFragment notificationsListFragment = new NotificationsListFragment();
        NotificationDetailsFragment notificationDetailsFragment = new NotificationDetailsFragment();

        transaction.add(R.id.notificationList_fragment_container, notificationsListFragment);
        transaction.add(R.id.notificationDetails_fragment_container, notificationDetailsFragment);

        transaction.commit();

    }


}
