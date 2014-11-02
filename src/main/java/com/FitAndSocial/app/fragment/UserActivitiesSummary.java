package com.FitAndSocial.app.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.util.ApplicationConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by mint on 11-9-14.
 */

public class UserActivitiesSummary extends BaseFragment{

    private View view;
    private NodeList nodelist;
    private ProgressDialog pDialog;
    private String userActivitiesAddress;

    private TextView totalActivities;
    private TextView createdActivities;
    private TextView participatedInActivities;
    private TextView cancelledActivities;

    private TextView totalActivitiesNumber;
    private TextView createdActivitiesNumber;
    private TextView participatedInActivitiesNumber;
    private TextView cancelledActivitiesNumber;

    private ImageView createdActivitiesImage;
    private ImageView participatedInActivitiesImage;
    private ImageView cancelledActivitiesImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        view = inflater.inflate(R.layout.activities_summary, null);

        totalActivities = (TextView)view.findViewById(R.id.activities_summary_total);
        createdActivities = (TextView)view.findViewById(R.id.created_activities);
        participatedInActivities = (TextView)view.findViewById(R.id.participated_in);
        cancelledActivities = (TextView)view.findViewById(R.id.cancelled_activities);

        totalActivitiesNumber = (TextView)view.findViewById(R.id.total_activities_number);
        createdActivitiesNumber = (TextView)view.findViewById(R.id.total_created_activities_number);
        participatedInActivitiesNumber = (TextView)view.findViewById(R.id.total_participated_activities_number);
        cancelledActivitiesNumber = (TextView)view.findViewById(R.id.total_cancelled_activities_number);

        createdActivitiesImage = (ImageView)view.findViewById(R.id.view_created_activities);
        participatedInActivitiesImage = (ImageView)view.findViewById(R.id.view_participated_in_activities);
        cancelledActivitiesImage = (ImageView)view.findViewById(R.id.view_cancelled_activities);
        userActivitiesAddress = ApplicationConstants.SERVER_BASE_ADDRESS+ApplicationConstants.SERVER_ADDRESS_ACTION_USER_ACTIVITIES_SUMMARY;
        new DownloadXML().execute(userActivitiesAddress);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        setUserVisibleHint(true);
    }


    // DownloadXML AsyncTask
    private class DownloadXML extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            // Create a progressbar
            pDialog = new ProgressDialog(getActivity());
            // Set progressbar title
            pDialog.setTitle("Checking for upcoming activities");
            // Set progressbar message
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            // Show progressbar
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... Url) {
            try {
                String authenticationProviderKey = getLoggedInUserId();
                if(authenticationProviderKey != null && !authenticationProviderKey.trim().isEmpty()) {
                    String address = Url[0].concat(authenticationProviderKey);
                    URLConnection connection = new URL(address).openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    DocumentBuilderFactory dbf = DocumentBuilderFactory
                            .newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    // Download the XML file
                    Document doc = db.parse(new InputSource(connection.getInputStream()));
                    doc.getDocumentElement().normalize();
                    // Locate the Tag Name
                    nodelist = doc.getElementsByTagName(ApplicationConstants.KEY_ACTIVITY);
                }else{
                    pDialog.dismiss();
                    return false;
                }

            }catch (MalformedURLException | ParserConfigurationException | SAXException | FileNotFoundException | SocketTimeoutException e ) {
                pDialog.dismiss();
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                pDialog.dismiss();
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean canConnect) {

            if (canConnect) {
                canConnectToServer(true);
                    for (int temp = 0; temp < nodelist.getLength(); temp++) {
                        Node nNode = nodelist.item(temp);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            totalActivitiesNumber.setText(getNode(ApplicationConstants.KEY_TOTAL, eElement));
                            createdActivitiesNumber.setText(getNode(ApplicationConstants.KEY_CREATED, eElement));
                            participatedInActivitiesNumber.setText(getNode(ApplicationConstants.KEY_PARTICIPATED, eElement));
                            cancelledActivitiesNumber.setText(getNode(ApplicationConstants.KEY_CANCELLED, eElement));
                            setViewVisibility(getNode(ApplicationConstants.KEY_CREATED, eElement), getNode(ApplicationConstants.KEY_PARTICIPATED, eElement), getNode(ApplicationConstants.KEY_CANCELLED, eElement));
                        }
                    }
//                }
                pDialog.dismiss();
            }else{
                pDialog.dismiss();
                canConnectToServer(false);
            }
        }

        private void setViewVisibility(String createdActivities, String participatedInActivities, String cancelledActivities) {

            if(createdActivities.trim().isEmpty() || createdActivities.equals("0")){
                createdActivitiesImage.setVisibility(View.INVISIBLE);
            }
            if(participatedInActivities.trim().isEmpty() || participatedInActivities.equals("0")){
                participatedInActivitiesImage.setVisibility(View.INVISIBLE);
            }
            if(cancelledActivities.trim().isEmpty() || cancelledActivities.equals("0")){
                cancelledActivitiesImage.setVisibility(View.INVISIBLE);
            }

        }

        private void canConnectToServer(boolean connection){
            manageRequiredFragments(connection);
        }

        private void manageRequiredFragments(boolean connection) {
            if(connection){
                if(totalActivitiesNumber.getVisibility() != View.VISIBLE){
                    manageTextVisibility(View.VISIBLE);
                }
            }else{
                if(totalActivitiesNumber.getVisibility() == View.VISIBLE){
                    manageTextVisibility(View.INVISIBLE);
                }
            }
        }

        private void manageTextVisibility(int visibility){

            totalActivities.setVisibility(visibility);
            participatedInActivities.setVisibility(visibility);
            cancelledActivities.setVisibility(visibility);
            totalActivitiesNumber.setVisibility(visibility);
            createdActivitiesNumber.setVisibility(visibility);
            participatedInActivitiesNumber.setVisibility(visibility);
            cancelledActivitiesNumber.setVisibility(visibility);
            createdActivitiesImage.setVisibility(visibility);
            participatedInActivitiesImage.setVisibility(visibility);
            cancelledActivitiesImage.setVisibility(visibility);
            if(visibility == View.VISIBLE){
                createdActivities.setText("Created Activities");
            }else{
                createdActivities.setText("Cannot connect to the server try again later!");
            }
        }
    }

    // getNode function
    private static String getNode(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
                .getChildNodes();
        Node nValue = (Node) nlList.item(0);
        return nValue.getNodeValue();
    }

}
