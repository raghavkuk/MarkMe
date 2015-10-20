package com.markme.mmapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import com.markme.mmapp.data.Course;
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

    public boolean addCourse(String courseName, String courseId, int engagedLectures,
                             int attendedLectures, int maxLectures, int minAttendance) {

        boolean result = false;

        if (isCoursePresent(courseId)) {
            Log.d("COURSE ALREADY EXISTS", "Course: " + courseName + " already exists.");
            return result;
        }

        ContentValues cv = createCourseValues(courseName, courseId, engagedLectures, attendedLectures, maxLectures, minAttendance);

        try {
            Uri uri = this.mContext.getContentResolver().insert(CourseTable.CONTENT_URI, cv);
            if (uri != null) {
                result = true;
            }
        } catch (SQLiteException e) {
            Log.d("INSERTION FAILED", "Unable to add course: " + courseId);
            e.printStackTrace();
        }

        return result;
    }

    public ContentValues createCourseValues(String courseName, String courseId, int engagedLectures,
                                            int attendedLectures, int maxLectures, int minAttendance) {

        ContentValues courseValues = new ContentValues();

        courseValues.put(CourseTable.COLUMN_COURSE_INST_ID, courseId);
        courseValues.put(CourseTable.COLUMN_COURSE_NAME, courseName);
        courseValues.put(CourseTable.COLUMN_COURSE_ENGAGED_LECTURES, engagedLectures);
        courseValues.put(CourseTable.COLUMN_COURSE_ATTENDED_LECTURES, attendedLectures);
        courseValues.put(CourseTable.COLUMN_COURSE_MAX_LECTURES, maxLectures);
        courseValues.put(CourseTable.COLUMN_COURSE_MIN_ATTENDANCE, minAttendance);

        return courseValues;
    }

    public boolean updateEngagedLectures(String courseId, int updatedEngagedLectures) {

        boolean result = false;

        ContentValues cv = new ContentValues();
        cv.put(CourseTable.COLUMN_COURSE_ENGAGED_LECTURES, updatedEngagedLectures);

        long numRowsAffected = this.mContext.getContentResolver().update(
                CourseTable.CONTENT_URI,
                cv,
                CourseTable.COLUMN_COURSE_INST_ID + "=? ",
                new String[]{courseId.toString()});

        if (numRowsAffected > 0) {
            result = true;
        }

        return result;
    }

    public boolean updateAttendedLectures(String courseId, int updatedAttendedLectures) {

        boolean result = false;

        ContentValues cv = new ContentValues();
        cv.put(CourseTable.COLUMN_COURSE_ATTENDED_LECTURES, updatedAttendedLectures);

        long numRowsAffected = this.mContext.getContentResolver().update(
                CourseTable.CONTENT_URI,
                cv,
                CourseTable.COLUMN_COURSE_INST_ID + "=? ",
                new String[]{courseId.toString()});

        if (numRowsAffected > 0) {
            result = true;
        }

        return result;
    }

    public int getAttendedLectures(String courseId) {

        int attendedLectures;

        Cursor cursor = this.mContext.getContentResolver().query(
                CourseTable.CONTENT_URI,
                new String[]{CourseTable.COLUMN_COURSE_ATTENDED_LECTURES},
                CourseTable.COLUMN_COURSE_INST_ID + "=? ",
                new String[]{courseId},
                null
        );

        cursor.moveToFirst();
        if ((cursor != null) && (cursor.getCount() > 0))
            attendedLectures = cursor.getInt(0);
        else
            attendedLectures = 0;
        cursor.close();
        return attendedLectures;
    }

    public int getEngagedLectures(String courseId) {

        int engagedLectures;

        Cursor cursor = this.mContext.getContentResolver().query(
                CourseTable.CONTENT_URI,
                new String[]{CourseTable.COLUMN_COURSE_ENGAGED_LECTURES},
                CourseTable.COLUMN_COURSE_INST_ID + "=? ",
                new String[]{courseId},
                null
        );

        cursor.moveToFirst();
        if ((cursor != null) && (cursor.getCount() > 0))
            engagedLectures = cursor.getInt(0);
        else
            engagedLectures = 0;
        cursor.close();
        return engagedLectures;
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
                Course course = new Course(id, name, max, engaged, attended);
                allCourses.add(course);
            }

        return allCourses;
    }


    /**
     * ******************************************
     * LECTURE TABLE API
     * *******************************************
     */
    public boolean isLecturePresent(String start_time, String end_time, String day) {

        boolean result = false;

        Cursor cursor = this.mContext.getContentResolver().query(
                LectureTable.CONTENT_URI,
                null,
                LectureTable.COLUMN_LECTURE_START_TIME + "=? and " + LectureTable.COLUMN_LECTURE_DAY + "=?",
                new String[]{start_time, day},
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

    public boolean addLecture(String courseName, String courseId, String startTime,
                              String endTime, String day, String location) {

        boolean result = false;

        if (isLecturePresent(startTime, endTime, day)) {
            Log.d("LECTURE ALREADY EXISTS", "Lecture: " + courseName + " already exists.");
            return result;
        }

        ContentValues cv = createLectureValues(courseName, courseId, startTime, endTime, day, location);

        try {
            Uri uri = this.mContext.getContentResolver().insert(CourseTable.CONTENT_URI, cv);
            if (uri != null) {
                result = true;
            }
        } catch (SQLiteException e) {
            Log.d("INSERTION FAILED", "Unable to add lecture: " + courseId);
            e.printStackTrace();
        }

        return result;
    }

    public ContentValues createLectureValues(String courseName, String courseId, String startTime,
                                             String endTime, String day, String location) {

        ContentValues lectureValues = new ContentValues();

        lectureValues.put(LectureTable.COLUMN_LECTURE_COURSE_ID, courseId);
        lectureValues.put(LectureTable.COLUMN_LECTURE_COURSE_NAME, courseName);
        lectureValues.put(LectureTable.COLUMN_LECTURE_START_TIME, startTime);
        lectureValues.put(LectureTable.COLUMN_LECTURE_END_TIME, endTime);
        lectureValues.put(LectureTable.COLUMN_LECTURE_DAY, day);
        lectureValues.put(LectureTable.COLUMN_LECTURE_LOCATION, location);

        return lectureValues;
    }

    public boolean updateLecture(String oldStartTime, String oldDay, String newStartTime, String newEndTime, String newDay, String newLocation) {

        boolean result = false;

        ContentValues cv = new ContentValues();
        cv.put(LectureTable.COLUMN_LECTURE_START_TIME, newStartTime);
        cv.put(LectureTable.COLUMN_LECTURE_END_TIME, newEndTime);
        cv.put(LectureTable.COLUMN_LECTURE_DAY, newDay);
        cv.put(LectureTable.COLUMN_LECTURE_LOCATION, newLocation);

        long numRowsAffected = this.mContext.getContentResolver().update(
                LectureTable.CONTENT_URI,
                cv,
                LectureTable.COLUMN_LECTURE_START_TIME + "=? and " + LectureTable.COLUMN_LECTURE_DAY + "=?",
                new String[]{oldStartTime, oldDay});

        if (numRowsAffected > 0) {
            result = true;
        }

        return result;
    }

}
