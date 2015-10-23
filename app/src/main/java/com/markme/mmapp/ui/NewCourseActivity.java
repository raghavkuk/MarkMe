package com.markme.mmapp.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.markme.mmapp.R;
import com.markme.mmapp.data.Course;
import com.markme.mmapp.db.DatabaseAPI;


public class NewCourseActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText courseId, courseName, engagedLectures,
                                                    attendedLectures, maxLectures, minAttendance;
    private Button saveButton,deleteButton;

    private LinearLayout rootLayout;

    private int edit_id;

    public static final String INT_EXTRA  = "course_int_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_new_course);
        edit_id = getIntent().getIntExtra(INT_EXTRA,-1);

        setUpToolBar();

        initializeAllViews();

        checkIfEditing();

    }

    private void checkIfEditing(){
        if (edit_id > 0){
            enable_editing();
        }
    }

    private void enable_editing(){
        deleteButton.setVisibility(View.VISIBLE);
        GetDataTask getDataTask = new GetDataTask(this);
        getDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, edit_id);
    }

    private void setUpToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.new_course_toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setTitle(R.string.title_activity_new_course);
        setSupportActionBar(toolbar);
    }

    private void initializeAllViews(){
        rootLayout = (LinearLayout) findViewById(R.id.courseRootLayout);
        courseId = (EditText) findViewById(R.id.new_course_id);
        courseName = (EditText) findViewById(R.id.new_course_name);
        engagedLectures = (EditText) findViewById(R.id.new_course_engaged_lectures);
        attendedLectures = (EditText) findViewById(R.id. new_course_attended_lectures);
        maxLectures = (EditText) findViewById(R.id.new_course_max_lectures);
        minAttendance = (EditText) findViewById(R.id. new_course_min_attendance);
        saveButton = (Button) findViewById(R.id.save_new_course_button);
        deleteButton = (Button) findViewById(R.id.delete_course_button);
        saveButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
    }

    private void disableAllViews(){
        courseId.setEnabled(false);
        courseName.setEnabled(false);
        engagedLectures.setEnabled(false);
        attendedLectures.setEnabled(false);
        maxLectures.setEnabled(false);
        minAttendance.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void enableAllViews(){
        courseId.setEnabled(true);
        courseName.setEnabled(true);
        engagedLectures.setEnabled(true);
        attendedLectures.setEnabled(true);
        maxLectures.setEnabled(true);
        minAttendance.setEnabled(true);
        saveButton.setEnabled(true);
        deleteButton.setEnabled(true);
    }

    private boolean checkIfValid(){
        boolean status = true;
        Integer attended;
        Integer engaged = null;
        if(courseId.getText().toString().trim().equals("")){
            courseId.setError("Please Enter a Valid Course Id");
            status = false;
        }
        if(courseName.getText().toString().trim().equals("")){
            courseName.setError("Please Enter a Valid Course Name");
            status = false;
        }
        if(engagedLectures.getText().toString().trim().equals("")){
            engagedLectures.setError("Please Enter The Number of Engaged Lectures");
            status = false;
        } else {
            try{
                engaged = Integer.parseInt(engagedLectures.getText().toString().trim());
                if(engaged < 0) {
                    status = false;
                    engagedLectures.setError("Engaged Lectures cannot be less than 0");
                }
            } catch (NumberFormatException e){
                status = false;
                engagedLectures.setError("Please Enter a Valid Number");
            }
        }

        if(attendedLectures.getText().toString().trim().equals("")){
            attendedLectures.setError("Please Enter The Number of Attended Lectures");
            status = false;
        } else {
            try{
                attended = Integer.parseInt(attendedLectures.getText().toString().trim());
                if(attended < 0) {
                    status = false;
                    attendedLectures.setError("Attended Lectures cannot be less than 0");
                } else {
                    if(engaged != null && engaged > 0){
                        if(attended > engaged){
                            status = false;
                            attendedLectures.setError("Attended Lectures cannot be greater than engaged lectures");
                        }
                    }
                }
            } catch (NumberFormatException e){
                status = false;
                attendedLectures.setError("Please Enter a Valid Number");
            }
        }

        if(minAttendance.getText().toString().equals("")){
            minAttendance.setError("Please Enter The Minimum Attendance required for this course");
            status = false;
        } else {
            try{
                double percent = Double.parseDouble(minAttendance.getText().toString().trim());
                if(percent < 0 || percent > 100) {
                    status = false;
                    minAttendance.setError("Min Attendance Req cannot be less than 0 or greater than 100");
                }
            } catch (NumberFormatException e){
                status = false;
                minAttendance.setError("Please Enter a Valid Number");
            }
        }

        if(!maxLectures.getText().toString().trim().equals("")){
            try{
                int maxLec = Integer.parseInt(maxLectures.getText().toString().trim());
                if(maxLec < 0) {
                    status = false;
                    maxLectures.setError("Maximum Lectures cannot be less than 0");
                } else {
                    if(engaged != null && engaged > 0){
                        if(maxLec < engaged){
                            status = false;
                            maxLectures.setError("Maximum Lectures cannot be less than engaged lectures");
                        }
                    }
                }
            } catch (NumberFormatException e){
                status = false;
                maxLectures.setError("Please Enter a Valid Number");
            }
        }
        return status;
    }

    private void createSnackBar(){
        Snackbar.make(rootLayout, "Course Already Present, Please Enter a different Value", Snackbar.LENGTH_SHORT).show();
    }

    private Course prepareCourse(){
        String id = courseId.getText().toString().trim();
        String name = courseName.getText().toString().trim();
        String maxString = maxLectures.getText().toString().trim();
        int maximum_lectures;
        if(maxString.equals("")){
            maximum_lectures = -1;
        } else {
            maximum_lectures = Integer.valueOf(maxString);
        }
        int engaged = Integer.valueOf(engagedLectures.getText().toString().trim());
        int attended = Integer.valueOf(attendedLectures.getText().toString().trim());
        double minimum = Double.valueOf(minAttendance.getText().toString().trim());
        return new Course(edit_id,id,name,maximum_lectures,engaged,attended,minimum);
    }

    private void submitTheData(){
        Course course = prepareCourse();
        if (edit_id < 0){
            EnterDataTask enterDataTask = new EnterDataTask(new DatabaseAPI(this));
            enterDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,course);
        } else {
            UpdateDataTask updateDataTask = new UpdateDataTask(this);
            updateDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, course);
        }
    }

    private void loadDetails(Course course){
        courseId.setText(course.getCourseId());
        courseName.setText(course.getCourseName());
        engagedLectures.setText(course.getLecturesEngaged()+"");
        attendedLectures.setText(course.getLecturesAttended()+"");
        minAttendance.setText(course.getMinAttendance()+"");
        int max_lectures = course.getTotalLectures();
        if(max_lectures > 0){
            maxLectures.setText(max_lectures+"");
        }
    }

    private void showFailure(){
        setResult(RESULT_CANCELED);
        finish();
    }

    private void deleteCourse(){
        DeleteDataTask deleteDataTask = new DeleteDataTask(this);
        deleteDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,edit_id);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.save_new_course_button){
            disableAllViews();
            if(checkIfValid()){
                submitTheData();
            } else {
                enableAllViews();
            }
        } else {
            deleteCourse();
        }

    }

    private void setAndFinish(){
        setResult(RESULT_OK);
        finish();
    }

    private void showUpdateSuccess(){
        setResult(HomeActivity.RESULT_UPDATE_COURSE);
        finish();
    }

    private void showDeleteSuccess(){
        setResult(HomeActivity.RESULT_DELETE_COURSE);
        finish();
    }

    private class EnterDataTask extends AsyncTask<Course,Void,Boolean>{

        private DatabaseAPI databaseAPI;
        public EnterDataTask(DatabaseAPI databaseAPI){
            this.databaseAPI = databaseAPI;
        }

        @Override
        protected Boolean doInBackground(Course... courses) {
            return databaseAPI.addCourse(courses[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                setAndFinish();
            } else {
                createSnackBar();
            }
        }
    }

    private class GetDataTask extends AsyncTask<Integer,Void,Course>{

        private DatabaseAPI databaseAPI;

        public GetDataTask(Context context){
            databaseAPI = new DatabaseAPI(context);
        }

        @Override
        protected Course doInBackground(Integer... integers) {
            return databaseAPI.getCourse(integers[0]);
        }

        @Override
        protected void onPostExecute(Course course) {
            super.onPostExecute(course);
            if(course != null){
                loadDetails(course);
            } else {
                showFailure();
            }
        }
    }

    private class UpdateDataTask extends AsyncTask<Course,Void,Boolean>{

        private DatabaseAPI databaseAPI;

        public UpdateDataTask(Context context){
            databaseAPI = new DatabaseAPI(context);
        }

        @Override
        protected Boolean doInBackground(Course... courses) {
            return databaseAPI.updateCourse(courses[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                showUpdateSuccess();
            } else {
                createSnackBar();
            }
        }
    }

    private class DeleteDataTask extends AsyncTask<Integer,Void,Integer>{

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
            return databaseAPI.deleteCourse(integers[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer > 0){
                showDeleteSuccess();
            } else {
                showFailure();
            }
        }
    }
}
