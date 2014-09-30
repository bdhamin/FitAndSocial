package com.FitAndSocial.app.fragment.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.FitAndSocial.app.fragment.*;
import com.FitAndSocial.app.socialLogin.facebook.FacebookLogin;
import com.FitAndSocial.app.socialLogin.google.GoogleLogin;

/**
 * Created by mint on 4-7-14.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter{

    // Declare the number of ViewPager pages
    final int PAGE_COUNT = 5;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int tab) {
        switch (tab) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                PersonalFragment personalFragment = new PersonalFragment();
                return personalFragment;
//            case 2:
//                FacebookLogin facebookLogin = new FacebookLogin();
//                return facebookLogin;
            case 2:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            case 3:
                FriendsListFragment friendsListFragment = new FriendsListFragment();
                return friendsListFragment;
            case 4:
                Account account = new Account();
                return account;
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
