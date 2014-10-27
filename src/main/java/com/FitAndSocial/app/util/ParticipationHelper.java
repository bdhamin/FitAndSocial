package com.FitAndSocial.app.util;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

/**
 * Created by mint on 20-9-14.
 */
public class ParticipationHelper extends IntentService{

    private String userId;
    private long activityId;
    private final String ACTIVITY_ID = "activityId";
    private final String USER_ID = "userId";
    private final String PARTICIPATION_URL = "http://192.168.2.7:9000/participationRequest";

    public ParticipationHelper(){
        super("ParticipationHelper");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle bundle = intent.getExtras();
        this.userId = bundle.getString("userId");
        this.activityId = bundle.getLong("activityId");
        new Participate().execute();
    }

    private class Participate extends AsyncTask<String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {

        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate(ACTIVITY_ID, activityId);
            jsonObject.accumulate(USER_ID, userId);
            StringEntity stringEntity = new StringEntity(jsonObject.toString());

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(PARTICIPATION_URL);
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(stringEntity);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            StatusLine httpStatus = httpResponse.getStatusLine();
            if(httpStatus.getStatusCode() == HttpStatus.SC_OK){
                return true;
            }else{
                return false;
            }

        }catch (JSONException ex) {
            ex.printStackTrace();
            return false;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        }

        @Override
        public void onPostExecute(Boolean success){
            if(success){
                Toast.makeText(getApplicationContext(), "Participation Success", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Participation Failed", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
