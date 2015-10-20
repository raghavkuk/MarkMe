package com.markme.mmapp.data;

/**
 * Created by raghav on 18/10/15.
 */
public class Course {
    private String courseId;
    private String courseName;
    private int totalLectures;
    private int lecturesEngaged;
    private int lecturesAttended;

    public Course(String courseId, String courseName,
                  int totalLectures, int lecturesEngaged, int lecturesAttended){
        this.courseId = courseId;
        this.courseName = courseName;
        this.totalLectures = totalLectures;
        this.lecturesEngaged = lecturesEngaged;
        this.lecturesAttended = lecturesAttended;
    }


    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getTotalLectures() {
        return totalLectures;
    }

    public int getLecturesEngaged() {
        return lecturesEngaged;
    }

    public int getLecturesAttended() {
        return lecturesAttended;
    }
}