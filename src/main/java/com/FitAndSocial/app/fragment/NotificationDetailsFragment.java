package com.FitAndSocial.app.fragment;

/**
 * Created by mint on 13-10-14.
 */
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.FitAndSocial.app.fragment.activityCommunicationInterface.OnSelectedNotificationListener;
import com.FitAndSocial.app.integration.service.INotificationRepo;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.model.Notification;
import com.FitAndSocial.app.util.Utils;
import com.google.inject.Inject;

import java.sql.SQLException;

public class NotificationDetailsFragment extends BaseFragment{

    private View view;
    private TextView title;
    private TextView content;
    private TextView date;
    private TextView time;
    private ImageButton deleteButton;
    @Inject
    INotificationRepo _notificationRepo;
    private Notification notification;
//    private int _notificationPosition;
    private OnSelectedNotificationListener onSelectedNotificationListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        view = inflater.inflate(R.layout.notification_details, container, false);
        deleteButton = (ImageButton)view.findViewById(R.id.notification_delete_button);
        deleteButton.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onAttach(Activity activity){
        try{
            super.onAttach(activity);
            onSelectedNotificationListener = (OnSelectedNotificationListener)activity;
        }catch (ClassCastException e){
            System.out.println("Wrong class cast exception!");
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceBundle){
        super.onSaveInstanceState(savedInstanceBundle);
        setUserVisibleHint(true);
    }

    public void displayNotificationDetails(final int notificationPosition ,int id){
//        this._notificationPosition = notificationPosition;
        try {
            notification = _notificationRepo.find(id);
            if(notification != null){
                if(notification.isRead() == 0) {
                    notification.setRead(1);
                    _notificationRepo.update(notification);
                }
                deleteButton.setVisibility(View.VISIBLE);
                deleteButton = (ImageButton)view.findViewById(R.id.notification_delete_button);
                deleteButton.setVisibility(View.VISIBLE);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            updateViewVisibility();
                            onSelectedNotificationListener.updateNotificationListView(notificationPosition);
                            _notificationRepo.delete(notification);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                title = (TextView)view.findViewById(R.id.notificationDetailsTitle);
                content = (TextView)view.findViewById(R.id.notificationDetailsBody);
                date = (TextView)view.findViewById(R.id.notificationDetailsDate);
                time = (TextView)view.findViewById(R.id.notificationDetailsTime);


                title.setText(notification.getTitle());
                content.setText(notification.getMessage());
                date.setText(Utils.getDateFromLong(notification.getDate()));
                time.setText(Utils.getTimeFromLong(notification.getDate()));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateViewIfNeeded(int id){
        if(notification != null) {
            if (notification.getId() == id) {
                updateViewVisibility();
            }
        }
    }

    private void updateViewVisibility() {
        title.setText("");
        content.setText("");
        date.setText("");
        time.setText("");
        deleteButton.setVisibility(View.INVISIBLE);
    }
}
