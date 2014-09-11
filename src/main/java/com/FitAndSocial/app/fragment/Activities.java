package com.FitAndSocial.app.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.util.ActivitiesLazyAdapter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mint on 12-7-14.
 */
public class Activities extends BaseFragment {

    private  final String XML_ADDRESS = "http://192.168.2.7:9000/xml";
    private ListView listView;
    private ActivitiesLazyAdapter activitiesLazyAdapter;
    private final String KEY_ACTIVITY = "activity"; //parent node name
    private final String KEY_TITLE = "title";
    private final String KEY_TYPE = "type";
    private final String KEY_DISTANCE = "distance";
    private final String KEY_DURATION = "duration";
    private final String KEY_DATE = "date";
    private final String KEY_TIME = "time";
    private final String KEY_MEMBERS_TOTAL="members_total";
    private boolean isInformation = false;
    private View view;
    private String results;
    private NodeList nodelist;
    private ProgressDialog pDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        view = inflater.inflate(R.layout.activities, container, false);
        new DownloadXML().execute(XML_ADDRESS);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle onState){
        super.onSaveInstanceState(onState);
        setUserVisibleHint(true);
    }

    public void isActivityInformation(boolean isInformationView){
        if(isInformationView){
            isInformation = true;
        }
    }



    // DownloadXML AsyncTask
    private class DownloadXML extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressbar
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
        protected Void doInBackground(String... Url) {
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
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void args) {

            /**
             * Are there any upcoming activities for the user?
             * If so display them and remove no activities found title
             * else remove the activities fragment and display
             * no activities found fragment
             */

            if (nodelist != null) {

                if (nodelist.getLength() > 0) {

                    TextView noActivitiesFound = (TextView) getActivity().findViewById(R.id.no_upcoming_activities);
                    if (noActivitiesFound != null) {
                        noActivitiesFound.setText("You have " + nodelist.getLength() + " upcoming activities!");
                    }
                    ArrayList<HashMap<String, String>> activitiesList = new ArrayList<>();

                    for (int temp = 0; temp < nodelist.getLength(); temp++) {
                        HashMap<String, String> map = new HashMap<>();
                        Node nNode = nodelist.item(temp);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            map.put(KEY_TITLE, getNode(KEY_TITLE, eElement));
                            map.put(KEY_TYPE, getNode(KEY_TYPE, eElement));
                            map.put(KEY_DISTANCE, getNode(KEY_DISTANCE, eElement));
                            map.put(KEY_DURATION, getNode(KEY_DURATION, eElement));
                            map.put(KEY_DATE, getNode(KEY_DATE, eElement));
                            map.put(KEY_TIME, getNode(KEY_TIME, eElement));
                            map.put(KEY_MEMBERS_TOTAL, getNode(KEY_MEMBERS_TOTAL, eElement));
                            NodeList members = ((Element) nodelist.item(temp)).getElementsByTagName("member");
                            if (members != null && members.getLength() > 0) {
                                for (int j = 0; j < members.getLength(); j++) {
                                    Element member = (Element) members.item(j);
                                    map.put("member_" + j + "_id", getNode("id", member));
                                    map.put("member_" + j + "_name", getNode("name", member));
                                    map.put("member_" + j + "_pictureURL", getNode("pictureURL", member));
                                }
                            }
                            activitiesList.add(map);
                        }
                        listView = (ListView) view.findViewById(R.id.list);
                        activitiesLazyAdapter = new ActivitiesLazyAdapter(getActivity(), activitiesList);
                        listView.setAdapter(activitiesLazyAdapter);
                        activitiesLazyAdapter.setIsInformation(true);
                    }

                } else {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Fragment activities = fragmentManager.findFragmentById(R.id.activities_container);
                    LastActivity lastActivity = new LastActivity();
                    fragmentTransaction.add(R.id.last_activity_fragment_container, lastActivity, "create_fragment");
                    fragmentTransaction.remove(activities);
                    fragmentTransaction.commit();

                }
                pDialog.dismiss();

            }else{
                pDialog.dismiss();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment noActivities = fragmentManager.findFragmentById(R.id.no_activities_fragment_container);
                fragmentTransaction.remove(noActivities);
                fragmentTransaction.commit();
                Toast.makeText(getActivity(), "Could not connect to the server! try again later.", Toast.LENGTH_LONG).show();
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