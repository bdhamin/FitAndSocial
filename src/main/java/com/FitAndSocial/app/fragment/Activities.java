package com.FitAndSocial.app.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.adapter.ActivitiesLazyAdapter;
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
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mint on 12-7-14.
 */
public class Activities extends BaseFragment{

    private String url;
    private ListView listView;
    private ActivitiesLazyAdapter activitiesLazyAdapter;
    private View view;
    private NodeList nodelist;
    private SwipeRefreshLayout swipeLayout;
    private TextView notification;
    private SharedPreferences applicationPreference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        view = inflater.inflate(R.layout.activities, container, false);
        notification = (TextView)view.findViewById(R.id.notification);
        setActionbarNavigationMode(2);
        url = ApplicationConstants.SERVER_BASE_ADDRESS+ApplicationConstants.SERVER_ADDRESS_ACTION_UPCOMING_ACTIVITIES;
        new DownloadUpcomingActivities().execute(url);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle onState){
        super.onSaveInstanceState(onState);
        setUserVisibleHint(true);
    }

    private class DownloadUpcomingActivities extends AsyncTask<String, Void, Boolean> implements SwipeRefreshLayout.OnRefreshListener{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.activities_ln);
            swipeLayout.setOnRefreshListener(this);
            swipeLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                    android.R.color.holo_green_dark,
                    android.R.color.holo_blue_dark,
                    android.R.color.holo_green_dark);
        }

        @Override
        protected Boolean doInBackground(String... Url) {

            StringBuilder sb = new StringBuilder();
            applicationPreference = getActivity().getSharedPreferences(ApplicationConstants.APPLICATION_PREFERENCE, Context.MODE_PRIVATE);
            if(applicationPreference.contains(ApplicationConstants.APPLICATION_PREFERENCE_USER_ID)){
                String userId = applicationPreference.getString(ApplicationConstants.APPLICATION_PREFERENCE_USER_ID, "");
                sb.append(url).append("?id=").append(userId);
                String address = sb.toString();

                    try {
                        URLConnection connection = new URL(address).openConnection();
                        connection.setConnectTimeout(5000);
                        connection.setReadTimeout(5000);
                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                        DocumentBuilder db = dbf.newDocumentBuilder();
                        Document doc = db.parse(new InputSource(connection.getInputStream()));
                        doc.getDocumentElement().normalize();
                        nodelist = doc.getElementsByTagName(ApplicationConstants.KEY_ACTIVITY);
                        return true;

                } catch (ParserConfigurationException | SAXException | SocketTimeoutException e ) {
                        System.out.println("timeout!");
                    e.printStackTrace();
                        return false;
                } catch (IOException e) {
                    e.printStackTrace();
                        return false;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean canConnect) {
            /**
             * Are there any upcoming activities for the user?
             * If so display them and remove no activities found title
             * else remove the activities fragment and display
             * no activities found fragment
             */
            if (canConnect) {
                notification.setVisibility(View.INVISIBLE);
                if (nodelist != null && nodelist.getLength() > 0) {
                    canConnectToServer(true);
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
                            map.put(ApplicationConstants.KEY_ACTIVITY_ID, getNode(ApplicationConstants.KEY_ACTIVITY_ID, eElement));
                            map.put(ApplicationConstants.KEY_TITLE, getNode(ApplicationConstants.KEY_TITLE, eElement));
                            map.put(ApplicationConstants.KEY_TYPE, getNode(ApplicationConstants.KEY_TYPE, eElement));
                            map.put(ApplicationConstants.KEY_DISTANCE, getNode(ApplicationConstants.KEY_DISTANCE, eElement));
                            map.put(ApplicationConstants.KEY_DURATION, getNode(ApplicationConstants.KEY_DURATION, eElement));
                            map.put(ApplicationConstants.KEY_DATE, getNode(ApplicationConstants.KEY_DATE, eElement));
                            map.put(ApplicationConstants.KEY_TIME, getNode(ApplicationConstants.KEY_TIME, eElement));
                            map.put(ApplicationConstants.KEY_MEMBERS_TOTAL, getNode(ApplicationConstants.KEY_MEMBERS_TOTAL, eElement));
                            map.put(ApplicationConstants.START_LAT, getNode(ApplicationConstants.START_LAT, eElement));
                            map.put(ApplicationConstants.START_LNG, getNode(ApplicationConstants.START_LNG, eElement));
                            map.put(ApplicationConstants.END_LAT, getNode(ApplicationConstants.END_LAT, eElement));
                            map.put(ApplicationConstants.END_LNG, getNode(ApplicationConstants.END_LNG, eElement));
                            map.put(ApplicationConstants.START_STREET_NAME, getNode(ApplicationConstants.START_STREET_NAME, eElement));
                            map.put(ApplicationConstants.END_STREET_NAME, getNode(ApplicationConstants.END_STREET_NAME, eElement));
                            map.put(ApplicationConstants.COMPLETE_START_ADDRESS, getNode(ApplicationConstants.COMPLETE_START_ADDRESS, eElement));
                            map.put(ApplicationConstants.COMPLETE_END_ADDRESS, getNode(ApplicationConstants.COMPLETE_END_ADDRESS, eElement));
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
                        activitiesLazyAdapter = new ActivitiesLazyAdapter(Activities.this, activitiesList);
                        listView.setAdapter(activitiesLazyAdapter);
                        activitiesLazyAdapter.setIsInformation(false);
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
                swipeLayout.setRefreshing(false);

            }else{
                canConnectToServer(false);
                swipeLayout.setRefreshing(false);
                notification.setText("Can not connect to the server! try again later.");
            }
        }

        @Override
        public void onRefresh() {
            new DownloadUpcomingActivities().execute(url);
        }

        private void canConnectToServer(boolean connection){
               manageRequiredFragments(connection);
        }

        private void manageRequiredFragments(boolean hasConnection){

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if(hasConnection){
                if(fragmentManager.findFragmentById(R.id.no_activities_fragment_container) == null ||
                        !fragmentManager.findFragmentById(R.id.no_activities_fragment_container).isVisible()){
                    NoActivities noActivities = new NoActivities();
                    fragmentTransaction.add(R.id.no_activities_fragment_container, noActivities);
                    fragmentTransaction.commit();
                }
                view.findViewById(R.id.list).setVisibility(View.VISIBLE);
            }else{
                Fragment noActivities = fragmentManager.findFragmentById(R.id.no_activities_fragment_container);
                if(noActivities != null){
                    fragmentTransaction.remove(noActivities);
                    view.findViewById(R.id.list).setVisibility(View.INVISIBLE);
                    fragmentTransaction.commit();
                }
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