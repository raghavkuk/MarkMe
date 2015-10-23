package com.markme.mmapp.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.markme.mmapp.R;
import com.markme.mmapp.data.Lecture;

import java.util.ArrayList;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.LectureViewHolder>{

    private ArrayList<Lecture> lectureArrayList;

    public LectureAdapter(ArrayList<Lecture> lectureArrayList){
        this.lectureArrayList = lectureArrayList;
    }

    @Override
    public LectureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.lecture_card_view,
                                                                                    parent, false);
        return new LectureViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LectureViewHolder holder, int position) {
        Lecture lecture = lectureArrayList.get(position);
        holder.courseNameView.setText(lecture.getCourseName());
        holder.lectureTime.setText(lecture.getStartTime()+" - "+lecture.getEndTime());
        holder.lectureLocation.setText(lecture.getLocation());
    }

    @Override
    public int getItemCount() {
        return lectureArrayList.size();
    }

    protected static class LectureViewHolder extends RecyclerView.ViewHolder{

        public TextView courseNameView;
        public TextView lectureTime;
        public TextView lectureLocation;
        public LectureViewHolder(View itemView) {
            super(itemView);
            courseNameView = (TextView)itemView.findViewById(R.id.courseNameTextView);
            lectureTime = (TextView)itemView.findViewById(R.id.lecture_time);
            lectureLocation = (TextView)itemView.findViewById(R.id.lecture_location);
        }
    }
}
