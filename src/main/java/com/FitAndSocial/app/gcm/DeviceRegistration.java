package com.FitAndSocial.app.gcm;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.FitAndSocial.app.fragment.BaseFragment;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by mint on 9-10-14.
 */
public class DeviceRegistration extends BaseFragment {

    private GoogleCloudMessaging gcm;
    private final String PROJECT_NUMBER = "5876528920";
    private String deviceRegistrationId;
    private String deviceRegistrationUrl;
    private Context activity;

    public DeviceRegistration(Context activity) {
        this.activity = activity;
        deviceRegistrationUrl = getBaseUrl().concat("/deviceRegistrationId/");
        new SendDeviceRegistrationRequest().execute(deviceRegistrationUrl);
    }


    private class SendDeviceRegistrationRequest extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                if (gcm == null) {
                    gcm = new GoogleCloudMessaging().getInstance(activity);
                }
                SharedPreferences sharedPreferences = activity.getSharedPreferences("applicationPreference", activity.MODE_PRIVATE);
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
