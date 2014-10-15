package com.FitAndSocial.app.gcm;


import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.FitAndSocial.app.integration.DatabaseHandler;
import com.FitAndSocial.app.model.Notification;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.Date;

/**
 * Created by mint on 6-10-14.
 */
public class GcmMessageHandler extends IntentService {

    private String title;
    private String message;
    private String userId;
    private Handler handler;

   public GcmMessageHandler() {
        super("GCM");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
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
        showToast();
        Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("title"));
        GcmBroadcastReceiver.completeWakefulIntent(intent);

        new ProgressNotification().execute();

    }

    public void showToast(){
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), title, Toast.LENGTH_LONG).show();
            }
        });

    }



    private class ProgressNotification extends AsyncTask<Void, Boolean, Boolean>{

        Notification notification;
        DatabaseHandler databaseHandler;

        @Override
        protected void onPreExecute(){
            databaseHandler= new DatabaseHandler(getApplicationContext());
            notification = new Notification();
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setUserId(userId);
            notification.setDate(new Date().getTime());
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                databaseHandler.addNotification(notification);
            }catch (Exception e){
                return false;
            }
            return true;
        }
    }


}
