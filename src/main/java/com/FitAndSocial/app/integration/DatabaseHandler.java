package com.FitAndSocial.app.integration;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.FitAndSocial.app.model.FASUser;
import com.FitAndSocial.app.model.Notification;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mint on 12-10-14.
 */
public class DatabaseHandler extends SQLiteOpenHelper implements CRUDService{

    private static final String DATABASE_NAME = "notification";
    private static final int DATABASE_VERSION = 1;
    private static final String NOTIFICATION_TABLE = "userNotification";
    private static final String USER_TABLE = "fasUser";
    private static final String ID = "id";
    private static final String USER_ID = "userId";
    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String DATE = "date";
    private static final String OLD_MESSAGE = "oldMessage";
    private static final String USER_NAME = "username";
    private static final String ACTIVE_SINCE = "activeSince";

    private static DatabaseHandler _instance;

    public static DatabaseHandler getInstance(Context context){
        if(_instance == null){
            _instance = new DatabaseHandler(context.getApplicationContext());
        }
        return _instance;
    }

    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String notificationQuery = createNotificationQuery();
        String userQuery = createUserQuery();
        db.execSQL(notificationQuery);
        db.execSQL(userQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NOTIFICATION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    private String createNotificationQuery(){
        String query = "CREATE TABLE " + NOTIFICATION_TABLE + "("
                       + ID + " INTEGER PRIMARY KEY NOT NULL, "
                       + USER_ID + " VARCHAR(250) NOT NULL, "
                       + TITLE + " VARCHAR(250), "
                       + MESSAGE + " TEXT, "
                       + DATE + " INTEGER, "
                       + OLD_MESSAGE + " INTEGER" + ")";
        return  query;
    }

    private String createUserQuery(){

        String query = "CREATE TABLE " + USER_TABLE + "("
                        + ID + " VARCHAR(250) PRIMARY KEY NOT NULL, "
                        + USER_NAME + " VARCHAR(100) NOT NULL, "
                        + ACTIVE_SINCE + " VARCHAR(15)" + ")";
        return query;
    }

    @Override
    public void addNotification(Notification notification) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID, notification.getUserId());
        contentValues.put(TITLE, notification.getTitle());
        contentValues.put(MESSAGE, notification.getMessage());
        contentValues.put(DATE, notification.getDate());
        contentValues.put(OLD_MESSAGE, 0);
        db.insert(NOTIFICATION_TABLE, null, contentValues);
        db.close();
    }

    @Override
    public Notification getNotification(int id) {
        try {
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.query(NOTIFICATION_TABLE, new String[]{ID, USER_ID, TITLE, MESSAGE, DATE, OLD_MESSAGE}, ID + "=?",
                        new String[]{String.valueOf(id)}, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    Notification notification = new Notification(Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1), cursor.getString(2), cursor.getString(3), Long.parseLong(cursor.getString(4)), Integer.parseInt(cursor.getString(5)));
                    return notification;
                }
        }catch(Exception e){System.out.println("Database Handler Error");System.out.println(e.getCause());System.out.println(e.getMessage());}
            return null;
        }

    @Override
    public List<Notification> getAllNotifications() {

        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM " + NOTIFICATION_TABLE;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do {
                Notification notification = new Notification();
                notification.setId(cursor.getInt(0));
                notification.setUserId(cursor.getString(1));
                notification.setTitle(cursor.getString(2));
                notification.setMessage(cursor.getString(3));
                notification.setDate(cursor.getLong(4));
                notification.setRead(cursor.getInt(5));
                notifications.add(notification);
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return notifications;
    }

    @Override
    public int getNotificationsCount() {
        String countQuery = "SELECT * FROM " + NOTIFICATION_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount(); //added line here
        cursor.close();
        return count;
    }

    @Override
    public int updateNotification(Notification notification) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OLD_MESSAGE, notification.isRead());
        return db.update(NOTIFICATION_TABLE, contentValues, ID + " = ? ",
                new String[]{String.valueOf(notification.getId()) });
    }

    @Override
    public void deleteNotification(Notification notification) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTIFICATION_TABLE, ID + " = ? ", new String[]{String.valueOf(notification.getId())});
        db.close();

    }

    @Override
    public void addUser(FASUser user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, user.getId());
        contentValues.put(USER_NAME, user.getUsername());
        contentValues.put(ACTIVE_SINCE, user.getActiveSince());
        db.insert(USER_TABLE, null, contentValues);
        db.close();
    }

    @Override
    public FASUser findUser(String userID) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(USER_TABLE, new String[]{ID, USER_NAME, ACTIVE_SINCE}, ID + " = ?",
                    new String[]{userID}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                FASUser fasUser = new FASUser(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                return fasUser;
            }
        }catch (Exception e){System.out.println("Error trying to save into db!");System.out.println(e.getCause());System.out.println(e.getMessage());}
        return null;
    }
}