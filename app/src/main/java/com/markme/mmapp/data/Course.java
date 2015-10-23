package com.markme.mmapp.data;


public class Course {
    private String courseId;
    private String courseName;
    private int totalLectures;
    private int lecturesEngaged;
    private int lecturesAttended;
    private int id;
    private double minAttendance;

    public Course(int id, String courseId, String courseName,
                  int totalLectures, int lecturesEngaged, int lecturesAttended, double minAttendance){
        this.id = id;
        this.courseId = courseId;
        this.courseName = courseName;
        this.totalLectures = totalLectures;
        this.lecturesEngaged = lecturesEngaged;
        this.lecturesAttended = lecturesAttended;
        this.minAttendance = minAttendance;
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

    public double getMinAttendance() {
        return minAttendance;
    }

    public int getId() {
        return id;
    }
}
