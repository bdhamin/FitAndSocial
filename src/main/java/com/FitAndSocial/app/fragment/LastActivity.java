package com.FitAndSocial.app.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by mint on 12-7-14.
 */
public class LastActivity extends BaseFragment {

    private TextView title;
    private TextView typeName;
    private TextView distanceInKM;
    private TextView dTime;
    private TextView aDate;
    private TextView aTime;
    private TextView members;
    private NodeList nodelist;
    private View view;

    public LastActivity(){
        String lastActivityUrl = ApplicationConstants.SERVER_BASE_ADDRESS+ApplicationConstants.SERVER_ADDRESS_ACTION_LAST_ACTIVITY;
        new UserLastActivity().execute(lastActivityUrl);
    }


    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        view = layoutInflater.inflate(R.layout.last_activity, container, false);
        title = (TextView) view.findViewById(R.id.title);
        typeName = (TextView) view.findViewById(R.id.typeName);
        distanceInKM = (TextView) view.findViewById(R.id.km);
        dTime = (TextView) view.findViewById(R.id.dTime);
        aDate = (TextView) view.findViewById(R.id.aDate);
        aTime = (TextView) view.findViewById(R.id.aTime);
        members = (TextView)view.findViewById(R.id.members);
        setFragmentTitle(getUsername());
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle onState){
        super.onSaveInstanceState(onState);
        setUserVisibleHint(true);
    }

    private class UserLastActivity extends AsyncTask<String, Void, Boolean>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... url){

            try {
                String authenticationKey = getLoggedUserId();

                if (authenticationKey.trim().isEmpty()) {
                    return false;
                }

                String lastActivityAddress = url[0].concat("?id=").concat(authenticationKey);
                URLConnection connection = new URL(lastActivityAddress).openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                // Download the XML file
                Document doc = db.parse(new InputSource(connection.getInputStream()));
                doc.getDocumentElement().normalize();
                // Locate the Tag Name
                nodelist = doc.getElementsByTagName(ApplicationConstants.KEY_ACTIVITY);
            }catch (MalformedURLException | ParserConfigurationException | SAXException | FileNotFoundException | NullPointerException e ) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success){

            if (success) {
                if (nodelist != null && nodelist.getLength() > 0) {
                    for (int temp = 0; temp < nodelist.getLength(); temp++) {
                        Node nNode = nodelist.item(temp);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;

                            title.setText(getNode(ApplicationConstants.KEY_TITLE, eElement));
                            typeName.setText(getNode(ApplicationConstants.KEY_TYPE, eElement));
                            distanceInKM.setText(getNode(ApplicationConstants.KEY_DISTANCE, eElement));
                            dTime.setText(getNode(ApplicationConstants.KEY_DURATION, eElement));
                            aDate.setText(getNode(ApplicationConstants.KEY_DATE, eElement));
                            aTime.setText(getNode(ApplicationConstants.KEY_TIME, eElement));
                            members.setText("Members: " + getNode(ApplicationConstants.KEY_MEMBERS_TOTAL, eElement));
                        }
                    }
                }
             }else{
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Fragment noActivity = fragmentManager.findFragmentById(R.id.no_activities_fragment_container);
                if(noActivity == null){
                    NoActivities noActivities = new NoActivities();
                    fragmentTransaction.add(R.id.no_activities_fragment_container, noActivities);
                    fragmentTransaction.commit();
                }
                TextView lastActivity = (TextView)view.findViewById(R.id.lastActivity);
                lastActivity.setText("No activities found!");
            }
        }

        private String getNode(String sTag, Element eElement) {
            NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
                    .getChildNodes();
            Node nValue = (Node) nlList.item(0);
            return nValue.getNodeValue();
        }
    }
    private String getLoggedUserId(){
        SharedPreferences preference = getActivity().getSharedPreferences(ApplicationConstants.APPLICATION_PREFERENCE, Context.MODE_PRIVATE);
        if(preference.contains(ApplicationConstants.APPLICATION_PREFERENCE_USER_ID)){
            return preference.getString("userId", "");
        }
        return "";
    }
}
