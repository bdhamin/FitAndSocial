package com.FitAndSocial.app.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;
import com.FitAndSocial.app.fragment.helper.NonSwipeableViewPager;
import com.FitAndSocial.app.mobile.FitAndSocial;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.util.ApplicationConstants;
import com.actionbarsherlock.app.ActionBar;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

/**
 * Created by mint on 31-7-14.
 */
public class BaseFragment extends RoboSherlockFragment {

    private ActionBar actionbar;
    private NonSwipeableViewPager viewPager;
    protected SharedPreferences applicationPreference;

    /**
     * On some fragments we need to disable the navigation tabs and enable them
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
        if(title != null && !title.trim().isEmpty()){
            this.changeFragmentTitle(title);
        }
    }

    private void changeFragmentTitle(String title) {
       TextView fragmentTitle = (TextView)getActivity().findViewById(R.id.username);
       fragmentTitle.setText(title);
    }

    public String getLoggedInUserId(){
        applicationPreference = this.getActivity().getSharedPreferences(ApplicationConstants.APPLICATION_PREFERENCE, Context.MODE_PRIVATE);
        if(applicationPreference.contains(ApplicationConstants.APPLICATION_PREFERENCE_USER_ID)){
            return applicationPreference.getString(ApplicationConstants.APPLICATION_PREFERENCE_USER_ID, "");
        }
        return "";
    }

    protected String getUsername(){
        applicationPreference = this.getActivity().getSharedPreferences(ApplicationConstants.APPLICATION_PREFERENCE, Context.MODE_PRIVATE);
        if(applicationPreference.contains(ApplicationConstants.APPLICATION_PREFERENCE_USERNAME)){
            return applicationPreference.getString(ApplicationConstants.APPLICATION_PREFERENCE_USERNAME, "");
        }
        return "";
    }
}
