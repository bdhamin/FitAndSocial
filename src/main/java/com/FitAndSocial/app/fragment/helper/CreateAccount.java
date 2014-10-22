package com.FitAndSocial.app.fragment.helper;

import android.content.Context;
import android.os.AsyncTask;
import com.FitAndSocial.app.fragment.BaseFragment;
import com.FitAndSocial.app.integration.DatabaseHandler;
import com.FitAndSocial.app.model.FASAccount;
import com.FitAndSocial.app.model.FASUser;
import com.FitAndSocial.app.util.Utils;
import com.facebook.model.GraphUser;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Date;

/**
 * Created by mint on 1-10-14.
 */
public class CreateAccount extends EmptyFragmentHelper implements CreateAccountHelper{

    private final String CREATE_ACCOUNT_URL = "http://192.168.2.7:9000/register";
    private GraphUser user;
    private Person person;
    private boolean isCreateFacebookAccount;
    private String email;

    @Override
    public void createAccountUsingFacebook(GraphUser graphUser) {
        this.user = graphUser;
        isCreateFacebookAccount = true;
        try{
            new Register().execute(CREATE_ACCOUNT_URL);

        }catch (Exception e){
        }
    }

    @Override
    public void createAccountUsingGoogle(Person person, String email) {
        this.person = person;
        this.email = email;
        isCreateFacebookAccount = false;
        try {
            new Register().execute(CREATE_ACCOUNT_URL);

        } catch (Exception e) {
        }
    }

    @Override
    public void saveUserInfoToLocalDB(Context context, String id, String displayName, long date) {
        DatabaseHandler db = DatabaseHandler.getInstance(context);
        FASUser fasUser = new FASUser(id, displayName, Utils.getDateFromLong(date));
        db.addUser(fasUser);
    }


    private class Register extends AsyncTask<String, Void, Boolean>{

        private FASAccount account;

        @Override
        protected void onPreExecute(){}

        @Override
        protected Boolean doInBackground(String... createUrl) {
            Gson gson = new Gson();
            String json = "";

            if(isCreateFacebookAccount){
                if(user != null){
                    account = createFacebookAccount();
                    json = gson.toJson(account);
                }else{
                    return false;
                }
            }else{
                if(person != null){
                    account = createGoogleAccount();
                    json = gson.toJson(account);
                }else{
                    return false;
                }
            }

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(createUrl[0]);
                StringEntity entity = new StringEntity(json);
                httpPost.setEntity(entity);
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpClient.execute(httpPost);
                StatusLine statusLine = httpResponse.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    return true;
                }else{
                    return false;
                }
            } catch (MalformedURLException | UnsupportedEncodingException | ClientProtocolException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success){}

        private FASAccount createFacebookAccount(){
            FASAccount account = new FASAccount();
            account.setProviderType("FACEBOOK");
            account.setProviderKey(user.getId());
            account.setAge(30);
            account.setGender(user.getProperty("gender").toString());
            account.setFirstName(user.getFirstName());
            account.setLastName(user.getLastName());
            account.setUserEmail(user.getProperty("email").toString());
            account.setDetails("Please update this section with details about your self");
            account.setNickname("New User");
            account.setActivitiesOfInterest("Swimming, Running, Cycling, Climbing");
            return account;
        }

        private FASAccount createGoogleAccount() {
            FASAccount account = new FASAccount();
            account.setProviderType("GOOGLE");
            account.setProviderKey(person.getId());
            account.setAge(30);
            account.setGender(person.getGender() == 0 ? "male" : "female");
            account.setFirstName(person.getDisplayName());
            account.setLastName(person.getDisplayName());
            account.setUserEmail(email);
            account.setDetails("Please update this section with details about your self");
            account.setNickname("New User");
            account.setActivitiesOfInterest("Swimming, Running, Cycling, Climbing");
            return account;
        }
    }
}