package com.markme.mmapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.markme.mmapp.R;
import com.markme.mmapp.data.Lecture;
import com.markme.mmapp.db.DatabaseAPI;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class NewLectureActivity extends AppCompatActivity implements View.OnClickListener{

    private TimePicker startTimePicker, endTimePicker;
    private TextView startTimeView, endTimeView;
    private ArrayAdapter<String> courseAdapter;
    private Button button;
    private Spinner dayChooseSpinner, courseChooseSpinner;
    private String[] daysArray;
    private EditText location;
    private HashMap<String,String> courseList;
    private ArrayList<String> courseIdList;
    private LinearLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_lecture);

        setUpToolbar();
        initializeStringVariables();
        initializeAllViews();
        getCourseData();

    }

    private void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.new_lecture_toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setTitle(R.string.title_activity_new_lecture);
        setSupportActionBar(toolbar);
    }

    private void initializeStringVariables(){
        daysArray = getResources().getStringArray(R.array.five_day_array);
        courseList = new HashMap<>();
        courseIdList = new ArrayList<>();
    }


    private void initializeAllViews(){
        rootLayout = (LinearLayout)findViewById(R.id.lectureRootLayout);
        location = (EditText)findViewById(R.id.lecture_location);

        startTimePicker = (TimePicker)findViewById(R.id.startTimePicker);
        endTimePicker = (TimePicker)findViewById(R.id.endTimePicker);

        startTimeView = (TextView)findViewById(R.id.startTimeLabel);
        endTimeView = (TextView)findViewById(R.id.endTimeLabel);

        dayChooseSpinner = (Spinner) findViewById(R.id.lecture_add_spinner);
        courseChooseSpinner = (Spinner) findViewById(R.id.course_choose_spinner);

        ArrayAdapter<String> spinnerAdapter =
                                       new ArrayAdapter<>(this, R.layout.spinner_layout, daysArray);
        dayChooseSpinner.setAdapter(spinnerAdapter);

        courseAdapter = new ArrayAdapter<>(this,
                                            R.layout.spinner_layout,
                                                           courseIdList);
        courseChooseSpinner.setAdapter(courseAdapter);

        button = (Button)findViewById(R.id.save_new_lecture_button);
        button.setOnClickListener(this);

    }

    private void disableAllViews(){
        startTimePicker.setEnabled(false);
        endTimePicker.setEnabled(false);
        startTimeView.setVisibility(View.GONE);
        endTimeView.setVisibility(View.GONE);
        courseChooseSpinner.setEnabled(false);
        location.setEnabled(false);
        dayChooseSpinner.setEnabled(false);
        button.setEnabled(false);
    }

    private void enableAllViews(){
        startTimePicker.setEnabled(true);
        endTimePicker.setEnabled(true);
        courseChooseSpinner.setEnabled(true);
        location.setEnabled(true);
        dayChooseSpinner.setEnabled(true);
        button.setEnabled(true);
    }

    private boolean checkValidityOfData(){
        boolean status = true;
        disableAllViews();
        long start_time = startTimePicker.getDrawingTime();
        long end_time = endTimePicker.getDrawingTime();
        if(start_time >= end_time){
            startTimeView.setText("Start Time Cannot be >= End Time");
            startTimeView.setVisibility(View.VISIBLE);
            status = false;
        }
        if(location.getText().toString().trim().equals("")){
            location.setError("Please Enter a Valid Location");
            status = false;
        }
        return status;
    }

    private Lecture createALecture() {
        String courseId = courseChooseSpinner.getSelectedItem().toString();
        String courseName = courseList.get(courseId);
        int lectureDay = dayChooseSpinner.getSelectedItemPosition() + Calendar.MONDAY;
        Time startTime = new Time(startTimePicker.getDrawingTime());
        Time endTime = new Time(endTimePicker.getDrawingTime());
        String location_text = location.getText().toString();
        return new Lecture(courseId, courseName,startTime.toString(), endTime.toString(),
                                                                        lectureDay, location_text);
    }

    private void submitData(){
        Lecture lecture = createALecture();
        SubmitDataTask submitDataTask = new SubmitDataTask(this);
        submitDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,lecture);
    }

    private void getCourseData(){
        CourseIdTask courseIdTask = new CourseIdTask(this);
        courseIdTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void exitEarly(){
        setResult(HomeActivity.RESULT_NO_COURSE);
        finish();
    }

    @Override
    public void onClick(View view) {
        if (checkValidityOfData()){
            submitData();
        } else {
            enableAllViews();
        }
    }

    private void exit_properly(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(HomeActivity.DAY_OF_WEEK,
                                       dayChooseSpinner.getSelectedItemPosition()+ Calendar.MONDAY);
        setResult(RESULT_OK,resultIntent);
        finish();
    }

    private void showFailure(){
        Snackbar.make(rootLayout,"An Error Occured, Try Later",Snackbar.LENGTH_SHORT).show();
    }

    private class CourseIdTask extends AsyncTask<Void,Void,HashMap<String,String>>{

        private DatabaseAPI databaseAPI;

        public CourseIdTask(Context context){
            databaseAPI = new DatabaseAPI(context);
        }

        @Override
        protected HashMap<String,String> doInBackground(Void... voids) {
            return databaseAPI.getAllCourseIds();
        }

        @Override
        protected void onPostExecute(HashMap<String,String> strings) {
            if (strings != null && strings.size() > 0){
                courseList.clear();
                courseIdList.clear();
                courseList.putAll(strings);
                courseIdList.addAll(strings.keySet());
                courseAdapter.notifyDataSetChanged();
            } else {
                exitEarly();
            }
        }
    }

    private class SubmitDataTask extends AsyncTask<Lecture, Void, Boolean>{

        private DatabaseAPI databaseAPI;

        public SubmitDataTask(Context context){
            databaseAPI = new DatabaseAPI(context);
        }

        @Override
        protected Boolean doInBackground(Lecture... lectures) {
            return databaseAPI.addLecture(lectures[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean){
                exit_properly();
            } else {
                showFailure();
            }
        }
    }
}
