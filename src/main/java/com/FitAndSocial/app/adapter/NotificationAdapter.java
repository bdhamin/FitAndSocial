package com.FitAndSocial.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
    private int color;
    private ImageView deleteImageView;
    private ListView listView;
    private View view;

    public NotificationAdapter(BaseFragment baseFragment, List<Notification> notificationList, OnSelectedNotificationListener notificationListener, ListView listView){
        this.baseFragment = baseFragment;
        this.notificationList = notificationList;
        this.notificationListener = notificationListener;
        this.listView = listView;
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

       view = convertView;
        if(view == null)
            view = layoutInflater.inflate(R.layout.notification_adapter_row, null);

        title = (TextView)view.findViewById(R.id.notificationTitle);
        date = (TextView)view.findViewById(R.id.notificationDate);
        time = (TextView)view.findViewById(R.id.notificationTime);
        deleteImageView = (ImageView)view.findViewById(R.id.delete_image_view);

        Notification notification = new Notification();
        notification = notificationList.get(position);
        if(notification.isRead() == 1){
           color = baseFragment.getView().getResources().getColor(android.R.color.darker_gray);
        }else{
            color = baseFragment.getView().getResources().getColor(android.R.color.black);
        }
        title.setTextColor(color);
        title.setText(notification.getTitle());
        if(notification.getDate() != 0 && notification.getDate() > 0){
            date.setTextColor(color);
            date.setText(Utils.getDateFromLong(notification.getDate()));
            time.setTextColor(color);
            time.setText(Utils.getTimeFromLong(notification.getDate()));
        }
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationListener.updateNotificationListView(position);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationListener.setSelectedNotification(notificationList.get(position).getId());
            }
        });
        return view;
    }
}
