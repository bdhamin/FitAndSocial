package com.FitAndSocial.app.mobile;

import android.app.Application;
import com.FitAndSocial.app.integration.DatabaseHelper;

/**
 * Created by mint on 22-10-14.
 */
public class InitApp extends Application{

    @Override
    public void onCreate(){
        new DatabaseHelper(getApplicationContext()).getWritableDatabase();
    }
}
