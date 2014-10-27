package com.FitAndSocial.app.fragment.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import com.FitAndSocial.app.integration.service.IFASUserRepo;
import com.FitAndSocial.app.model.FASAccount;
import com.FitAndSocial.app.model.FASUser;
import com.FitAndSocial.app.util.Utils;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.inject.Inject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import roboguice.service.RoboIntentService;

/**
 * Created by mint on 1-10-14.
 */
public class ProcessRegistration extends RoboIntentService{

    private FASAccount fasAccount;
    private GoogleCloudMessaging gcm;
    private final String PROJECT_NUMBER = "5876528920";
    private String deviceRegistrationId;
    private String deviceRegistrationUrl;
    private Context context;
    private String url = "http://192.168.2.7:9000";
    @Inject
    IFASUserRepo _userRepo;
    private final String CREATE_ACCOUNT_URL = "http://192.168.2.7:9000/register";

    public ProcessRegistration() {
        super("Create Account");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        String registrationPart = extras.getString("registrationPart");

        if(registrationPart.equals("account")){
            try {
                fasAccount = extras.getParcelable("account");
                FASUser user = new FASUser();
                user.setId(fasAccount.getProviderKey());
                user.setUsername(fasAccount.getFirstName() + " " + fasAccount.getLastName());
                user.setActiveSince(Utils.getDateFromLong(new Date().getTime()));
                _userRepo.save(user);
                new ProcessCreatingAccount().execute("account");
            } catch (Exception e) {
                System.out.println("Error Trying to save the user!!");
                e.printStackTrace();
            }

        }else{
            context = this;
            deviceRegistrationUrl = url.concat("/deviceRegistrationId/");
            new ProcessCreatingAccount().execute("registerDevice");
        }
    }

    private class ProcessCreatingAccount extends AsyncTask<String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... registrationPart) {

            if(registrationPart[0].equals("account")){
                Gson gson = new Gson();
                String json = "";
                json = gson.toJson(fasAccount);
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(CREATE_ACCOUNT_URL);
                    StringEntity entity = new StringEntity(json);
                    httpPost.setEntity(entity);
                    httpPost.setHeader("Content-type", "application/json");
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    StatusLine statusLine = httpResponse.getStatusLine();
                    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                        return true;
                    }else{
                        return false;
                    }
                } catch (MalformedURLException | UnsupportedEncodingException | ClientProtocolException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }else{
                try {
                    if (gcm == null) {
                        gcm = new GoogleCloudMessaging().getInstance(context);
                    }
                    SharedPreferences sharedPreferences = context.getSharedPreferences("applicationPreference", context.MODE_PRIVATE);
                    String authenticationProviderKey = sharedPreferences.getString("userId", "");
                    deviceRegistrationId = gcm.register(PROJECT_NUMBER);
                    String registrationUrl = deviceRegistrationUrl.concat(authenticationProviderKey).concat("/").concat(deviceRegistrationId);
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(registrationUrl);
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    StatusLine statusLine = httpResponse.getStatusLine();
                    if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
    }
}