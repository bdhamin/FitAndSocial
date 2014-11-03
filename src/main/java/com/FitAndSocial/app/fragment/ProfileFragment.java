package com.FitAndSocial.app.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.model.UserProfileModel;
import com.FitAndSocial.app.util.ApplicationConstants;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mint on 4-7-14.
 */
public class ProfileFragment extends BaseFragment implements  View.OnClickListener, View.OnFocusChangeListener{

    private View view;
    private NodeList nodelist;
    private TextView name;
    private TextView age;
    private TextView gender;
    private TextView email;
    private EditText nickname;
    private EditText about;
    private TextView editPersonalInfo;
    private TextView editAboutInfo;
    private View rootView;
    private Button update;
    private CheckBox swimming, cycling, running, climbing, walking, gym;
    private ProgressDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceBundle){
        view = layoutInflater.inflate(R.layout.profile, container, false);
        rootView = view.findViewById(R.id.profile_root_container);
        initTextView();
        registerListeners();
        initFragments();
        //TODO: change this to own method where we first get the logged in user id and then execute the method
        String userProfileAddress = ApplicationConstants.SERVER_BASE_ADDRESS+ApplicationConstants.SERVER_ADDRESS_ACTION_USER_PROFILE;
        new LodUserProfile().execute(userProfileAddress);
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
        nickname = (EditText)view.findViewById(R.id.profile_nickname_value);
        nickname.setEnabled(false);
        about = (EditText)view.findViewById(R.id.profile_about_value);
        about.setEnabled(false);
        swimming = (CheckBox)view.findViewById(R.id.profile_swimming_activity);
        cycling = (CheckBox)view.findViewById(R.id.profile_cycling_activity);
        running = (CheckBox)view.findViewById(R.id.profile_running_activity);
        climbing = (CheckBox)view.findViewById(R.id.profile_climbing_activity);
        walking = (CheckBox)view.findViewById(R.id.profile_walking_activity);
        gym = (CheckBox)view.findViewById(R.id.profile_gym_activity);
        editPersonalInfo = (TextView)view.findViewById(R.id.profile_personal_info_edit_title);
        editAboutInfo = (TextView)view.findViewById(R.id.profile_about_edit_title);
        update = (Button)view.findViewById(R.id.profile_update_button);
    }

    private void registerListeners(){
        rootView.setOnClickListener(this);
        about.setOnFocusChangeListener(this);
        nickname.setOnFocusChangeListener(this);
        editPersonalInfo.setOnClickListener(this);
        editAboutInfo.setOnClickListener(this);
        update.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profile_personal_info_edit_title:
                 about.setBackgroundColor(getResources().getColor(R.color.transparent));
                 about.clearFocus();
                 about.setEnabled(false);
                 nickname.setBackgroundColor(getResources().getColor(R.color.editText_focus));
                 nickname.requestFocus();
                 nickname.setEnabled(true);
                 break;
            case R.id.profile_about_edit_title:
                 nickname.clearFocus();
                 nickname.setBackgroundColor(getResources().getColor(R.color.transparent));
                 nickname.setEnabled(false);
                 about.setBackgroundColor(getResources().getColor(R.color.editText_focus));
                 about.setEnabled(true);
                 about.requestFocus();
                 break;
            case R.id.profile_update_button:
                new UpdateUserProfile().execute();
                break;
            default:
                nickname.clearFocus();
                about.clearFocus();
                nickname.setBackgroundColor(getResources().getColor(R.color.transparent));
                about.setBackgroundColor(getResources().getColor(R.color.transparent));

        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId()){
            case R.id.profile_nickname_value:
                if(!hasFocus){
                    hideKeyboard(nickname);
                    break;
                }else{
                    showKeyboard(nickname);
                    break;
                }

            case R.id.profile_about_value:
                if(!hasFocus){
                    hideKeyboard(about);
                    break;
                }else{
                    showKeyboard(about);
                    break;
                }
            default:
                break;
        }
    }

    private void showKeyboard(EditText editText){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard(EditText editText){
        ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


    private class LodUserProfile extends AsyncTask<String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... address){
            try {
                String authenticationProviderKey = getLoggedInUserId();
                if (authenticationProviderKey != null && !authenticationProviderKey.trim().isEmpty()) {
                    String profileUrl = address[0].concat(authenticationProviderKey);
                    URLConnection connection = new URL(profileUrl).openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(new InputSource(connection.getInputStream()));
                    doc.getDocumentElement().normalize();
                    nodelist = doc.getElementsByTagName(ApplicationConstants.KEY_USER);
                }

            }catch (MalformedURLException | ParserConfigurationException | SAXException | FileNotFoundException | SocketTimeoutException e ) {
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
                            name.setText(getNode(ApplicationConstants.KEY_NAME, eElement));
                            age.setText(getNode(ApplicationConstants.KEY_AGE, eElement));
                            gender.setText(getNode(ApplicationConstants.KEY_GENDER, eElement));
                            email.setText(getNode(ApplicationConstants.KEY_EMAIL, eElement));
                            nickname.setText(getNode(ApplicationConstants.KEY_NICKNAME, eElement));
                            about.setText(getNode(ApplicationConstants.KEY_ABOUT, eElement));
                            if(getNode(ApplicationConstants.KEY_ACTIVITIES, eElement) != null && getNode(ApplicationConstants.KEY_ACTIVITIES, eElement) != ""){
                                initActivitiesCheckBox(getNode(ApplicationConstants.KEY_ACTIVITIES, eElement));
                            }
                        }
                    }
                }
            }else{
                System.out.println("Something went wrong!");
            }
        }

        private void initActivitiesCheckBox(String activities) {
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

    private class UpdateUserProfile extends AsyncTask<Void, Boolean, Boolean>{

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Updating Profile");
            progressDialog.setMessage("Updating...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String finalProfileUpdateUrl = buildProfileUpdateURL();

            String _nickname  = nickname.getText().toString();
            String _about = about.getText().toString();

            List<String>activities = new ArrayList<>();

            if(swimming.isChecked()){activities.add(swimming.getText().toString());}
            if(cycling.isChecked()){activities.add(cycling.getText().toString());}
            if(running.isChecked()){activities.add(running.getText().toString());}
            if(climbing.isChecked()){activities.add(climbing.getText().toString());}
            if(walking.isChecked()){activities.add(walking.getText().toString());}
            if(gym.isChecked()){activities.add(gym.getText().toString());}

            String userActivities = buildActivitiesString(activities);

            Gson userProfileUpdate = new Gson();
            UserProfileModel userProfile = new UserProfileModel(_nickname, userActivities, _about);
            String GSONToString = userProfileUpdate.toJson(userProfile);
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(finalProfileUpdateUrl);
                StringEntity stringEntity = new StringEntity(GSONToString);
                httpPost.setEntity(stringEntity);
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpClient.execute(httpPost);
                StatusLine status = httpResponse.getStatusLine();
                if(status.getStatusCode() == HttpStatus.SC_OK){
                    progressDialog.dismiss();
                    return true;
                }else{
                    progressDialog.dismiss();
                    return false;
                }

            } catch (UnsupportedEncodingException | ClientProtocolException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
                progressDialog.dismiss();
                return false;
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
                progressDialog.dismiss();
                return false;
            }
        }

        protected void onPostExecute(Boolean success){
            if(success){
                Toast.makeText(getActivity().getBaseContext(), "Profile Updated Successfully!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity().getBaseContext(), "Error.. Try again later", Toast.LENGTH_SHORT).show();
            }
        }

        private String buildActivitiesString(List<String> activities){
            StringBuilder sb = new StringBuilder();
            for(int i =0; i < activities.size(); i++){
                if(i < activities.size()-1){
                        sb.append(activities.get(i)).append(", ");
                    }else{
                        sb.append(activities.get(i));
                    }
            }
            return sb.toString();
        }

        private String buildProfileUpdateURL(){
            StringBuilder sb = new StringBuilder();
            sb.append(ApplicationConstants.SERVER_BASE_ADDRESS).append(ApplicationConstants.SERVER_ADDRESS_ACTION_UPDATE_PROFILE).append("?authenticationID=").append(getLoggedInUserId());
            return sb.toString();
        }
    }
}