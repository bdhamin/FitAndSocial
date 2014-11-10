package com.FitAndSocial.app.fragment.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.FitAndSocial.app.fragment.Account;
import com.FitAndSocial.app.integration.service.IFASUserRepo;
import com.FitAndSocial.app.model.FASAccount;
import com.FitAndSocial.app.model.FASUser;
import com.FitAndSocial.app.util.ApplicationConstants;
import com.FitAndSocial.app.util.Utils;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Date;
import org.apache.commons.io.IOUtils;
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
public class ProcessRegistrationService extends RoboIntentService{

    private FASAccount fasAccount;
    private GoogleCloudMessaging gcm;
    private String deviceRegistrationId;
    private String deviceRegistrationUrl;
    private Context context;
    @Inject
    IFASUserRepo _userRepo;
    private byte[]imgProfilePic;
    private String accountType;

    public ProcessRegistrationService() {
        super("Create Account");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        String registrationPart = extras.getString("registrationPart");

        if(registrationPart.equals("loadImage")){
            String photoUrl = extras.getString("photoUrl");
            this.accountType = extras.getString("accountType");

            new LoadProfileImage().execute(photoUrl);
        }else if(registrationPart.equals("account")){
            try {
                fasAccount = extras.getParcelable("account");
                FASUser user = new FASUser();
                user.setId(fasAccount.getProviderKey());
                user.setUsername(fasAccount.getFirstName() + " " + fasAccount.getLastName());
                user.setActiveSince(Utils.getDateFromLong(new Date().getTime()));
                user.setImageBytes(fasAccount.getImageBytes());
                _userRepo.save(user);
                new ProcessCreatingAccount().execute("account");
            } catch (Exception e) {
                System.out.println("Error Trying to save the user!!");
                e.printStackTrace();
            }
        }else{
            context = this;
            deviceRegistrationUrl = ApplicationConstants.SERVER_BASE_ADDRESS+ApplicationConstants.SERVER_ADDRESS_ACTION_DEVICE_REGISTRATION;
            new ProcessCreatingAccount().execute("registerDevice");
        }
    }

    /**
     * Background Async task to load user profile picture from url
     * When it's done the ProfilePictureReceiver gets notified
     */
    private class LoadProfileImage extends AsyncTask<String, Void, byte[]> {

        @Override
        protected byte[] doInBackground(String... urls) {
            String urlDisplay = urls[0];
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                imgProfilePic = IOUtils.toByteArray(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return imgProfilePic;
        }

        @Override
        protected void onPostExecute(byte[] image){
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(Account.ProfilePictureReceiver.PROCESS_RESPONSE);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra("image", image);
            broadcastIntent.putExtra("accountType", accountType);
            sendBroadcast(broadcastIntent);
        }
    }

    private class ProcessCreatingAccount extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String... registrationPart) {

            //TODO: implement onPostExecute method to support when fail to success process the account creation request
            /**
             * Send registration request to the backend server as a json object
             */
            if(registrationPart[0].equals("account")){
                Gson gson = new Gson();
                String json = gson.toJson(fasAccount);
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(ApplicationConstants.SERVER_BASE_ADDRESS+ApplicationConstants.SERVER_ADDRESS_ACTION_REGISTER);
                    StringEntity entity = new StringEntity(json);
                    httpPost.setEntity(entity);
                    httpPost.setHeader("Content-type", "application/json");
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    StatusLine statusLine = httpResponse.getStatusLine();
                    return statusLine.getStatusCode() == HttpStatus.SC_OK;
                } catch (MalformedURLException | UnsupportedEncodingException | ClientProtocolException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }else{
                /**
                 * Register the device using gcm register method and then send request to the backend to
                 * create a record in the DB and make the device available to receive notification from the server.
                 */
                try {
                    if (gcm == null) {
                        gcm = new GoogleCloudMessaging().getInstance(context);
                    }
                    SharedPreferences sharedPreferences = context.getSharedPreferences("applicationPreference", context.MODE_PRIVATE);
                    String authenticationProviderKey = sharedPreferences.getString("userId", "");
                    deviceRegistrationId = gcm.register(ApplicationConstants.PROJECT_NUMBER);
                    String registrationUrl = deviceRegistrationUrl.concat(authenticationProviderKey).concat("/").concat(deviceRegistrationId);
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(registrationUrl);
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    StatusLine statusLine = httpResponse.getStatusLine();
                    return statusLine.getStatusCode() == HttpStatus.SC_OK;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
    }
}