package com.markme.mmapp.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.markme.mmapp.R;
import com.markme.mmapp.data.Course;

import java.util.ArrayList;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CourseViewHolder> {

    private ArrayList<Course> courses;
    private CourseInterface courseInterface;

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

        final Course course = courses.get(position);

        holder.courseName.setText(course.getCourseName());
        holder.courseId.setText(course.getCourseId());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (courseInterface != null)
                    courseInterface.edit_course(course.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void setCourseInterface(CourseInterface courseInterface) {
        this.courseInterface = courseInterface;
    }

    public interface CourseInterface {
        void edit_course(int course_id);
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder{

        TextView courseName;
        TextView courseId;
        Button button;

        CourseViewHolder(View itemView) {
            super(itemView);
                courseName = (TextView)itemView.findViewById(R.id.course_name);
                courseId = (TextView)itemView.findViewById(R.id.course_id);
                button = (Button)itemView.findViewById(R.id.course_edit_button);
        }

    }
}
