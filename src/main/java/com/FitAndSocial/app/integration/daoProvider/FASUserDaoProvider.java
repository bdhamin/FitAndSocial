package com.FitAndSocial.app.integration.daoProvider;

import com.FitAndSocial.app.integration.DatabaseHelper;
import com.FitAndSocial.app.model.FASUser;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;

/**
 * Created by mint on 22-10-14.
 */
public class FASUserDaoProvider implements Provider<Dao<FASUser, String>>{

    private ConnectionSource connectionSource;

    @Inject
    public FASUserDaoProvider(DatabaseHelper dbHelper){
        connectionSource = dbHelper.getConnectionSource();
    }

    @Override
    public Dao<FASUser, String> get() {
        try {
            return DaoManager.createDao(connectionSource, FASUser.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
