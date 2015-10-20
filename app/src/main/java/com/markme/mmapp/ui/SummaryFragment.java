package com.markme.mmapp.ui;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markme.mmapp.R;
import com.markme.mmapp.data.Course;
import com.markme.mmapp.db.DatabaseAPI;

import java.util.ArrayList;

public class SummaryFragment extends Fragment {

    private SummaryAdapter summaryAdapter;
    private ArrayList<Course> courseData;
    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summary, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.summaryRecyclerView);
        courseData = new ArrayList<>();
        summaryAdapter = new SummaryAdapter(activity,courseData);
        recyclerView.setAdapter(summaryAdapter);
        getData();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    private void getData(){
        FetchDataTask fetchDataTask = new FetchDataTask(activity);
        fetchDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class FetchDataTask extends AsyncTask<Void,Void,ArrayList<Course>>{

        private DatabaseAPI databaseAPI;
        public FetchDataTask(Context context){
            databaseAPI = new DatabaseAPI(context);
        }

        @Override
        protected ArrayList<Course> doInBackground(Void... voids) {
            // courseData.addAll(null);
            //TODO: Database API for all courses
            return courseData;
        }

        @Override
        protected void onPostExecute(ArrayList<Course> courses) {
            courseData.clear();
            courseData.addAll(courses);
            summaryAdapter.notifyDataSetChanged();
        }
    }
}
