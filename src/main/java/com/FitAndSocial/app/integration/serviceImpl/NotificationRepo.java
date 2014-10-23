package com.FitAndSocial.app.integration.serviceImpl;

import android.content.Context;
import com.FitAndSocial.app.integration.daoProvider.NotificationDaoProvider;
import com.FitAndSocial.app.integration.service.INotificationRepo;
import com.FitAndSocial.app.model.Notification;
import com.google.inject.Inject;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by mint on 22-10-14.
 */
public class NotificationRepo implements INotificationRepo{

    private Dao<Notification, Integer> _notificationDao;

    @Inject
    public NotificationRepo(Context context, NotificationDaoProvider notificationDaoProvider){
        _notificationDao = notificationDaoProvider.get();
    }

    @Override
    public void save(Notification notification) throws SQLException {
        _notificationDao.create(notification);
    }

    @Override
    public Notification find(int id) throws SQLException {
        return _notificationDao.queryForId(id);
    }

    @Override
    public List<Notification> getAllNotifications() throws SQLException {
        return _notificationDao.queryForAll();
    }

    @Override
    public void delete(Notification notification) throws SQLException {
        _notificationDao.delete(notification);
    }

    @Override
    public void update(Notification notification) throws SQLException {
        _notificationDao.update(notification);
    }
}
