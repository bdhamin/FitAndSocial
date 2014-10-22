package com.FitAndSocial.app.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import com.FitAndSocial.app.fragment.activityCommunicationInterface.OnSelectedNotificationListener;
import com.FitAndSocial.app.integration.DatabaseHandler;
import com.FitAndSocial.app.mobile.FitAndSocial;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.model.Notification;
import com.FitAndSocial.app.adapter.NotificationAdapter;

import java.util.List;

/**
 * Created by mint on 4-7-14.
 */
public class NotificationsListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeLayout;
    private static View view;
    private OnSelectedNotificationListener notificationListener;
    private ListView notificationsList;
    private static NotificationAdapter notificationAdapter;
    private static List<Notification> notifications;
    private static DatabaseHandler db;


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

//        TextView noNotification;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

//            noNotification = (TextView)view.findViewById(R.id.no_notification);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                db = new DatabaseHandler(getActivity().getApplicationContext());
                notifications = db.getAllNotifications();
                if(notifications != null && notifications.size() > 0){
                    swipeLayout.setRefreshing(false);
                    return true;
                }else{
                    swipeLayout.setRefreshing(false);
                    return false;
                }
            }catch (Exception e){
                swipeLayout.setRefreshing(false);
                System.out.println("Exception!");
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success){
            if(success){
//                noNotification.setText("");
                notificationsList = (ListView)view.findViewById(R.id.notificationsList_lv);
                notificationAdapter = new NotificationAdapter(NotificationsListFragment.this, notifications, notificationListener, notificationsList);
                notificationsList.setAdapter(notificationAdapter);
            }else{
//                noNotification.setText("Nothing here...");
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
        db.deleteNotification(notification);
    }


}
