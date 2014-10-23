package com.FitAndSocial.app.fragment.helper;

import android.content.Context;
import com.facebook.model.GraphUser;
import com.google.android.gms.plus.model.people.Person;

import java.util.Date;

/**
 * Created by mint on 1-10-14.
 */
public interface CreateAccountHelper {

    public void createAccountUsingFacebook(GraphUser graphUser);
    public void createAccountUsingGoogle(Person person, String email);
    void saveUserInfoToLocalDB(String id, String displayName, long date);
}
