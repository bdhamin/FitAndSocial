package com.FitAndSocial.app.integration;

import com.FitAndSocial.app.model.Notification;

import java.util.List;

/**
 * Created by mint on 12-10-14.
 */
public interface CRUDService {

    // Adding new notification
    public void addNotification(Notification notification);

    // Getting single notification
    public Notification getNotification(int id);

    // Getting All Notifications
    public List<Notification> getAllNotifications();

    // Getting Notifications Count
    public int getNotificationsCount();

    // Updating single notification
    public int updateNotification(Notification notification);

    // Deleting single notification
    public void deleteNotification(Notification notification);
}
