package com.FitAndSocial.app.fragment.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.FitAndSocial.app.gcm.DeviceRegistration;
import com.FitAndSocial.app.integration.service.IFASUserRepo;
import com.FitAndSocial.app.model.FASAccount;
import com.FitAndSocial.app.model.FASUser;
import com.FitAndSocial.app.util.Utils;
import com.google.gson.Gson;
import com.google.inject.Inject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.sql.SQLException;
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
public class CreateAccount extends RoboIntentService{

    @Inject
    IFASUserRepo _userRepo;
    private final String CREATE_ACCOUNT_URL = "http://192.168.2.7:9000/register";

    public CreateAccount() {
        super("Create Account");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();

        try {
            FASAccount account = extras.getParcelable("account");
            FASUser user = new FASUser();
            user.setId(account.getProviderKey());
            user.setUsername(account.getFirstName() + " " + account.getLastName());
            user.setActiveSince(Utils.getDateFromLong(new Date().getTime()));
            _userRepo.save(user);
            new ProcessCreatingAccount().execute(account);
        } catch (Exception e) {
            System.out.println("Error Trying to save the user!!");
            e.printStackTrace();
        }
    }

    private class ProcessCreatingAccount extends AsyncTask<FASAccount, Void, Boolean>{

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected Boolean doInBackground(FASAccount... fasAccounts) {
            Gson gson = new Gson();
            String json = "";
            json = gson.toJson(fasAccounts[0]);
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
        }

        @Override
        protected void onPostExecute(Boolean success){
            if(success){
                System.out.println("Successfully registered user");
            }else{
                System.out.println("Error registering user");
            }
            new DeviceRegistration(getApplicationContext());
        }
    }
}