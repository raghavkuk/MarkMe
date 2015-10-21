package com.markme.mmapp.utils;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.markme.mmapp.R;
import com.markme.mmapp.data.Course;

import java.util.ArrayList;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CourseViewHolder> {

    private ArrayList<Course> courses;

    public CoursesAdapter(ArrayList<Course> courses){
        this.courses = courses;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_card_view,
                                                                                    parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {

        holder.courseName.setText(courses.get(position).getCourseName());
        holder.courseId.setText(courses.get(position).getCourseId());

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }


    public static class CourseViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView courseName;
        TextView courseId;
        ImageView editIcon;

        CourseViewHolder(View itemView) {
            super(itemView);
            if(itemView instanceof CardView){
                cardView = (CardView)itemView;
                courseName = (TextView)cardView.findViewById(R.id.course_name);
                courseId = (TextView)itemView.findViewById(R.id.course_id);
                editIcon = (ImageView)itemView.findViewById(R.id.edit_icon);
            }

        }

    }
}
