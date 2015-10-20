package com.markme.mmapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.markme.mmapp.R;

public class TimeTableFragment extends Fragment {


    private Spinner spinner;
    private RecyclerView lectureRecyclerView;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timetable, container, false);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progress);
        lectureRecyclerView = (RecyclerView)rootView.findViewById(R.id.lectureRecyclerView);
        spinner = (Spinner)rootView.findViewById(R.id.daySpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return rootView;
    }


}
