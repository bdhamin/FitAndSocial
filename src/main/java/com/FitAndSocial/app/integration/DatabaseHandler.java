package com.FitAndSocial.app.integration;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.FitAndSocial.app.model.Notification;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mint on 12-10-14.
 */
public class DatabaseHandler extends SQLiteOpenHelper implements CRUDService{

    private static final String DATABASE_NAME = "notification";
    private static final int DATABASE_VERSION = 7;
    private static final String TABLE_NAME = "userNotification";
    private static final String ID = "id";
    private static final String USER_ID = "userId";
    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String DATE = "date";
    private static final String OLD_MESSAGE = "oldMessage";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = createQuery();
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        System.out.println("Drop Query");

        onCreate(db);
    }

    private String createQuery(){
        System.out.println("Create Query");
        String query = "CREATE TABLE " + TABLE_NAME + "("
                       + ID + " INTEGER PRIMARY KEY NOT NULL, "
                       + USER_ID + " VARCHAR(250) NOT NULL, "
                       + TITLE + " VARCHAR(250), "
                       + MESSAGE + " TEXT, "
                       + DATE + " INTEGER, "
                       + OLD_MESSAGE + " INTEGER" + ")";
        return  query;
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
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    @Override
    public Notification getNotification(int id) {
        try {
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.query(TABLE_NAME, new String[]{ID, USER_ID, TITLE, MESSAGE, DATE, OLD_MESSAGE}, ID + "=?",
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
        String query = "SELECT * FROM " + TABLE_NAME;
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
        String countQuery = "SELECT * FROM " + TABLE_NAME;
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
        return db.update(TABLE_NAME, contentValues, ID + " = ? ",
                new String[]{String.valueOf(notification.getId()) });
    }

    @Override
    public void deleteNotification(Notification notification) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ? ", new String[]{String.valueOf(notification.getId())});
        db.close();

    }


}
