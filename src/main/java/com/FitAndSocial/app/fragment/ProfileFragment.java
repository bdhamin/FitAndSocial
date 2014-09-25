package com.FitAndSocial.app.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.FitAndSocial.app.mobile.R;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
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
import java.util.Arrays;
import java.util.List;

/**
 * Created by mint on 4-7-14.
 */
public class ProfileFragment extends BaseFragment {

    private View view;

    private String url = "http://192.168.2.9:9000/userProfile/368";
    private final String KEY_USER = "user"; //parent node name
    private final String KEY_NAME = "name";
    private final String KEY_AGE = "age";
    private final String KEY_GENDER = "gender";
    private final String KEY_EMAIL = "email";
    private final String KEY_NICKNAME = "nickname";
    private final String KEY_ACTIVITIES = "activities";
    private final String KEY_ABOUT = "about";

    private NodeList nodelist;
    private TextView name;
    private TextView age;
    private TextView gender;
    private TextView email;
    private TextView nickname;
    private TextView about;

    private CheckBox swimming, cycling, running, climbing, walking, gym;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceBundle){
        view = layoutInflater.inflate(R.layout.profile, container, false);
        initTextView();
        initFragments();
        //TODO: change this to own method where we first get the logged in user id and then execute the method
        new UserProfile().execute(url);
        return view;
    }

    private void initFragments() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        UserGeneralInformation userGeneralInformation = new UserGeneralInformation();
        fragmentTransaction.add(R.id.profile_general_info_container, userGeneralInformation);
        fragmentTransaction.commit();
    }

    private void initTextView() {
        name = (TextView)view.findViewById(R.id.profile_name_value);
        age = (TextView)view.findViewById(R.id.profile_age_value);
        gender = (TextView)view.findViewById(R.id.profile_gender_value);
        email = (TextView)view.findViewById(R.id.profile_email_value);
        nickname = (TextView)view.findViewById(R.id.profile_nickname_value);
        about = (TextView)view.findViewById(R.id.profile_about_value);
        swimming = (CheckBox)view.findViewById(R.id.profile_swimming_activity);
        cycling = (CheckBox)view.findViewById(R.id.profile_cycling_activity);
        running = (CheckBox)view.findViewById(R.id.profile_running_activity);
        climbing = (CheckBox)view.findViewById(R.id.profile_climbing_activity);
        walking = (CheckBox)view.findViewById(R.id.profile_walking_activity);
        gym = (CheckBox)view.findViewById(R.id.profile_gym_activity);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }


    private List<String> activitiesType(String activities){

        List<String> activitiesList = Arrays.asList(activities.split(","));
        return activitiesList;

    }


    private class UserProfile extends AsyncTask<String, Void, Boolean>{

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected Boolean doInBackground(String... address){

            try {
                URL url = new URL(address[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();

                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                nodelist = doc.getElementsByTagName(KEY_USER);

            }catch (MalformedURLException | ParserConfigurationException | SAXException | FileNotFoundException e ) {

                Log.e("Error", e.getMessage());
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

            if(success){
                if (nodelist != null && nodelist.getLength() > 0) {
                    for (int temp = 0; temp < nodelist.getLength(); temp++) {
                        Node nNode = nodelist.item(temp);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            name.setText(getNode(KEY_NAME, eElement));
                            age.setText(getNode(KEY_AGE, eElement));
                            gender.setText(getNode(KEY_GENDER, eElement));
                            email.setText(getNode(KEY_EMAIL, eElement));
                            nickname.setText(getNode(KEY_NICKNAME, eElement));
                            about.setText(getNode(KEY_ABOUT, eElement));
                            if(getNode(KEY_ACTIVITIES, eElement) != null && getNode(KEY_ACTIVITIES, eElement) != ""){
                                initActivitiesCheckBox(getNode(KEY_ACTIVITIES, eElement));
                            }
                        }
                    }
                }
            }else{
                System.out.println("Something went wrong!");
            }
        }

        private void initActivitiesCheckBox(String activities) {
            System.out.println("Activities: " + activities);
            List<String> activitiesList = Arrays.asList(activities.split(", "));
            System.out.println("ActivitiesList: " + activitiesList);
            for(String activityType : activitiesList){
                switch (activityType){
                    case "Swimming":
                        swimming.setChecked(true);
                        break;
                    case "Cycling":
                        cycling.setChecked(true);
                        break;
                    case "Running":
                        running.setChecked(true);
                        break;
                    case "Climbing":
                        climbing.setChecked(true);
                        break;
                    case "Walking":
                        walking.setChecked(true);
                        break;
                    case "Gym":
                        gym.setChecked(true);
                        break;
                }
            }

        }

        private String getNode(String sTag, Element eElement) {
            NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
                    .getChildNodes();
            Node nValue = (Node) nlList.item(0);
            return nValue.getNodeValue();
        }
    }

}
