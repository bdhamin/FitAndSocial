package com.FitAndSocial.app.mobile;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.FitAndSocial.app.fragment.helper.ViewPagerAdapter;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import roboguice.inject.ContentView;

import static android.support.v4.view.ViewPager.SimpleOnPageChangeListener;

@ContentView(R.layout.main)
public class FitAndSocial extends RoboSherlockFragmentActivity {

    private ActionBar actionbar;
    private ActionBar.Tab tab;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            return;
        }
        actionbar = getSupportActionBar();
//        actionbar.setDisplayShowHomeEnabled(false);
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.notification_bar_layout);
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        View homeIcon = findViewById(android.R.id.home);
        ((View) homeIcon.getParent()).setVisibility(View.GONE);


        viewPager = (ViewPager)findViewById(R.id.pager);
        FragmentManager fragmentManager = getSupportFragmentManager();

        SimpleOnPageChangeListener viewPagerListener = new ViewPager.SimpleOnPageChangeListener(){

            @Override
            public void onPageSelected(int position){
                super.onPageSelected(position);
                actionbar.setSelectedNavigationItem(position);
            }
        };

        viewPager.setOnPageChangeListener(viewPagerListener);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(fragmentManager);
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

        tab = actionbar.newTab().setIcon(getResources().getDrawable(R.drawable.home)).setTabListener(tabListener);
        actionbar.addTab(tab);

        tab = actionbar.newTab().setText("Personal").setTabListener(tabListener);
        actionbar.addTab(tab);

        tab = actionbar.newTab().setText("Activities").setTabListener(tabListener);
        actionbar.addTab(tab);

        tab = actionbar.newTab().setText("Profile").setTabListener(tabListener);
        actionbar.addTab(tab);

        tab = actionbar.newTab().setIcon(getResources().getDrawable(R.drawable.friends)).setTabListener(tabListener);
        actionbar.addTab(tab);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater menuInflater = getSupportMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
