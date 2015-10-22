package com.markme.mmapp.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.markme.mmapp.R;
import com.markme.mmapp.data.Lecture;
import com.markme.mmapp.db.DatabaseAPI;


public class NewLectureActivity extends AppCompatActivity implements View.OnClickListener{

    private Spinner courseName;
    private Button saveButton;
    private DatabaseAPI dbApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_new_lecture);
        dbApi = new DatabaseAPI(this);
        setUpToolBar();
        initializeAllViews();
    }

    private void setUpToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.new_lecture_toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setTitle(R.string.title_activity_new_lecture);
        setSupportActionBar(toolbar);
    }

    private void initializeAllViews(){
        courseName = (Spinner)findViewById(R.id.new_lecture_course_name);
        ArrayAdapter<String> courseNamesAdapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        dbApi.getAllCourseNames());
        courseName.setAdapter(courseNamesAdapter);
        saveButton = (Button) findViewById(R.id.save_new_lecture_button);
        saveButton.setOnClickListener(this);
    }

    private void disableAllViews(){
        courseName.setEnabled(false);
    }

    private void enableAllViews(){
        courseName.setEnabled(true);
        saveButton.setEnabled(true);
    }


    private void createSnackBar(){
        Toast.makeText(this,"Course with this ID already exists",Toast.LENGTH_SHORT).show();
        //TODO: Replace with Snackbar
        //TODO: change hint color
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    private Lecture prepareLecture(){
        String name = (String)courseName.getSelectedItem();
        return null;
    }

    private void submitTheData(){
        Lecture lecture = prepareLecture();
        EnterDataTask enterDataTask = new EnterDataTask(new DatabaseAPI(this));
        enterDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, lecture);
    }

    @Override
    public void onClick(View view) {
        disableAllViews();
        submitTheData();
    }

    private void setAndFinish(){
        setResult(RESULT_OK);
        finish();
    }

    private class EnterDataTask extends AsyncTask<Lecture,Void,Boolean>{

        private DatabaseAPI databaseAPI;
        public EnterDataTask(DatabaseAPI databaseAPI){
            this.databaseAPI = databaseAPI;
        }

        @Override
        protected Boolean doInBackground(Lecture... lectures) {
            return databaseAPI.addLecture(lectures[0]);
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
}
