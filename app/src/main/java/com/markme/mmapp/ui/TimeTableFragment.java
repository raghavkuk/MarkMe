package com.markme.mmapp.ui;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.markme.mmapp.R;
import com.markme.mmapp.data.Lecture;
import com.markme.mmapp.db.DatabaseAPI;
import com.markme.mmapp.utils.LectureAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class TimeTableFragment extends Fragment {


    private Spinner spinner;
    private RecyclerView lectureRecyclerView;
    private ProgressBar progressBar;
    private TextView textView;
    private int lastPosition = 0;
    private ArrayList<Lecture> lectureArrayList;
    private LectureAdapter lectureAdapter;
    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timetable, container, false);
        initializeAllViews(rootView);
        setSpinnerListener();
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        loadListBasedOnDay(day);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    private void initializeAllViews(View rootView){
        progressBar = (ProgressBar)rootView.findViewById(R.id.progress);
        lectureRecyclerView = (RecyclerView)rootView.findViewById(R.id.lectureRecyclerView);
        spinner = (Spinner)rootView.findViewById(R.id.daySpinner);
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<>(activity,
                        android.R.layout.simple_spinner_dropdown_item,
                         getResources().getStringArray(R.array.five_day_array));
        spinner.setAdapter(spinnerAdapter);
        lectureArrayList = new ArrayList<>();
        lectureAdapter = new LectureAdapter(activity, lectureArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        lectureRecyclerView.setLayoutManager(layoutManager);
        lectureRecyclerView.setAdapter(lectureAdapter);
        textView = (TextView)rootView.findViewById(R.id.labelTextView);
    }

    private void setSpinnerListener(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != lastPosition) {
                    loadListBasedOnDay(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void loadListBasedOnDay(int day){
        LoadLecturesTask loadLecturesTask = new LoadLecturesTask(activity);
        loadLecturesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,day);
    }

    private class LoadLecturesTask extends AsyncTask<Integer,Void,ArrayList<Lecture>>{

        private DatabaseAPI databaseAPI;

        public LoadLecturesTask(Context context){
            databaseAPI = new DatabaseAPI(context);
        }
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            lectureRecyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }

        @Override
        protected ArrayList<Lecture> doInBackground(Integer... integers) {
            return databaseAPI.getAllLectures(integers[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Lecture> lectures) {
            if(lectures == null || lectures.size() == 0){
                textView.setVisibility(View.VISIBLE);
            } else {
                lectureArrayList.clear();
                lectureArrayList.addAll(lectures);
                lectureAdapter.notifyDataSetChanged();
                lectureRecyclerView.setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.GONE);
        }
    }
}
