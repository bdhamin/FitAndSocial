package com.FitAndSocial.app.integration.daoProvider;

import com.FitAndSocial.app.integration.DatabaseHelper;
import com.FitAndSocial.app.model.Notification;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;

/**
 * Created by mint on 22-10-14.
 */
public class NotificationDaoProvider implements Provider<Dao<Notification, Integer>>{

    private ConnectionSource connectionSource;

    @Inject
    public NotificationDaoProvider(DatabaseHelper databaseHelper){
        connectionSource = databaseHelper.getConnectionSource();
    }

    @Override
    public Dao<Notification, Integer> get() {
        try {
            return DaoManager.createDao(connectionSource, Notification.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
