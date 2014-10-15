package com.FitAndSocial.app.fragment;

/**
 * Created by mint on 13-10-14.
 */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

    private static DatabaseHandler db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        db = new DatabaseHandler(getActivity().getBaseContext());
        view = inflater.inflate(R.layout.notification_details, container, false);
        return view;
    }

    public void onSaveInstanceState(Bundle savedInstanceBundle){
        super.onSaveInstanceState(savedInstanceBundle);
        setUserVisibleHint(true);
    }

    public void displayNotificationDetails(int id){
        Notification notification = db.getNotification(id);

        if(notification != null){
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
