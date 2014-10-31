package com.FitAndSocial.app.mobile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import com.FitAndSocial.app.fragment.NotificationDetailsFragment;
import com.FitAndSocial.app.fragment.NotificationsListFragment;
import com.FitAndSocial.app.fragment.activityCommunicationInterface.OnSelectedNotificationListener;
import com.FitAndSocial.app.fragment.helper.FragmentVisibilityDeterminationInterface;
import com.FitAndSocial.app.fragment.helper.NonSwipeableViewPager;
import com.FitAndSocial.app.adapter.ViewPagerAdapter;
import com.FitAndSocial.app.util.ApplicationConstants;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import roboguice.inject.ContentView;
import static android.support.v4.view.ViewPager.SimpleOnPageChangeListener;

@ContentView(R.layout.main)
public class FitAndSocial extends RoboSherlockFragmentActivity implements OnSelectedNotificationListener {

    private ActionBar actionbar;
    private ActionBar.Tab tab;
    private NonSwipeableViewPager viewPager;
    private SharedPreferences applicationPreference;
//    private FragmentVisibilityDeterminationInterface fragmentVisibility;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            return;
        }
        actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionbar.setCustomView(R.layout.notification_bar_layout);

//        getSupportActionBar().setCustomView(R.layout.notification_bar_layout);
        View homeIcon = findViewById(android.R.id.home);
        ((View) homeIcon.getParent()).setVisibility(View.GONE);



        viewPager = (NonSwipeableViewPager)findViewById(R.id.pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(fragmentManager);

        SimpleOnPageChangeListener viewPagerListener = new NonSwipeableViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position){
                super.onPageSelected(position);
                actionbar.setSelectedNavigationItem(position);
//                try{
//                        FragmentVisibilityDeterminationInterface fragment = (FragmentVisibilityDeterminationInterface)viewPagerAdapter.instantiateItem(viewPager, position);
//                        if(fragment != null){
//                        actionbar.setSelectedNavigationItem(position);
//                        fragment.fragmentBecameVisible();
//                    }
//                }catch (ClassCastException e){
//                    actionbar.setSelectedNavigationItem(position);
//                }

            }
        };

        viewPager.setOnPageChangeListener(viewPagerListener);
        viewPager.setAdapter(viewPagerAdapter);


        // Capture tab button clicks
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // Pass the position on tab click to ViewPager
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // TODO Auto-generated method stub
            }
        };

//        tab = actionbar.newTab().setIcon(getResources().getDrawable(R.drawable.home)).setTabListener(tabListener);
        tab = actionbar.newTab().setIcon(getResources().getDrawable(R.drawable.home)).setText(" ").setTabListener(tabListener);
        actionbar.addTab(tab);

        tab = actionbar.newTab().setText(ApplicationConstants.VIEW_PAGER_TAB_PERSONAL).setTabListener(tabListener);
        actionbar.addTab(tab);

        tab = actionbar.newTab().setText(ApplicationConstants.VIEW_PAGER_TAB_PROFILE).setTabListener(tabListener);
        actionbar.addTab(tab);

        tab = actionbar.newTab().setText(ApplicationConstants.VIEW_PAGER_TAB_NOTIFICATIONS).setTabListener(tabListener);
        actionbar.addTab(tab);

        tab = actionbar.newTab().setText(ApplicationConstants.VIEW_PAGER_TAB_ACCOUNT).setTabListener(tabListener);
        actionbar.addTab(tab);

        applicationPreference = getSharedPreferences(ApplicationConstants.APPLICATION_PREFERENCE, MODE_PRIVATE);

        if(applicationPreference.contains(ApplicationConstants.APPLICATION_PREFERENCE_LOGGED_IN)) {
            String loginType = applicationPreference.getString(ApplicationConstants.APPLICATION_PREFERENCE_LOGGED_IN, "");
            if (loginType != null && !loginType.equals("")) {
                viewPager.setCurrentItem(0);
            }else{
                viewPager.setCurrentItem(4);
            }
        }else{
            viewPager.setCurrentItem(4);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater menuInflater = getSupportMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
//                BaseFragment.getGoogleLoginClient().logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setSelectedNotification(int notificationPosition ,int id) {
        //TODO: if does not exist create it
        NotificationDetailsFragment notificationDetailsFragment =(NotificationDetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.notificationDetails_fragment_container);
        notificationDetailsFragment.displayNotificationDetails(notificationPosition ,id);
    }

    @Override
    public void updateNotificationListView(int id) {
        //TODO: if does not exist create it
        NotificationsListFragment notificationsListFragment = (NotificationsListFragment)
                getSupportFragmentManager().findFragmentById(R.id.notificationList_fragment_container);
        notificationsListFragment.updateView(id);
    }

    @Override
    public void notifyNotificationDetailsContainer(int notificationId) {
        //TODO: if does not exist create it
        NotificationDetailsFragment notificationDetailsFragment =(NotificationDetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.notificationDetails_fragment_container);
        notificationDetailsFragment.updateViewIfNeeded(notificationId);
    }
}
