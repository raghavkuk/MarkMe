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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.markme.mmapp.R;
import com.markme.mmapp.data.Lecture;
import com.markme.mmapp.db.DatabaseAPI;
import com.markme.mmapp.utils.CustomSpinnerAdapter;
import com.markme.mmapp.utils.LectureAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class TimeTableFragment extends Fragment implements LectureAdapter.LectureInterface{


    private Spinner spinner;
    private RecyclerView lectureRecyclerView;
    private ProgressBar progressBar;
    private TextView textView;
    private ArrayList<Lecture> lectureArrayList;
    private LectureAdapter lectureAdapter;
    private Activity activity;
    private DatabaseAPI dbApi;
    private TimeTableInteractionListener timeTableInteractionListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timetable, container, false);
        dbApi = new DatabaseAPI(getActivity());
        initializeAllViews(rootView);
        setSpinnerListener();
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        if (day >= Calendar.MONDAY && day <= Calendar.FRIDAY)
        spinner.setSelection(day - Calendar.MONDAY);

        loadListBasedOnDay(day);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        try {
            timeTableInteractionListener = (TimeTableInteractionListener)activity;
        } catch (ClassCastException e){
            e.printStackTrace();
        }

    }

    private void initializeAllViews(View rootView){
        progressBar = (ProgressBar)rootView.findViewById(R.id.progress);

        lectureRecyclerView = (RecyclerView)rootView.findViewById(R.id.lectureRecyclerView);

        spinner = (Spinner)rootView.findViewById(R.id.daySpinner);
        CustomSpinnerAdapter spinnerAdapter =
                new CustomSpinnerAdapter(activity,
                        R.layout.support_simple_spinner_dropdown_item,
                        new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.five_day_array))));
        spinner.setAdapter(spinnerAdapter);

        lectureArrayList = dbApi.getAllLectures(spinner.getSelectedItemPosition());
        lectureAdapter = new LectureAdapter(lectureArrayList);
        lectureAdapter.setLectureInterface(this);
        lectureRecyclerView.setAdapter(lectureAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        lectureRecyclerView.setLayoutManager(layoutManager);

        textView = (TextView)rootView.findViewById(R.id.labelTextView);
    }

    private void setSpinnerListener(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadListBasedOnDay(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadListBasedOnDay(int day){
        LoadLecturesTask loadLecturesTask = new LoadLecturesTask(activity);
        loadLecturesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, day);
    }

    public void changeSpinnerDay(int day){
        spinner.setSelection(day);
        loadListBasedOnDay(day);
    }

    @Override
    public void updateLecture(int lec_id) {
        timeTableInteractionListener.editTimeTable(lec_id);
    }

    public interface TimeTableInteractionListener{
        void editTimeTable(int x);
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
