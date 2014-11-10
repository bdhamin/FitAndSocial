package com.FitAndSocial.app.gcm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.FitAndSocial.app.integration.service.INotificationRepo;
import com.FitAndSocial.app.model.Notification;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.inject.Inject;
import roboguice.service.RoboIntentService;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by mint on 6-10-14.
 */
public class GcmMessageHandler extends RoboIntentService {

    private String title;
    private String message;
    private String userId;
    private Handler handler;
    @Inject
    INotificationRepo _notificationRepo;

   public GcmMessageHandler() {
        super("GCM");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);


        title = extras.getString("title");
        message = extras.getString("message");
        userId = extras.getString("userId");
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setUserId(userId);
        notification.setDate(new Date().getTime());

        try {
            _notificationRepo.save(notification);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        showToast();
        Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("title"));
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    public void showToast(){
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), title, Toast.LENGTH_LONG).show();
            }
        });
    }
}
