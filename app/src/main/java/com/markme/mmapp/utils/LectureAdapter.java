package com.markme.mmapp.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.markme.mmapp.R;
import com.markme.mmapp.data.Lecture;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.LectureViewHolder>{

    private ArrayList<Lecture> lectureArrayList;
    private LectureInterface lectureInterface;

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
        final Lecture lecture = lectureArrayList.get(position);
        holder.courseNameView.setText(lecture.getCourseName());
        holder.courseIdView.setText(lecture.getCourseId());
        holder.startTimeView.setText(convertTo12Hour(lecture.getStartTime()));
        holder.endTimeView.setText(convertTo12Hour(lecture.getEndTime()));
        holder.locationTextView.setText(lecture.getLocation());
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(lectureInterface != null)
                  lectureInterface.updateLecture(lecture.getId());
            }
        });
    }

    private String convertTo12Hour(String time){
        DateFormat inputFormat = new SimpleDateFormat("H:m", Locale.ENGLISH);
        DateFormat outputFormat = new SimpleDateFormat("h:m a", Locale.ENGLISH);
        try {
            Date date = inputFormat.parse(time);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return time;
        }
    }

    @Override
    public int getItemCount() {
        return lectureArrayList.size();
    }

    public void setLectureInterface(LectureInterface lectureInterface) {
        this.lectureInterface = lectureInterface;
    }

    protected static class LectureViewHolder extends RecyclerView.ViewHolder{

        public TextView courseNameView;
        public TextView courseIdView;
        public TextView startTimeView;
        public TextView endTimeView;
        public TextView locationTextView;
        public Button editButton;

        public LectureViewHolder(View itemView) {
            super(itemView);
            courseNameView = (TextView)itemView.findViewById(R.id.courseNameTextView);
            courseIdView = (TextView)itemView.findViewById(R.id.courseIdTextView);
            startTimeView = (TextView)itemView.findViewById(R.id.courseStartTime);
            endTimeView = (TextView)itemView.findViewById(R.id.courseEndTime);
            locationTextView = (TextView)itemView.findViewById(R.id.locationTextView);
            editButton = (Button)itemView.findViewById(R.id.editButton);
        }
    }

    public interface LectureInterface{
         void updateLecture(int lec_id);
    }
}
