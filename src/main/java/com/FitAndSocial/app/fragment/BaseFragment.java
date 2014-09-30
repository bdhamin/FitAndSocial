package com.FitAndSocial.app.fragment;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.widget.TextView;
import com.FitAndSocial.app.fragment.helper.NonSwipeableViewPager;
import com.FitAndSocial.app.mobile.FitAndSocial;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.socialLogin.google.GoogleLogin;
import com.actionbarsherlock.app.ActionBar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mint on 31-7-14.
 */
public class BaseFragment extends Fragment {

    private ActionBar actionbar;
    private NonSwipeableViewPager viewPager;
    private String title;
    private TextView fragmentTitle;
    private boolean isExternalInformationRequired;
    private static GoogleLogin googleLoginClient;
    protected SharedPreferences applicationPreference;
    protected final String APPLICATION_PREFERENCE = "applicationPreference";

    /**
     * On a certain fragments we need to disable the navigation tabs and enable them
     * again when needed. At this fragment we need to make sure that its enabled
     * and that is why we check the status of the actionbar.
     *
     * Note: that 2 is equal to NAVIGATION_MODE_TABS
     * NAVIGATION_MODE_STANDARD = 0;
     * NAVIGATION_MODE_LIST = 1;
     * NAVIGATION_MODE_TABS = 2;
     * DISPLAY_USE_LOGO = 1;
     * DISPLAY_SHOW_HOME = 2;
     * DISPLAY_HOME_AS_UP = 4;
     * DISPLAY_SHOW_TITLE = 8;
     * DISPLAY_SHOW_CUSTOM = 16;
     */
    protected void setActionbarNavigationMode(int navigationMode) {
        actionbar = ((FitAndSocial)getActivity()).getSupportActionBar();
        actionbar.setNavigationMode(navigationMode);
    }

    /**
     * Disable the swipe between pages. This is necessary because we added
     * the previous fragments to the transaction back stack. If we won't
     * disable it and go to another tab then click on back we will then get
     * java.lang.IllegalArgumentException: No view found since the other tab
     * or page does not have same fragment holders.
     */
    protected void enableViewPagerSwipe(boolean isEnabled){
        viewPager = (NonSwipeableViewPager)getActivity().findViewById(R.id.pager);
        if(viewPager != null) {
            viewPager.setIsSwipeable(isEnabled);
        }
    }

    protected void setFragmentTitle(String title){
        this.title = title;
        if(title != null && !title.trim().isEmpty()){
            changeFragmentTitle();
        }
    }

    private void changeFragmentTitle() {
        fragmentTitle = (TextView)getActivity().findViewById(R.id.username);
        fragmentTitle.setText(this.title);
    }

    /**
     *
     * @param isRequired
     * @return boolean
     * Some of the fragments are used to display information about the user
     * and about the user activities. Some of the user information are stored
     * local and so the user don't need to connect online te view the information
     * for example the user profile. When the user view his/her own profile the data
     * can be loaded from local storage. However this is not the case when the user
     * tries to view another user profile. In this case the data need to be loaded
     * from the server. So this boolean check is made to determine of external connection
     * needed or not.
     *
     */

    protected void setIsExternalInformationRequired(boolean isRequired){
        this.isExternalInformationRequired = isRequired;
    }

    protected boolean getIsExternalInformationRequired(){
        return isExternalInformationRequired;
    }


    public static void setGoogleLoginClient(GoogleLogin googleClient){
        googleLoginClient = googleClient;
    }

    public static GoogleLogin getGoogleLoginClient(){
        return googleLoginClient;
    }

}
