package com.markme.mmapp.data;

public class Lecture {
    private String courseId;
    private String courseName;
    private String startTime;
    private String endTime;
    private String day;

    public Lecture(String courseId,
                   String courseName, String startTime, String endTime, String day){
        this.courseId = courseId;
        this.courseName = courseName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
    }


    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDay() {
        return day;
    }
}
