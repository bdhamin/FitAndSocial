package com.FitAndSocial.app.util;

import android.os.AsyncTask;
import com.FitAndSocial.app.fragment.Activities;
/**
 * Created by mint on 9-9-14.
 */
public class RetrieveXML extends AsyncTask<String, Void, String>{


    private Exception exception;
    private XMLParser xmlParser = new XMLParser();
//    final Activities activities;
//
//    public RetrieveXML(Activities activities) {
//        this.activities = activities;
//    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            String url = (urls[0]);

            String xmlDocument = xmlParser.getXmlFromUrl(url);
            return xmlDocument;
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

//    @Override
//    protected void onPostExecute(String xmlDocument) {
//        System.out.println(xmlDocument);
////        activities.setXML(xmlDocument);
//    }




//    @Override
//    protected String doInBackground(String... urls) {
//        try {
//            String url = (urls[0]);
//
//            String xmlDocument = xmlParser.getXmlFromUrl(url);
//            return xmlDocument;
//        } catch (Exception e) {
//            this.exception = e;
//            return null;
//        }
//    }
//
//    @Override
//    protected void onPostExecute(String xmlDocument) {
//
//
//    }


}
