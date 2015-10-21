package com.markme.mmapp.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class HomePagerAdapter extends FragmentPagerAdapter{

    private String[] tabTitles;
    private Fragment[] pagerFragments;

    public HomePagerAdapter(FragmentManager fm, Fragment[] pagerFragments, String[] tabTitles) {
        super(fm);
        this.pagerFragments = pagerFragments;
        this.tabTitles = tabTitles;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        try {
            return pagerFragments[position];
        } catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
