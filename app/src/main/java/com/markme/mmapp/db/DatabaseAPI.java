package com.markme.mmapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import com.markme.mmapp.data.Course;
import com.markme.mmapp.data.Lecture;
import com.markme.mmapp.db.DatabaseContract.CourseTable;
import com.markme.mmapp.db.DatabaseContract.LectureTable;

import java.util.ArrayList;

public class DatabaseAPI {

    private Context mContext;

    public DatabaseAPI(Context context) {
        this.mContext = context;
    }

    /**
     * ******************************************
     * COURSE TABLE API
     * *******************************************
     */
    public boolean isCoursePresent(String courseId) {

        boolean result = false;

        Cursor cursor = this.mContext.getContentResolver().query(
                CourseTable.CONTENT_URI,
                new String[]{CourseTable.COLUMN_COURSE_INST_ID},
                CourseTable.COLUMN_COURSE_INST_ID + "=?",
                new String[]{courseId},
                null);

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                result = true;
            }
            cursor.close();
        }
        return result;
    }

    public boolean addCourse(Course newCourse) {

        boolean result = false;

        if (isCoursePresent(newCourse.getCourseId())) {
            Log.d("COURSE ALREADY EXISTS", "Course: " + newCourse.getCourseName() + " already exists.");
            return result;
        }

        ContentValues cv = createCourseValues(newCourse);

        try {
            Uri uri = this.mContext.getContentResolver().insert(CourseTable.CONTENT_URI, cv);
            if (uri != null) {
                result = true;
            }
        } catch (SQLiteException e) {
            Log.d("INSERTION FAILED", "Unable to add course: " + newCourse.getCourseId());
            e.printStackTrace();
        }

        return result;
    }

    public ContentValues createCourseValues(Course course) {

        ContentValues courseValues = new ContentValues();

        courseValues.put(CourseTable.COLUMN_COURSE_INST_ID, course.getCourseId());
        courseValues.put(CourseTable.COLUMN_COURSE_NAME, course.getCourseName());
        courseValues.put(CourseTable.COLUMN_COURSE_ENGAGED_LECTURES, course.getLecturesEngaged());
        courseValues.put(CourseTable.COLUMN_COURSE_ATTENDED_LECTURES, course.getLecturesAttended());
        courseValues.put(CourseTable.COLUMN_COURSE_MAX_LECTURES, course.getTotalLectures());
        courseValues.put(CourseTable.COLUMN_COURSE_MIN_ATTENDANCE, course.getMinAttendance());

        return courseValues;
    }

    public boolean updateCourse(Course newValues) {

        boolean result = false;

        ContentValues cv = new ContentValues();
        cv.put(CourseTable.COLUMN_COURSE_NAME, newValues.getCourseName());
        cv.put(CourseTable.COLUMN_COURSE_ENGAGED_LECTURES, newValues.getLecturesEngaged());
        cv.put(CourseTable.COLUMN_COURSE_ATTENDED_LECTURES, newValues.getLecturesAttended());
        cv.put(CourseTable.COLUMN_COURSE_MIN_ATTENDANCE, newValues.getMinAttendance());
        cv.put(CourseTable.COLUMN_COURSE_MAX_LECTURES, newValues.getTotalLectures());

        long numRowsAffected = this.mContext.getContentResolver().update(
                CourseTable.CONTENT_URI,
                cv,
                CourseTable.COLUMN_COURSE_INST_ID + "=? ",
                new String[]{newValues.getCourseId()});

        if (numRowsAffected > 0) {
            result = true;
        }

        return result;
    }

    public ArrayList<Course> getAllCourses(){

        ArrayList<Course> allCourses = new ArrayList<Course>();

        Cursor cursor = this.mContext.getContentResolver().query(
                CourseTable.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if(cursor != null)
            while(cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex(CourseTable.COLUMN_COURSE_INST_ID));
                String name = cursor.getString(cursor.getColumnIndex(CourseTable.COLUMN_COURSE_NAME));
                int max = cursor.getInt(cursor.getColumnIndex(CourseTable.COLUMN_COURSE_MAX_LECTURES));
                int engaged = cursor.getInt(cursor.getColumnIndex(CourseTable.COLUMN_COURSE_ENGAGED_LECTURES));
                int attended = cursor.getInt(cursor.getColumnIndex(CourseTable.COLUMN_COURSE_ATTENDED_LECTURES));
                int minimum = cursor.getInt(cursor.getColumnIndex(CourseTable.COLUMN_COURSE_MIN_ATTENDANCE));
                Course course = new Course(id, name, max, engaged, attended,minimum);
                allCourses.add(course);
            }

        return allCourses;
    }

    public Course getCourse(String courseId){

        Course course;

        Cursor cursor = this.mContext.getContentResolver().query(
                CourseTable.CONTENT_URI,
                null,
                CourseTable.COLUMN_COURSE_INST_ID + "=? ",
                new String[]{courseId},
                null
        );

        if(cursor != null){
            String id = cursor.getString(cursor.getColumnIndex(CourseTable.COLUMN_COURSE_INST_ID));
            String name = cursor.getString(cursor.getColumnIndex(CourseTable.COLUMN_COURSE_NAME));
            int max = cursor.getInt(cursor.getColumnIndex(CourseTable.COLUMN_COURSE_MAX_LECTURES));
            int engaged = cursor.getInt(cursor.getColumnIndex(CourseTable.COLUMN_COURSE_ENGAGED_LECTURES));
            int attended = cursor.getInt(cursor.getColumnIndex(CourseTable.COLUMN_COURSE_ATTENDED_LECTURES));
            int minimum = cursor.getInt(cursor.getColumnIndex(CourseTable.COLUMN_COURSE_MIN_ATTENDANCE));
            course = new Course(id, name, max, engaged, attended,minimum);
        }else{
            course = null;
        }

        return course;
    }

    public int deleteCourse(String courseId){

        int rowsAffected = this.mContext.getContentResolver().delete(
                CourseTable.CONTENT_URI,
                CourseTable.COLUMN_COURSE_INST_ID + "=? ",
                new String[]{courseId}
        );

        return rowsAffected;
    }


    /**
     * ******************************************
     * LECTURE TABLE API
     * *******************************************
     */
    public boolean isLecturePresent(Lecture lecture) {

        boolean result = false;

        Cursor cursor = this.mContext.getContentResolver().query(
                LectureTable.CONTENT_URI,
                null,
                LectureTable.COLUMN_LECTURE_START_TIME + "=? and " + LectureTable.COLUMN_LECTURE_DAY + "=?",
                new String[]{lecture.getStartTime(), String.valueOf(lecture.getDay())},
                null);

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                result = true;
            }
            cursor.close();
        }
        return result;
    }

    public boolean addLecture(Lecture newLecture) {

        boolean result = false;

        if (isLecturePresent(newLecture)) {
            Log.d("LECTURE ALREADY EXISTS", "Lecture: " + newLecture.getCourseName() + " already exists.");
            return result;
        }

        ContentValues cv = createLectureValues(newLecture);

        try {
            Uri uri = this.mContext.getContentResolver().insert(LectureTable.CONTENT_URI, cv);
            if (uri != null) {
                result = true;
            }
        } catch (SQLiteException e) {
            Log.d("INSERTION FAILED", "Unable to add lecture: " + newLecture.getCourseId());
            e.printStackTrace();
        }

        return result;
    }

    public ContentValues createLectureValues(Lecture lecture) {

        ContentValues lectureValues = new ContentValues();

        lectureValues.put(LectureTable.COLUMN_LECTURE_COURSE_ID, lecture.getCourseId());
        lectureValues.put(LectureTable.COLUMN_LECTURE_COURSE_NAME, lecture.getCourseName());
        lectureValues.put(LectureTable.COLUMN_LECTURE_START_TIME, lecture.getStartTime());
        lectureValues.put(LectureTable.COLUMN_LECTURE_END_TIME, lecture.getEndTime());
        lectureValues.put(LectureTable.COLUMN_LECTURE_DAY, lecture.getDay());
        lectureValues.put(LectureTable.COLUMN_LECTURE_LOCATION, lecture.getLocation());

        return lectureValues;
    }

    public boolean updateLecture(Lecture oldValues, Lecture newValues) {

        boolean result = false;

        ContentValues cv = new ContentValues();
        cv.put(LectureTable.COLUMN_LECTURE_START_TIME, newValues.getStartTime());
        cv.put(LectureTable.COLUMN_LECTURE_END_TIME, newValues.getEndTime());
        cv.put(LectureTable.COLUMN_LECTURE_DAY, newValues.getDay());
        cv.put(LectureTable.COLUMN_LECTURE_LOCATION, newValues.getLocation());

        long numRowsAffected = this.mContext.getContentResolver().update(
                LectureTable.CONTENT_URI,
                cv,
                LectureTable.COLUMN_LECTURE_START_TIME + "=? and " + LectureTable.COLUMN_LECTURE_DAY + "=?",
                new String[]{oldValues.getStartTime(), String.valueOf(oldValues.getDay())});

        if (numRowsAffected > 0) {
            result = true;
        }

        return result;
    }

    public ArrayList<Lecture> getAllLectures(Course course, int mDay){

        ArrayList<Lecture> allLectures = new ArrayList<Lecture>();

        Cursor cursor = this.mContext.getContentResolver().query(
                LectureTable.CONTENT_URI,
                null,
                LectureTable.COLUMN_LECTURE_COURSE_ID + "=? and " + LectureTable.COLUMN_LECTURE_DAY + "=?",
                new String[]{course.getCourseId(), String.valueOf(mDay)},
                null
        );

        if(cursor != null)
            while(cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex(LectureTable.COLUMN_LECTURE_COURSE_ID));
                String name = cursor.getString(cursor.getColumnIndex(LectureTable.COLUMN_LECTURE_COURSE_NAME));
                String startTime = cursor.getString(cursor.getColumnIndex(LectureTable.COLUMN_LECTURE_START_TIME));
                String endTime = cursor.getString(cursor.getColumnIndex(LectureTable.COLUMN_LECTURE_END_TIME));
                int dayOfWeek = cursor.getInt(cursor.getColumnIndex(LectureTable.COLUMN_LECTURE_DAY));
                String location = cursor.getString(cursor.getColumnIndex(LectureTable.COLUMN_LECTURE_LOCATION));
                Lecture lecture = new Lecture(id, name, startTime, endTime, dayOfWeek, location);
                allLectures.add(lecture);
            }

        return allLectures;
    }

    public Lecture getLecture(Course course, int mDay, String mStartTime){

        Lecture lecture;

        Cursor cursor = this.mContext.getContentResolver().query(
                LectureTable.CONTENT_URI,
                null,
                LectureTable.COLUMN_LECTURE_COURSE_ID + "=? and " + LectureTable.COLUMN_LECTURE_DAY + "=? and " + LectureTable.COLUMN_LECTURE_START_TIME + "=?",
                new String[]{course.getCourseId(), String.valueOf(mDay), mStartTime},
                null
        );

        if(cursor != null){
                String id = cursor.getString(cursor.getColumnIndex(LectureTable.COLUMN_LECTURE_COURSE_ID));
                String name = cursor.getString(cursor.getColumnIndex(LectureTable.COLUMN_LECTURE_COURSE_NAME));
                String startTime = cursor.getString(cursor.getColumnIndex(LectureTable.COLUMN_LECTURE_START_TIME));
                String endTime = cursor.getString(cursor.getColumnIndex(LectureTable.COLUMN_LECTURE_END_TIME));
                int dayOfWeek = cursor.getInt(cursor.getColumnIndex(LectureTable.COLUMN_LECTURE_DAY));
                String location = cursor.getString(cursor.getColumnIndex(LectureTable.COLUMN_LECTURE_LOCATION));
                lecture = new Lecture(id, name, startTime, endTime, dayOfWeek, location);
        }else{
            lecture = null;
        }

        return lecture;
    }

    public int deleteLecture(Course course, String startTime, int day){

        int rowsAffected = this.mContext.getContentResolver().delete(
                LectureTable.CONTENT_URI,
                LectureTable.COLUMN_LECTURE_COURSE_ID + "=? and " + LectureTable.COLUMN_LECTURE_START_TIME + "=? and " + LectureTable.COLUMN_LECTURE_DAY + "=?",
                new String[]{course.getCourseId(), startTime, String.valueOf(day)}
        );

        return rowsAffected;
    }

}
