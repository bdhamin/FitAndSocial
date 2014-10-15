package com.FitAndSocial.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.FitAndSocial.app.fragment.BaseFragment;
import com.FitAndSocial.app.fragment.activityCommunicationInterface.OnSelectedNotificationListener;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.model.Notification;
import com.FitAndSocial.app.util.Utils;
import java.util.List;

/**
 * Created by mint on 13-10-14.
 */
public class NotificationAdapter extends BaseAdapter{

    private BaseFragment baseFragment;
    private List<Notification> notificationList;
    private static LayoutInflater layoutInflater = null;
    private TextView title;
    private TextView date;
    private TextView time;
    private OnSelectedNotificationListener notificationListener;

    public NotificationAdapter(BaseFragment baseFragment, List<Notification> notificationList, OnSelectedNotificationListener notificationListener){
        this.baseFragment = baseFragment;
        this.notificationList = notificationList;
        this.notificationListener = notificationListener;
        layoutInflater = (LayoutInflater)baseFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return notificationList.size();
    }

    @Override
    public Object getItem(int position) {
        return notificationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

       View view = convertView;
        if(view == null)
            view = layoutInflater.inflate(R.layout.notification_adapter_row, null);

        title = (TextView)view.findViewById(R.id.notificationTitle);
        date = (TextView)view.findViewById(R.id.notificationDate);
        time = (TextView)view.findViewById(R.id.notificationTime);

        Notification notification = new Notification();
        notification = notificationList.get(position);

        title.setText(notification.getTitle());
        if(notification.getDate() != 0 && notification.getDate() > 0){
            date.setText(Utils.getDateFromLong(notification.getDate()));
            time.setText(Utils.getTimeFromLong(notification.getDate()));
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationListener.setSelectedNotification(notificationList.get(position).getId());
            }
        });
        return view;
    }
}
