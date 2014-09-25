package com.FitAndSocial.app.util;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;
import com.FitAndSocial.app.fragment.BaseFragment;
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
public class ParticipationHelper extends AsyncTask<String, Void, Boolean>{


    private long userId;
    private long activityId;
    private ProgressDialog pDialog;
    private BaseFragment baseFragment;
    private final String ACTIVITY_ID = "activityId";
    private final String USER_ID = "userId";


    public ParticipationHelper(long userId, long activityId, BaseFragment baseFragment){
        this.userId = userId;
        this.activityId = activityId;
        this.baseFragment = baseFragment;
    }

    @Override
    protected void onPreExecute(){
        pDialog = new ProgressDialog(baseFragment.getActivity());
        pDialog.setTitle("Participate");
        pDialog.setMessage("Saving...");
        pDialog.setIndeterminate(true);
        pDialog.show();
    }


    @Override
    protected Boolean doInBackground(String... url) {

        try{

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate(ACTIVITY_ID, activityId);
            jsonObject.accumulate(USER_ID, userId);
            StringEntity stringEntity = new StringEntity(jsonObject.toString());

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url[0]);
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(stringEntity);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            StatusLine httpStatus = httpResponse.getStatusLine();
            if(httpStatus.getStatusCode() == HttpStatus.SC_OK){
                pDialog.dismiss();
                return true;
            }else{
                pDialog.dismiss();
                return false;
            }

        }catch (JSONException ex) {
            pDialog.dismiss();
            ex.printStackTrace();
            return false;
        }catch (IOException e){
            e.printStackTrace();
            pDialog.dismiss();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success){
        if(success){
            Toast.makeText(baseFragment.getActivity(), "Participation Success", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(baseFragment.getActivity(), "Participation Failed", Toast.LENGTH_SHORT).show();
        }

    }

}
