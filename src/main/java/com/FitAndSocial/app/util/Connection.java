package com.FitAndSocial.app.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by mint on 30-10-14.
 */
public final class Connection {
    private static boolean canConnect = false;
    private static Activity _activity;

    private Connection(){}


    public static boolean hasInternetConnection(Activity activity){
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean canConnectToServer(Activity activity) {
        _activity = activity;
        try {
            new CanConnectTest().execute().get(3500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return canConnect;
    }


    private static class CanConnectTest extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {

            // First, check we have any sort of connectivity
            final ConnectivityManager connMgr = (ConnectivityManager) _activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
            boolean isReachable = false;

            if (netInfo != null && netInfo.isConnected()) {
                // Some sort of connection is open, check if server is reachable
                try {
                    URL url = new URL(ApplicationConstants.SERVER_BASE_ADDRESS + ApplicationConstants.SERVER_ADDRESS_ACTION_CAN_CONNECT);
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setRequestProperty("User-Agent", "Android Application");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    isReachable = (urlc.getResponseCode() == 200);
                } catch (IOException e) {
                    Log.e("OOPS", e.getMessage());
                }
            }
            return isReachable;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            canConnect = success ? true : false;
        }
    }


}
