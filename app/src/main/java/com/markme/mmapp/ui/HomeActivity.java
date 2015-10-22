package com.markme.mmapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.markme.mmapp.R;
import com.markme.mmapp.utils.HomePagerAdapter;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int CALL_COURSE_INSERT = 315;
    public static final int CALL_LECTURE_INSERT = 813;
    private CoordinatorLayout rootLayout;
    private ViewPager viewPager;
    private Fragment[] pagerFragments;
    private String[] titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_stringArray();
        init_fragments();
        setContentView(R.layout.activity_home);
        init_instances();
    }

    private void init_stringArray(){
        titles = getResources().getStringArray(R.array.titles);
    }

    private void init_fragments(){
        SummaryFragment summaryFragment = new SummaryFragment();
        TimeTableFragment timeTableFragment = new TimeTableFragment();
        CoursesFragment coursesFragment = new CoursesFragment();

        pagerFragments = new Fragment[3];
        pagerFragments[0] = summaryFragment;
        pagerFragments[1] = timeTableFragment;
        pagerFragments[2] = coursesFragment;
    }

    private void init_instances() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        for (String string:titles){
            tabLayout.addTab(tabLayout.newTab().setText(string));
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setAdapter(
                new HomePagerAdapter(getSupportFragmentManager(), pagerFragments, titles));
        tabLayout.setupWithViewPager(viewPager);


        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);

        final FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.addButton);
        addButton.setOnClickListener(this);
        if(viewPager.getCurrentItem() == 0)
            addButton.setVisibility(View.GONE);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    addButton.setVisibility(View.GONE);
                }
                else {
                    addButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CALL_COURSE_INSERT:
                if(resultCode == RESULT_OK){
                    if(pagerFragments[2] instanceof CoursesFragment){
                        CoursesFragment coursesFragment = (CoursesFragment)pagerFragments[2];
                        coursesFragment.getData();
                    }
                    Snackbar.make(rootLayout,
                            "Course Inserted Successfully"
                                        ,Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(rootLayout,
                            "Unable To Insert Course"
                                        ,Snackbar.LENGTH_SHORT).show();
                }
                break;
            case CALL_LECTURE_INSERT:break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View view) {
        int pageNum = viewPager.getCurrentItem();
        if (pageNum == 1) {
            Intent newCourseIntent = new Intent(HomeActivity.this, NewLectureActivity.class);
            startActivityForResult(newCourseIntent, CALL_LECTURE_INSERT);
        }else if (pageNum == 2) {
            Intent newCourseIntent = new Intent(HomeActivity.this, NewCourseActivity.class);
            startActivityForResult(newCourseIntent, CALL_COURSE_INSERT);
        }
    }
}
