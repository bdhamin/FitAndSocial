package com.FitAndSocial.app.fragment;

/**
 * Created by mint on 13-10-14.
 */
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.FitAndSocial.app.fragment.activityCommunicationInterface.OnSelectedNotificationListener;
import com.FitAndSocial.app.integration.DatabaseHandler;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.model.Notification;
import com.FitAndSocial.app.util.Utils;

public class NotificationDetailsFragment extends BaseFragment{

    private static View view;
    private TextView title;
    private TextView content;
    private TextView date;
    private TextView time;
    private ImageButton deleteButton;
    private OnSelectedNotificationListener notificationListener;
    private static Activity _activity;
    private static DatabaseHandler db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        db = new DatabaseHandler(getActivity().getBaseContext());
        view = inflater.inflate(R.layout.notification_details, container, false);
        deleteButton = (ImageButton)view.findViewById(R.id.notification_delete_button);
        deleteButton.setVisibility(View.INVISIBLE);
        return view;
    }

    public void onSaveInstanceState(Bundle savedInstanceBundle){
        super.onSaveInstanceState(savedInstanceBundle);
        setUserVisibleHint(true);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        _activity = activity;
    }

    public void displayNotificationDetails(int id){
        final Notification notification = db.getNotification(id);

        if(notification != null){
            if(notification.isRead() == 0) {
                notification.setRead(1);
                db.updateNotification(notification);
            }
            deleteButton = (ImageButton)view.findViewById(R.id.notification_delete_button);
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.deleteNotification(notification);
                    try{
//                        notificationListener = (OnSelectedNotificationListener) _activity;
//                        notificationListener.updateNotificationListView();
                    }catch (ClassCastException e){
                        System.out.println("Class cast Exception");
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

    }


}
