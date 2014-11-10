package com.FitAndSocial.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.FitAndSocial.app.fragment.*;

/**
 * Created by mint on 4-7-14.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter{

    // Declare the number of ViewPager pages
    private final static int PAGE_COUNT = 5;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int tab) {
        switch (tab) {
            case 0:
                return new HomeFragment();
            case 1:
                return new PersonalFragment();
            case 2:
                return new ProfileFragment();
            case 3:
                return new NotificationContainerFragment();
            case 4:
                return new Account();
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
