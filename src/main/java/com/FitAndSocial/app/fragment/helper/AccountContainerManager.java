package com.FitAndSocial.app.fragment.helper;

import com.facebook.model.GraphUser;
import com.google.android.gms.plus.model.people.Person;

/**
 * Created by mint on 30-9-14.
 */
public interface AccountContainerManager {

//    public void manageContainers(String loginType, boolean isLogin);
//    public boolean createUserAccountGoogle(Person person, String email);
//    public boolean createUserAccountFacebook(GraphUser graphUser);
    public void processLoggedInFacebookUser(GraphUser user);
    public void processLoggedInGoogleUser(Person user, String email);
    public void processLogoutUser();
}
