package com.markme.mmapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.markme.mmapp.R;
import com.markme.mmapp.data.Lecture;
import com.markme.mmapp.db.DatabaseAPI;
import com.markme.mmapp.utils.CustomSpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class NewLectureActivity extends AppCompatActivity implements View.OnClickListener {

    private TimePicker startTimePicker, endTimePicker;
    private CustomSpinnerAdapter courseAdapter;
    private Button button,deleteButton;
    private Spinner dayChooseSpinner, courseChooseSpinner;
    private ArrayList<String> daysArray;
    private EditText location;
    private HashMap<String, String> courseList;
    private ArrayList<String> courseNameList;
    private LinearLayout rootLayout;
    private TextView errorTextView;

    private int id = -1;

    public static final String INT_EXTRA = "integer_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_lecture);

        setUpToolbar();

        initializeStringVariables();

        initializeAllViews();

        getCourseData();

        checkEditMode();

    }

    private void checkEditMode(){
        id = getIntent().getIntExtra(INT_EXTRA,-1);
        if(id > 0){
            getRequiredLecture(id);
            enableEditMode();
        }
    }

    private void enableEditMode(){
        deleteButton.setVisibility(View.VISIBLE);
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.new_lecture_toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setTitle(R.string.title_activity_new_lecture);
        setSupportActionBar(toolbar);
    }

    private void initializeStringVariables() {
        daysArray = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.five_day_array)));
        courseList = new HashMap<>();
        courseNameList = new ArrayList<>();
    }


    private void initializeAllViews() {
        rootLayout = (LinearLayout) findViewById(R.id.lectureRootLayout);
        location = (EditText) findViewById(R.id.lecture_location);

        startTimePicker = (TimePicker) findViewById(R.id.startTimePicker);
        endTimePicker = (TimePicker) findViewById(R.id.endTimePicker);

        dayChooseSpinner = (Spinner) findViewById(R.id.lecture_add_spinner);
        courseChooseSpinner = (Spinner) findViewById(R.id.course_choose_spinner);

        CustomSpinnerAdapter spinnerAdapter =
                new CustomSpinnerAdapter(this, R.layout.support_simple_spinner_dropdown_item, daysArray);
        dayChooseSpinner.setAdapter(spinnerAdapter);

        courseAdapter = new CustomSpinnerAdapter(this,
                R.layout.support_simple_spinner_dropdown_item,
                courseNameList);
        courseChooseSpinner.setAdapter(courseAdapter);

        errorTextView = (TextView)findViewById(R.id.errorLabel);

        button = (Button) findViewById(R.id.save_new_lecture_button);
        deleteButton = (Button) findViewById(R.id.delete_lecture_button);

        button.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
    }

    private void disableAllViews() {
        startTimePicker.setEnabled(false);
        endTimePicker.setEnabled(false);
        courseChooseSpinner.setEnabled(false);
        location.setEnabled(false);
        dayChooseSpinner.setEnabled(false);
        button.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void enableAllViews() {
        startTimePicker.setEnabled(true);
        endTimePicker.setEnabled(true);
        courseChooseSpinner.setEnabled(true);
        location.setEnabled(true);
        dayChooseSpinner.setEnabled(true);
        button.setEnabled(true);
        deleteButton.setEnabled(true);
        errorTextView.setVisibility(View.GONE);
    }

    private boolean checkValidityOfData() {
        boolean status = true;
        disableAllViews();
        int start_hour = startTimePicker.getCurrentHour();
        int start_minute = startTimePicker.getCurrentMinute();
        int end_hour = endTimePicker.getCurrentHour();
        int end_minute = endTimePicker.getCurrentMinute();

        if ((start_hour > end_hour) || (start_hour == end_hour && start_minute >= end_minute)) {
            errorTextView.setVisibility(View.VISIBLE);
            status = false;
        }
        if (location.getText().toString().trim().equals("")) {
            location.setError("Please Enter a Valid Location");
            status = false;
        }
        return status;
    }

    private Lecture createALecture() {
        String courseName = courseChooseSpinner.getSelectedItem().toString();
        String courseId = courseList.get(courseName);
        int lectureDay = dayChooseSpinner.getSelectedItemPosition();
        String startTime = startTimePicker.getCurrentHour() + ":" + startTimePicker.getCurrentMinute();
        String endTime = endTimePicker.getCurrentHour() + ":" + endTimePicker.getCurrentMinute();
        String location_text = location.getText().toString();
        return new Lecture(id,courseId, courseName, startTime, endTime, lectureDay, location_text);
    }

    private void submitData() {
        Lecture lecture = createALecture();
        if(id < 0) {
            SubmitDataTask submitDataTask = new SubmitDataTask(this);
            submitDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, lecture);
        } else {
            UpdateDataTask updateDataTask = new UpdateDataTask(this);
            updateDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,lecture);
        }

    }

    private void getCourseData() {
        CourseIdTask courseIdTask = new CourseIdTask(this);
        courseIdTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void exitEarly() {
        setResult(HomeActivity.RESULT_NO_COURSE);
        finish();
    }

    private void deleteData(){
        DeleteDataTask deleteDataTask = new DeleteDataTask(this);
        deleteDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, id);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.save_new_lecture_button){
            if (checkValidityOfData()) {
                submitData();
            } else {
                enableAllViews();
            }
        } else {
            deleteData();
        }

    }

    private void exit_properly() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(HomeActivity.DAY_OF_WEEK,
                dayChooseSpinner.getSelectedItemPosition());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void updated_properly(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(HomeActivity.DAY_OF_WEEK,
                dayChooseSpinner.getSelectedItemPosition());
        setResult(HomeActivity.RESULT_UPDATE_LECTURE, resultIntent);
        finish();
    }

    private void deleted_properly(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(HomeActivity.DAY_OF_WEEK,
                dayChooseSpinner.getSelectedItemPosition());
        setResult(HomeActivity.RESULT_DELETE_LECTURE, resultIntent);
        finish();
    }

    private void showFailure() {
        Snackbar.make(rootLayout, "An Error Occurred, Try Later", Snackbar.LENGTH_SHORT).show();
        enableAllViews();
    }

    private void getRequiredLecture(int x){
        GetLectureTask getLectureTask = new GetLectureTask(this);
        getLectureTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, x);
    }

    private void setAppropriateValues(Lecture lecture){
        String[] start_time = lecture.getStartTime().split(":");
        String[] end_time = lecture.getEndTime().split(":");

        int start_time_hour = Integer.parseInt(start_time[0]);
        int start_time_minute = Integer.parseInt(start_time[1]);

        int end_time_hour = Integer.parseInt(end_time[0]);
        int end_time_minute = Integer.parseInt(end_time[1]);

        startTimePicker.setCurrentHour(start_time_hour);
        startTimePicker.setCurrentMinute(start_time_minute);

        endTimePicker.setCurrentHour(end_time_hour);
        endTimePicker.setCurrentMinute(end_time_minute);

        location.setText(lecture.getLocation());

        dayChooseSpinner.setSelection(lecture.getDay() - Calendar.MONDAY);
        courseChooseSpinner.setSelection(reverseArrayList(lecture.getCourseName()));
    }

    private int reverseArrayList(String courseName){
        int i = 0;
        for (String course:courseNameList){
            if(course.equals(courseName)){
                break;
            }
            i++;
        }
        return i;
    }

    private class GetLectureTask extends AsyncTask<Integer, Void, Lecture>{

        private DatabaseAPI databaseAPI;
        public GetLectureTask(Context context){
            databaseAPI = new DatabaseAPI(context);
        }

        @Override
        protected Lecture doInBackground(Integer... integers) {
            return databaseAPI.getLecture(integers[0]);
        }

        @Override
        protected void onPostExecute(Lecture lecture) {
            super.onPostExecute(lecture);
            if (lecture != null)
            setAppropriateValues(lecture);
            else {
                exitEarly();
            }
        }
    }

    private class CourseIdTask extends AsyncTask<Void, Void, HashMap<String, String>> {

        private DatabaseAPI databaseAPI;

        public CourseIdTask(Context context) {
            databaseAPI = new DatabaseAPI(context);
        }

        @Override
        protected HashMap<String, String> doInBackground(Void... voids) {
            return databaseAPI.getAllCourseIds();
        }

        @Override
        protected void onPostExecute(HashMap<String, String> strings) {
            if (strings != null && strings.size() > 0) {
                courseList.clear();
                courseNameList.clear();
                courseList.putAll(strings);
                courseNameList.addAll(strings.keySet());
                courseAdapter.notifyDataSetChanged();
            } else {
                exitEarly();
            }
        }
    }

    private class SubmitDataTask extends AsyncTask<Lecture, Void, Boolean> {

        private DatabaseAPI databaseAPI;

        public SubmitDataTask(Context context) {
            databaseAPI = new DatabaseAPI(context);
        }

        @Override
        protected Boolean doInBackground(Lecture... lectures) {
            return databaseAPI.addLecture(lectures[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                exit_properly();
            } else {
                showFailure();
            }
        }
    }

    private class UpdateDataTask extends AsyncTask<Lecture, Void, Boolean> {

        private DatabaseAPI databaseAPI;

        public UpdateDataTask(Context context){
            databaseAPI = new DatabaseAPI(context);
        }

        @Override
        protected Boolean doInBackground(Lecture... lectures) {
            return databaseAPI.updateLecture(lectures[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean){
                updated_properly();
            } else {
                showFailure();
            }
        }
    }

    private class DeleteDataTask extends AsyncTask<Integer, Void, Integer> {

        private DatabaseAPI databaseAPI;

        public DeleteDataTask(Context context){
            databaseAPI = new DatabaseAPI(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            disableAllViews();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            return databaseAPI.deleteLecture(integers[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer > 0){
                deleted_properly();
            } else {
                showFailure();
                enableAllViews();
            }

        }
    }
}
