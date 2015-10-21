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

import com.markme.mmapp.R;
import com.markme.mmapp.data.Course;
import com.markme.mmapp.db.DatabaseAPI;
import com.markme.mmapp.utils.CoursesAdapter;

import java.util.ArrayList;

public class CoursesFragment extends Fragment {

    private Activity activity;
    private CoursesAdapter coursesAdapter;
    private ArrayList<Course> courseArrayList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_courses, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.courses_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager coursesLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(coursesLayoutManager);

        courseArrayList = new ArrayList<>();
        coursesAdapter = new CoursesAdapter(courseArrayList);
        recyclerView.setAdapter(coursesAdapter);

        getData();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    public void getData(){
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
            return databaseAPI.getAllCourses();
        }

        @Override
        protected void onPostExecute(ArrayList<Course> courses) {
            courseArrayList.clear();
            courseArrayList.addAll(courses);
            coursesAdapter.notifyDataSetChanged();
        }
    }
}
