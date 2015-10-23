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

import java.util.Calendar;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener,
                                                                ViewPager.OnPageChangeListener,
        TimeTableFragment.TimeTableInteractionListener,CoursesFragment.CourseFragmentInterface{

    public static final int CALL_COURSE_INSERT = 315;
    public static final int CALL_LECTURE_INSERT = 813;
    public static final int CALL_COURSE_EDIT = 304;
    public static final int CALL_LECTURE_EDIT = 204;

    public static final int RESULT_NO_COURSE = -872;

    public static final int RESULT_UPDATE_COURSE = -472;
    public static final int RESULT_DELETE_COURSE = -491;

    public static final int RESULT_UPDATE_LECTURE = -721;
    public static final int RESULT_DELETE_LECTURE = -814;


    public static final String DAY_OF_WEEK = "dayOfWeek";
    private CoordinatorLayout rootLayout;
    private ViewPager viewPager;
    private Fragment[] pagerFragments;
    private String[] titles;
    private FloatingActionButton addButton;

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
        viewPager.addOnPageChangeListener(this);

        addButton = (FloatingActionButton) findViewById(R.id.add_button);
        if(viewPager.getCurrentItem() == 0)
            addButton.setVisibility(View.GONE);
        addButton.setOnClickListener(this);
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
            case CALL_LECTURE_INSERT:
                if(resultCode == RESULT_OK){
                    if(pagerFragments[1] instanceof TimeTableFragment){
                        TimeTableFragment timeTableFragment = (TimeTableFragment)pagerFragments[1];
                        timeTableFragment.changeSpinnerDay(data.getIntExtra(DAY_OF_WEEK, Calendar.MONDAY));
                    }
                    Snackbar.make(rootLayout,
                            "Lecture Inserted Successfully"
                            ,Snackbar.LENGTH_SHORT).show();
                } else if(resultCode == RESULT_NO_COURSE) {

                    Snackbar.make(rootLayout,
                            "Please Insert A Course First"
                            ,Snackbar.LENGTH_SHORT).show();

                } else {
                        Snackbar.make(rootLayout,
                                "Unable To Insert Lecture"
                                ,Snackbar.LENGTH_SHORT).show();
                    }break;
            case CALL_LECTURE_EDIT:
                if(resultCode == RESULT_UPDATE_LECTURE){
                    if(pagerFragments[1] instanceof TimeTableFragment){
                        TimeTableFragment timeTableFragment = (TimeTableFragment)pagerFragments[1];
                        timeTableFragment.changeSpinnerDay(data.getIntExtra(DAY_OF_WEEK, Calendar.MONDAY));
                    }
                    Snackbar.make(rootLayout,
                            "Lecture Updated Successfully"
                            ,Snackbar.LENGTH_SHORT).show();
                } else if (resultCode == RESULT_DELETE_LECTURE) {
                    if(pagerFragments[1] instanceof TimeTableFragment){
                        TimeTableFragment timeTableFragment = (TimeTableFragment)pagerFragments[1];
                        timeTableFragment.changeSpinnerDay(data.getIntExtra(DAY_OF_WEEK, Calendar.MONDAY));
                    }
                    Snackbar.make(rootLayout,
                            "Lecture Deleted Successfully"
                            ,Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(rootLayout,
                            "Unable To Edit Lecture"
                            ,Snackbar.LENGTH_SHORT).show();
                }break;

            case CALL_COURSE_EDIT:
                if(resultCode == RESULT_UPDATE_COURSE){
                    Snackbar.make(rootLayout,
                            "Course Updated Successfully"
                            ,Snackbar.LENGTH_SHORT).show();
                } else if (resultCode == RESULT_DELETE_COURSE) {
                    Snackbar.make(rootLayout,
                            "Course Deleted Successfully"
                            ,Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(rootLayout,
                            "Unable To Edit Course"
                            ,Snackbar.LENGTH_SHORT).show();
                }break;

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
        if (pageNum == 2) {
            Intent newCourseIntent = new Intent(this, NewCourseActivity.class);
            newCourseIntent.putExtra(NewCourseActivity.INT_EXTRA,-1);
            startActivityForResult(newCourseIntent, CALL_COURSE_INSERT);
        } else if(pageNum == 1) {
            Intent newLectureIntent = new Intent(this, NewLectureActivity.class);
            newLectureIntent.putExtra(NewLectureActivity.INT_EXTRA, -1);
            startActivityForResult(newLectureIntent, CALL_LECTURE_INSERT);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position == 0)
            addButton.setVisibility(View.GONE);
        else
            addButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void editTimeTable(int x) {
        Intent newLectureIntent = new Intent(this, NewLectureActivity.class);
        newLectureIntent.putExtra(NewLectureActivity.INT_EXTRA, x);
        startActivityForResult(newLectureIntent, CALL_LECTURE_EDIT);
    }

    @Override
    public void edit_course(int c_id) {
        Intent editCourseIntent = new Intent(this,NewCourseActivity.class);
        editCourseIntent.putExtra(NewCourseActivity.INT_EXTRA, c_id);
        startActivityForResult(editCourseIntent, CALL_COURSE_EDIT);
    }
}
