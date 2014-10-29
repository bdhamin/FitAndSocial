package com.FitAndSocial.app.fragment.helper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.FitAndSocial.app.model.Event;
import com.FitAndSocial.app.util.ApplicationConstants;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import roboguice.service.RoboIntentService;

import java.io.UnsupportedEncodingException;

/**
 * Created by mint on 28-10-14.
 */
public class EventHelperService extends RoboIntentService{
    private String eventType;
    private String url;
    private Event event;
    private String userId;
    private long activityId;
    private final String ACTIVITY_ID = "activityId";
    private final String USER_ID = "userId";

    public EventHelperService(){super("EventHelper");}

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        eventType = bundle.getString(ApplicationConstants.EVENT_TYPE);
        switch (eventType){
            case ApplicationConstants.EVENT_TYPE_CREATE_ACTION:
                 url = ApplicationConstants.SERVER_BASE_ADDRESS+ApplicationConstants.SERVER_ADDRESS_ACTION_CREATE_ACTIVITY;
                 this.event = bundle.getParcelable("eventModel");
                break;
            case ApplicationConstants.EVENT_TYPE_PARTICIPATE_CANCEL_ACTION:
                 url = ApplicationConstants.SERVER_BASE_ADDRESS+ApplicationConstants.SERVER_ADDRESS_ACTION_CANCEL_PARTICIPATION;
                 userId = bundle.getString(USER_ID);
                 activityId = bundle.getLong(ACTIVITY_ID);
                break;
            case ApplicationConstants.EVENT_TYPE_PARTICIPATE_ACTION:
                url = ApplicationConstants.SERVER_BASE_ADDRESS+ApplicationConstants.SERVER_ADDRESS_ACTION_PARTICIPATION_REQUEST;
                userId = bundle.getString(USER_ID);
                activityId = bundle.getLong(ACTIVITY_ID);
                break;
            default:
                break;
        }
        new ProcessEventHelper().execute(eventType);
    }

    private class ProcessEventHelper extends AsyncTask<String, Boolean, Boolean>{

        private JSONObject jsonObject;
        private Gson gson;
        private String json;
        private StringEntity stringEntity;
        private String actionType;

        @Override
        protected Boolean doInBackground(String... type) {

            switch (type[0]){
                case ApplicationConstants.EVENT_TYPE_CREATE_ACTION:

                    gson = new Gson();
                    json = gson.toJson(event);
                    try {
                        stringEntity = new StringEntity(json);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    actionType = ApplicationConstants.EVENT_TYPE_CREATE_ACTION;
                    break;

                case ApplicationConstants.EVENT_TYPE_PARTICIPATE_CANCEL_ACTION:
                case ApplicationConstants.EVENT_TYPE_PARTICIPATE_ACTION:

                    jsonObject = new JSONObject();
                    try {
                        jsonObject.accumulate(ACTIVITY_ID, activityId);
                        jsonObject.accumulate(USER_ID, userId);
                        stringEntity = new StringEntity(jsonObject.toString());
                    } catch (JSONException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    actionType = ApplicationConstants.EVENT_TYPE_PARTICIPATE_CANCEL_ACTION;
                    break;
                default:
                    break;
            }
            try{
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(stringEntity);
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpClient.execute(httpPost);
                StatusLine statusLine = httpResponse.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    return true;
                }else{
                    System.out.println("Error!!" + httpResponse.getStatusLine());
                    return false;
                }
            }catch (Exception e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success){

            String message = "";
            if(success) {
                switch (actionType) {
                    case ApplicationConstants.EVENT_TYPE_CREATE_ACTION:
                        message = "Activity Created Successfully!";
                         break;
                    case ApplicationConstants.EVENT_TYPE_PARTICIPATE_CANCEL_ACTION:
                    case ApplicationConstants.EVENT_TYPE_PARTICIPATE_ACTION:
                        message = "Action completed Successfully!";
                         break;
                }
                Toast.makeText(EventHelperService.this.getApplicationContext(),message, Toast.LENGTH_LONG).show();
            }else{
                message = "Error. Something went wrong try again later.";
                Toast.makeText(EventHelperService.this.getApplicationContext(),message, Toast.LENGTH_LONG).show();
            }
        }
    }

}
