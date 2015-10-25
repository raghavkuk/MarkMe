package com.markme.mmapp.data;

public class Lecture {
    private String courseId;
    private String courseName;
    private String startTime;
    private String endTime;
    private int day;
    private int id;
    private String location;

    public Lecture(int id, String courseId,
                   String courseName, String startTime, String endTime, int day, String location){
        this.courseId = courseId;
        this.courseName = courseName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.location = location;
        this.id = id;
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

    public int getDay() {
        return day;
    }

    public String getLocation() {
        return location;
    }

    public int getId() {
        return id;
    }
}
