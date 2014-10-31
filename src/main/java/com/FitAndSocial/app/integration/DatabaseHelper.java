package com.FitAndSocial.app.integration;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.FitAndSocial.app.model.FASUser;
import com.FitAndSocial.app.model.Notification;
import com.google.inject.Inject;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by mint on 22-10-14.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

    private static final String DATABASE_NAME = "fitAndSocial.db";
    private static final int DATABASE_VERSION = 2;

    @Inject
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, FASUser.class);
            TableUtils.createTableIfNotExists(connectionSource, Notification.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
         try{
             TableUtils.dropTable(connectionSource, FASUser.class, true);
             TableUtils.dropTable(connectionSource, Notification.class, true);
         }catch (SQLException e){
             System.out.println("droping table..");
         }
    }
}
