package com.FitAndSocial.app.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.FitAndSocial.app.fragment.activityCommunicationInterface.OnSelectedNotificationListener;
import com.FitAndSocial.app.integration.service.INotificationRepo;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.model.Notification;
import com.FitAndSocial.app.adapter.NotificationAdapter;
import com.google.inject.Inject;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by mint on 4-7-14.
 */
public class NotificationsListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeLayout;
    private View view;
    private OnSelectedNotificationListener notificationListener;
    private ListView notificationsList;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notifications;
    @Inject
    INotificationRepo _notificationRepo;


    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceBundle){
        view = layoutInflater.inflate(R.layout.notificationslist, container, false);
        swipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.notification_swipe);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark);
        new LoadNotification().execute();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            notificationListener = (OnSelectedNotificationListener) activity;
        }catch (ClassCastException e){
            System.out.println("Class cast Exception");
        }
    }

    private class LoadNotification extends AsyncTask<Void, Boolean, Boolean> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                notifications = _notificationRepo.getAllNotifications();
                if(notifications != null && notifications.size() > 0){
                    swipeLayout.setRefreshing(false);
                    return true;
                }else{
                    swipeLayout.setRefreshing(false);
                    return false;
                }
            }catch (Exception e){
                swipeLayout.setRefreshing(false);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success){
            if(success){
                notificationsList = (ListView)view.findViewById(R.id.notificationsList_lv);
                notificationAdapter = new NotificationAdapter(NotificationsListFragment.this, notifications, notificationListener, notificationsList);
                notificationsList.setAdapter(notificationAdapter);
            }else{
                System.out.println("Cannot load notifications list!!");
            }
        }
    }

    @Override
    public void onRefresh() {
        new LoadNotification().execute();
    }

    public void updateView(int position){
        removeItemFromList(position);
    }

    private void removeItemFromList(int position) {
        Notification notification = notifications.get(position);
        notifications.remove(position);
        notificationAdapter.notifyDataSetChanged();
        notificationAdapter.notifyDataSetInvalidated();
        try {
            _notificationRepo.delete(notification);
            notifyNotificationContainer(notification.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void notifyNotificationContainer(int id){
        notificationListener.notifyNotificationDetailsContainer(id);
    }

}
