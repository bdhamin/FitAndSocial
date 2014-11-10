package com.FitAndSocial.app.fragment.helper;

/**
 * Created by mint on 14-10-14.
 */
public interface OnSelectedNotificationListener {

    public void setSelectedNotification(int notificationPosition, int notificationId);
    public void updateNotificationListView(int id);
    public void notifyNotificationDetailsContainer(int notificationId);

}
