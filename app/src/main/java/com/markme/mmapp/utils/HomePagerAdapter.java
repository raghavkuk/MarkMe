package com.markme.mmapp.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.markme.mmapp.ui.CoursesFragment;
import com.markme.mmapp.ui.SummaryFragment;
import com.markme.mmapp.ui.TimeTableFragment;

/**
 * Created by raghav on 18/10/15.
 */
public class HomePagerAdapter extends FragmentPagerAdapter{

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Summary", "Time Table", "Courses" };
    private Context context;

    public HomePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new SummaryFragment();
            case 1:
                return new TimeTableFragment();
            case 2:
                return new CoursesFragment();
            default:
                return new SummaryFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
