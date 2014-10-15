package com.FitAndSocial.app.fragment.helper;

import com.facebook.model.GraphUser;
import com.google.android.gms.plus.model.people.Person;

/**
 * Created by mint on 30-9-14.
 */
public interface AccountContainerManager {

    public void processLoggedInFacebookUser(GraphUser user);
    public void processLoggedInGoogleUser(Person user, String email);
    public void processLogoutUser();
    public void processLoggedInUserInformation(String username);
}
