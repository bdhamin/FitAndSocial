package com.FitAndSocial.app.integration;

import android.database.sqlite.SQLiteDatabase;
import com.FitAndSocial.app.model.FASUser;
import com.FitAndSocial.app.model.Notification;

import java.util.List;

/**
 * Created by mint on 12-10-14.
 */
public class CRUDServiceImpl implements CRUDService{

    @Override
    public void addNotification(Notification notification) {

    }

    @Override
    public Notification getNotification(int id) {
        return null;
    }

    @Override
    public List<Notification> getAllNotifications() {
        return null;
    }

    @Override
    public int getNotificationsCount() {
        return 0;
    }

    @Override
    public int updateNotification(Notification notification) {
        return 0;
    }

    @Override
    public void deleteNotification(Notification notification) {

    }

    @Override
    public void addUser(FASUser user) {

    }

    @Override
    public FASUser findUser(String userID) {
        return null;
    }
}
