package com.markme.mmapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markme.mmapp.R;
import com.markme.mmapp.db.DatabaseAPI;
import com.markme.mmapp.utils.CoursesAdapter;

public class CoursesFragment extends Fragment {

    DatabaseAPI dbApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbApi = new DatabaseAPI(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_courses, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.courses_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager coursesLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(coursesLayoutManager);

        CoursesAdapter coursesAdapter = new CoursesAdapter(dbApi.getAllCourses());
        recyclerView.setAdapter(coursesAdapter);

        return rootView;
    }
}
