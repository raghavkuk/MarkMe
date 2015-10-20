package com.markme.mmapp.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.markme.mmapp.R;
import com.markme.mmapp.data.Lecture;

import java.util.ArrayList;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.LectureViewHolder>{

    private Context context;
    private ArrayList<Lecture> lectureArrayList;

    public LectureAdapter(Context context, ArrayList<Lecture> lectureArrayList){
        this.context = context;
        this.lectureArrayList = lectureArrayList;
    }

    @Override
    public LectureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(context).inflate(R.layout.lecture_card_view, parent, false);
        return new LectureViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LectureViewHolder holder, int position) {
        Lecture lecture = lectureArrayList.get(position);
        holder.courseIdView.setText(lecture.getCourseId());
        holder.courseNameView.setText(lecture.getCourseName());
        holder.startTimeView.setText(lecture.getStartTime());
        holder.endTimeView.setText(lecture.getEndTime());
    }

    @Override
    public int getItemCount() {
        return lectureArrayList.size();
    }

    protected static class LectureViewHolder extends RecyclerView.ViewHolder{

        public TextView courseNameView;
        public TextView courseIdView;
        public TextView startTimeView;
        public TextView endTimeView;
        public LectureViewHolder(View itemView) {
            super(itemView);
            courseNameView = (TextView)itemView.findViewById(R.id.courseNameTextView);
            courseIdView = (TextView)itemView.findViewById(R.id.courseIdTextView);
            startTimeView = (TextView)itemView.findViewById(R.id.courseStartTime);
            endTimeView = (TextView)itemView.findViewById(R.id.courseEndTime);
        }
    }
}
