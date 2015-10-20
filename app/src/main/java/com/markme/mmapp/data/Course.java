package com.markme.mmapp.data;


public class Course {
    private String courseId;
    private String courseName;
    private int totalLectures;
    private int lecturesEngaged;
    private int lecturesAttended;
    private double minAttendance;

    public Course(String courseId, String courseName,
                  int totalLectures, int lecturesEngaged, int lecturesAttended, int minAttendance){
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

    public double getMinAttendance() {
        return minAttendance;
    }
}
