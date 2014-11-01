package com.FitAndSocial.app.fragment;

import android.content.*;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;
import com.FitAndSocial.app.fragment.helper.AccountContainerManager;
import com.FitAndSocial.app.fragment.helper.ProcessRegistration;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.model.FASAccount;
import com.FitAndSocial.app.socialLogin.facebook.FacebookLogin;
import com.FitAndSocial.app.socialLogin.google.GoogleLogin;
import com.FitAndSocial.app.util.ApplicationConstants;
import com.FitAndSocial.app.util.Connection;
import com.facebook.model.GraphUser;
import com.google.android.gms.plus.model.people.Person;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mint on 29-9-14.
 */
public class Account extends BaseFragment implements AccountContainerManager{

    private View view;
    SharedPreferences applicationPreference;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private SharedPreferences accounts;
    private SharedPreferences.Editor editor;
    private TextView noConnection;
    private Person person;
    private String email;
    private byte[] profileImage;
    private ProfilePictureReceiver receiver;
    private GraphUser user;
    private final String FACEBOOK_PROFILE_PIC_URL = "https://graph.facebook.com/";

    /**
     * Facebook offer difference sizes for the profile picture
     * -small 50pixels wide variable height
     * -normal 100pixels wide variable height
     * -large about 200pixels wide variable height
     * However is also possible to get specific height and width of a picture by
     * manually defining the height and width of the picture. For example:
     * https://graph.facebook.com/{userId}/picture?width=120&height=100
     * or
     * https://graph.facebook.com/{userId}/picture?type=normal
     */
    private final String FACEBOOK_PROFILE_PIC_TYPE = "/picture?width=80&height=80";
    private static final int PROFILE_PIC_SIZE = 80;


    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceBundle){
        view = layoutInflater.inflate(R.layout.account, container, false);
        accounts = getActivity().getSharedPreferences(ApplicationConstants.APPLICATION_PREFERENCE_REGISTERED_USERS, Context.MODE_PRIVATE);
        applicationPreference = getActivity().getSharedPreferences(ApplicationConstants.APPLICATION_PREFERENCE, Context.MODE_PRIVATE);
        editor = accounts.edit();
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        noConnection = (TextView)view.findViewById(R.id.noConnection);
        if(!isLoggedIn()){
            if(Connection.hasInternetConnection(getActivity()) && Connection.canConnectToServer(getActivity())){
                manageStartupFragments();
            }else{
                noConnection.setText("Cannot connect to the server!");
                configureTabMode();
            }
        }else{
            noConnection.setVisibility(View.GONE);
            manageStartupFragments();
        }
        registerReceiver();
        return view;
    }

    /**
     * Register the receiver which is an inner class.
     * The filter listen to specific address which is defined in the class.
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(ProfilePictureReceiver.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ProfilePictureReceiver();
        getActivity().registerReceiver(receiver, filter);
    }

    /**
     * Account startup layout fragments. It only fires when
     * there is an active internet connection and the backend server is up and running
     * or when the user is already logged in
     */
    private void manageStartupFragments(){
        FacebookLogin facebookLogin = new FacebookLogin();
        facebookLogin.setFragment(this);
        GoogleLogin googleLogin = new GoogleLogin();
        googleLogin.setFragment(this);
        transaction.add(R.id.facebook_login_container, facebookLogin);
        transaction.add(R.id.google_login_container, googleLogin);
        transaction.commit();
        configureTabMode();
    }

    public void setUserVisibleHint(boolean visibleHint){
        super.setUserVisibleHint(visibleHint);
    }

    /**
     *
     * @param user
     * @param email
     * If the user register/log in for the first time, a request gets created
     * to load the profile image using intentService and catches the response
     * using broadcastReceiver.
     * If not the first login then just gets the username from db
     * and configure the tab mode.
     */
    @Override
    public void processLoggedInGoogleUser(Person user, String email) {
        if(user != null && !user.getId().equals("")){
            if(!accountExist(user.getId())){
                this.person = user;
                this.email = email;
                String personPhotoUrl = user.getImage().getUrl();
                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,personPhotoUrl.length() - 2)+ PROFILE_PIC_SIZE;
                Intent processRegistration = new Intent(this.getActivity(), ProcessRegistration.class);
                processRegistration.putExtra("registrationPart", "loadImage");
                processRegistration.putExtra("photoUrl", personPhotoUrl);
                processRegistration.putExtra("accountType", "GOOGLE");
                getActivity().startService(processRegistration);

            }else{
                processLoggedInUserInformation(user.getName().getGivenName());
                saveLoginType(false, user.getId());
                configureTabMode();
            }
        }
    }

    /**
     * Gets fired after receiving input from broadcast Receiver.
     * Create a google account creation request
     * and device registration request.
     */
    @Override
    public void createAccountUsingGoogle(){
        FASAccount account = createAccountUsingGoogle(this.person, this.email);
        //Create User account
        Intent processRegistration = new Intent(getActivity(), ProcessRegistration.class);
        processRegistration.putExtra("account", account);
        processRegistration.putExtra("registrationPart", "account");
        getActivity().startService(processRegistration);

        //Register User Device to receive notification from GCM
        Intent deviceRegistration = new Intent(this.getActivity(), ProcessRegistration.class);
        deviceRegistration.putExtra("registrationPart", "deviceRegistration");
        getActivity().startService(deviceRegistration);

        addUserToSharedPreferences(this.person.getId());
        saveLoginType(false, this.person.getId());
        configureTabMode();
    }


    @Override
    public void processLoggedInFacebookUser(GraphUser user) {
        if(user != null && !user.getId().equals("")){
            if(!accountExist(user.getId())){
                this.user = user;
                String personPhotoUrl = FACEBOOK_PROFILE_PIC_URL+user.getId()+FACEBOOK_PROFILE_PIC_TYPE;
                Intent processRegistration = new Intent(this.getActivity(), ProcessRegistration.class);
                processRegistration.putExtra("registrationPart", "loadImage");
                processRegistration.putExtra("photoUrl", personPhotoUrl);
                processRegistration.putExtra("accountType", "FACEBOOK");
                getActivity().startService(processRegistration);

            }else{
                processLoggedInUserInformation(user.getFirstName());
                saveLoginType(true, user.getId());
                configureTabMode();
            }
        }
    }
    @Override
    public void createAccountUsingFacebook(){
        FASAccount account = createAccountUsingFacebook(user);

        //Create User account
        Intent processRegistration = new Intent(this.getActivity(), ProcessRegistration.class);
        processRegistration.putExtra("account", account);
        processRegistration.putExtra("registrationPart", "account");
        getActivity().startService(processRegistration);

        //Register User Device to receive notification from GCM
        Intent deviceRegistration = new Intent(this.getActivity(), ProcessRegistration.class);
        deviceRegistration.putExtra("registrationPart", "deviceRegistration");
        getActivity().startService(deviceRegistration);

        addUserToSharedPreferences(user.getId());
        saveLoginType(true, user.getId());
        configureTabMode();
    }

    /**
     *
     * @param userId
     * @return boolean
     * Check if the user account already exists in the SharedPreference.
     * If true then we don't need to create account for the user if false
     * process with registration
     */
    private boolean accountExist(String userId) {
        if (accounts.contains(ApplicationConstants.APPLICATION_PREFERENCE_USERS)) {
            Set<String> existingUsers = accounts.getStringSet(ApplicationConstants.APPLICATION_PREFERENCE_USERS, null);
            return existingUsers.contains(userId);
        }
        return false;
    }

    /**
     *
     * @param userId
     * Add user to the shared preference
     */
    private void addUserToSharedPreferences(String userId){

        Set<String> users;
        if(accounts.contains(ApplicationConstants.APPLICATION_PREFERENCE_USERS)){
            users = accounts.getStringSet(ApplicationConstants.APPLICATION_PREFERENCE_USERS, null);
            users.add(userId);
        }else{
            users = new HashSet<>();
            users.add(userId);
        }
        editor.putStringSet(ApplicationConstants.APPLICATION_PREFERENCE_USERS, users);
        editor.commit();
    }

    /**
     *
     * @param isFacebookLogin
     * @param userId
     * Store the login type (facebook, google) only those atm.
     * The type is needed to configure the logout button in the account fragment.
     * If facebook than hide google login button and display facebook logout button.
     * If google than hide facebook login button and display google logout button.
     */
    private void saveLoginType(boolean isFacebookLogin, String userId){
        SharedPreferences.Editor sharedEditor = applicationPreference.edit();
        if(isFacebookLogin){
            sharedEditor.putString(ApplicationConstants.APPLICATION_PREFERENCE_LOGIN_TYPE, ApplicationConstants.APPLICATION_PREFERENCE_LOGIN_TYPE_FACEBOOK);
        }else{
            sharedEditor.putString(ApplicationConstants.APPLICATION_PREFERENCE_LOGIN_TYPE, ApplicationConstants.APPLICATION_PREFERENCE_LOGIN_TYPE_GOOGLE);
        }
        sharedEditor.putString(ApplicationConstants.APPLICATION_PREFERENCE_USER_ID, userId);
        /**
         * Used apply instead of commit because apply do what commit does only
         * it do it in the background
         */
        sharedEditor.apply();
    }

    /**
     * Depending on the login status the tab gets showed
     * and hide to the user the same is true about the
     * screen swipe
     */
    private void configureTabMode() {
        if(isLoggedIn()){
            manageLoginContainer();
            setActionbarNavigationMode(2);
            setFragmentTitle(getUsername());
            enableViewPagerSwipe(true);
            noConnection.setVisibility(View.GONE);
        }else{
            setActionbarNavigationMode(0);
            setFragmentTitle(ApplicationConstants.FRAGMENT_TITLE_ACCOUNT);
            enableViewPagerSwipe(false);
        }
    }

    private boolean isLoggedIn() {
        if(applicationPreference.contains(ApplicationConstants.APPLICATION_PREFERENCE_LOGIN_TYPE)){
            String loginType = applicationPreference.getString(ApplicationConstants.APPLICATION_PREFERENCE_LOGIN_TYPE, "");
            return loginType != null && !loginType.equals("");
        }
        return false;
    }

    /**
     * Hide/Display facebook/google login/logout button depending on the
     * account used to login
     */
    private void manageLoginContainer() {
        String connectedWith = applicationPreference.getString(ApplicationConstants.APPLICATION_PREFERENCE_LOGIN_TYPE, "");
        switch (connectedWith){
            case "facebook":
                view.findViewById(R.id.google_login_container).setVisibility(View.GONE);
                view.findViewById(R.id.facebook_login_container).setVisibility(View.VISIBLE);
                break;
            case "google":
                view.findViewById(R.id.facebook_login_container).setVisibility(View.GONE);
                view.findViewById(R.id.google_login_container).setVisibility(View.VISIBLE);
                break;
            default:
                view.findViewById(R.id.facebook_login_container).setVisibility(View.VISIBLE);
                view.findViewById(R.id.google_login_container).setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void processLogoutUser() {
        SharedPreferences.Editor editor = applicationPreference.edit();
        editor.remove(ApplicationConstants.APPLICATION_PREFERENCE_LOGIN_TYPE);
        editor.remove(ApplicationConstants.APPLICATION_PREFERENCE_USER_ID);
        /**
         * Used apply instead of commit because apply do what commit does only
         * it do it in the background
         */
        editor.apply();
        configureTabMode();
        view.findViewById(R.id.google_login_container).setVisibility(View.VISIBLE);
        view.findViewById(R.id.facebook_login_container).setVisibility(View.VISIBLE);

    }

    /**
     *
     * @param username
     * Save the logged in username to the shared preference
     */
    @Override
    public void processLoggedInUserInformation(String username) {
        applicationPreference = getActivity().getSharedPreferences(ApplicationConstants.APPLICATION_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor info = applicationPreference.edit();
        info.putString(ApplicationConstants.APPLICATION_PREFERENCE_USERNAME, username);
        /**
         * Used apply instead of commit because apply do what commit does only
         * it do it in the background
         */
        info.apply();
    }

    /**
     *
     * @param user
     * @return FASAccount
     * Create an Fit&Social Account based on the logged in user info
     */
    private FASAccount createAccountUsingFacebook(GraphUser user){
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
        account.setImageBytes(profileImage);
        return account;
    }

    /**
     *
     * @param person
     * @param email
     * @return FASAccount
     * Create an Fit&Social Account based on the logged in user info
     */
    private FASAccount createAccountUsingGoogle(Person person, String email){
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
        account.setImageBytes(profileImage);
        return account;
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }

    /**
     * Get called from the intent service (onPostExecute) to notify us that the profile image
     * is done loading and is ready to continue.
     * This is essential because we need to load the image before we can create account
     * (because we are going to save the pic to the local db) if we do this using AsyncTask then
     * there is no way to know for sure that the image is loaded so that we can continue creating the account.
     * We could use .get(time in milisecond, time_type) on the AsyncTask but this will cause the main thread to freeze
     * for the given time. And it's in general a bad practice to do so and it also breaks the intent of creating
     * AsyncTask (asynchronous)
     */
    public class ProfilePictureReceiver extends BroadcastReceiver{

        public static final String PROCESS_RESPONSE = "com.FitAndSocial.app.fragment.helper.intent.action.PROCESS_RESPONSE";

        @Override
        public void onReceive(Context context, Intent intent) {
            String createAccountOfType = intent.getStringExtra("accountType");
            profileImage = intent.getByteArrayExtra("image");
            switch (createAccountOfType){
                case "GOOGLE":
                    createAccountUsingGoogle();
                    break;
                case "FACEBOOK":
                    createAccountUsingFacebook();
                    break;
            }
        }
    }
}