package com.FitAndSocial.app.fragment.helper;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.FitAndSocial.app.fragment.BaseFragment;
import com.FitAndSocial.app.mobile.R;

/**
 * Created by mint on 12-10-14.
 */
public class NotificationHandler extends BaseFragment implements UpdateNotificationCounter{

    @Override
    public void updateNotification() {
        updateNotificationCounter();
    }
}