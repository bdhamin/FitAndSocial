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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;

/**
 * Created by mint on 11-9-14.
 */

public class UserActivitiesSummary extends BaseFragment{

    private View view;
    private  final String USER_ACTIVITIES = "http://192.168.2.9:9000/userActivitiesSummary/341";
    private NodeList nodelist;
    private ProgressDialog pDialog;
    private final String KEY_ACTIVITY = "activity"; //parent node name
    private final String KEY_TOTAL = "total";
    private final String KEY_CREATED = "created";
    private final String KEY_PARTICIPATED = "participated";
    private final String KEY_CANCELLED = "cancelled";

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

        new DownloadXML().execute(USER_ACTIVITIES);
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
                URL url = new URL(Url[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                // Download the XML file
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                // Locate the Tag Name
                nodelist = doc.getElementsByTagName(KEY_ACTIVITY);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean canConnect) {

            if (canConnect) {
                canConnectToServer(true);
//                if (nodelist != null && nodelist.getLength() > 0) {
                    for (int temp = 0; temp < nodelist.getLength(); temp++) {
                        Node nNode = nodelist.item(temp);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            totalActivitiesNumber.setText(getNode(KEY_TOTAL, eElement));
                            createdActivitiesNumber.setText(getNode(KEY_CREATED, eElement));
                            participatedInActivitiesNumber.setText(getNode(KEY_PARTICIPATED, eElement));
                            cancelledActivitiesNumber.setText(getNode(KEY_CANCELLED, eElement));
                            setViewVisibility(getNode(KEY_CREATED, eElement), getNode(KEY_PARTICIPATED, eElement), getNode(KEY_CANCELLED, eElement));
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
//            createdActivities.setVisibility(visibility);
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
