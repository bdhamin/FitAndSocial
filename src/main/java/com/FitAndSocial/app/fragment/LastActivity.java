package com.FitAndSocial.app.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.util.ActivitiesLazyAdapter;
import com.FitAndSocial.app.util.Utils;
import com.FitAndSocial.app.util.XMLParser;
import com.actionbarsherlock.app.SherlockFragment;
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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mint on 12-7-14.
 */
public class LastActivity extends BaseFragment {

    private  String LAST_ACTIVITY_URL = "http://192.168.2.9:9000/lastActivity";
    private final String KEY_ACTIVITY = "activity"; //parent node name
    private final String KEY_TITLE = "title";
    private final String KEY_TYPE = "type";
    private final String KEY_DISTANCE = "distance";
    private final String KEY_DURATION = "duration";
    private final String KEY_DATE = "date";
    private final String KEY_TIME = "time";
    private final String KEY_MEMBERS_TOTAL="members_total";
    private TextView showMore;
    private TextView no_members;
    private TextView title;
    private TextView typeName;
    private TextView distanceInKM;
    private TextView dTime;
    private TextView aDate;
    private TextView aTime;
    private ImageView memberOne;
    private ImageView memberTwo;
    private ImageView memberThree;
    private TextView memberOneName;
    private TextView memberTwoName;
    private TextView memberThreeName;
    private TextView members;
    private NodeList nodelist;
    private View view;
    private String authenticationKey;


    public LastActivity(){
//        String activityUrl = assembleUrl(LAST_ACTIVITY_URL);

        new UserLastActivity().execute(LAST_ACTIVITY_URL);
    }

//    private String assembleUrl(String url) {
//
//        StringBuilder sb = new StringBuilder();
////        String authenticationProviderKey = getLoggedInUserId();
//        sb.append(url).append("?id=").append("225");
//        return sb.toString();
//    }





    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        view = layoutInflater.inflate(R.layout.last_activity, container, false);
        showMore = (TextView) view.findViewById(R.id.showMore);
        no_members = (TextView) view.findViewById(R.id.no_members);
        title = (TextView) view.findViewById(R.id.title);
        typeName = (TextView) view.findViewById(R.id.typeName);
        distanceInKM = (TextView) view.findViewById(R.id.km);
        dTime = (TextView) view.findViewById(R.id.dTime);
        aDate = (TextView) view.findViewById(R.id.aDate);
        aTime = (TextView) view.findViewById(R.id.aTime);

        memberOne = (ImageView) view.findViewById(R.id.memberOne);
        memberTwo = (ImageView) view.findViewById(R.id.memberTwo);
        memberThree = (ImageView) view.findViewById(R.id.memberThree);

        memberOneName = (TextView) view.findViewById(R.id.memberOneName);
        memberTwoName = (TextView) view.findViewById(R.id.memberTwoName);
        memberThreeName = (TextView) view.findViewById(R.id.memberThreeName);
        members = (TextView)view.findViewById(R.id.members);
        this.authenticationKey = getLoggedInUserId();
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
                    System.out.println("NO LOGGED IN USER FOUND!");
                    return false;
                }

                String lastActivityAddress = url[0].concat("?id=").concat(authenticationKey);
                URL address = new URL(lastActivityAddress);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                // Download the XML file
                Document doc = db.parse(new InputSource(address.openStream()));
                doc.getDocumentElement().normalize();
                // Locate the Tag Name
                nodelist = doc.getElementsByTagName(KEY_ACTIVITY);

            }catch (MalformedURLException | ParserConfigurationException | SAXException | FileNotFoundException | NullPointerException e ) {
//                Log.e("Error", e.getMessage());
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

                            title.setText(getNode(KEY_TITLE, eElement));
                            typeName.setText(getNode(KEY_TYPE, eElement));
                            distanceInKM.setText(getNode(KEY_DISTANCE, eElement));
                            dTime.setText(getNode(KEY_DURATION, eElement));
                            aDate.setText(getNode(KEY_DATE, eElement));
                            aTime.setText(getNode(KEY_TIME, eElement));
                            members.setText("Members: " + getNode(KEY_MEMBERS_TOTAL, eElement));

//                            NodeList members = ((Element) nodelist.item(temp)).getElementsByTagName("member");
//                            if(members != null && members.getLength() > 0){
//                                for(int j=0; j< members.getLength(); j++){
//                                    Element member = (Element) members.item(j);
//                                    map.put("member_"+j+"_id", xmlParser.getValue(member, "id"));
//                                    map.put("member_"+j+"_name", xmlParser.getValue(member, "name"));
//                                    map.put("member_"+j+"_pictureURL", xmlParser.getValue(member, "pictureURL"));
//                                }
//                            }
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
        SharedPreferences preference = getActivity().getSharedPreferences(APPLICATION_PREFERENCE, Context.MODE_PRIVATE);
        if(preference.contains("userId")){
            String userId = preference.getString("userId", "");
            return userId;
        }
        return "";
    }
}
