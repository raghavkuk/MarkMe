package com.markme.mmapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.markme.mmapp.db.DatabaseContract.CourseTable;
import com.markme.mmapp.db.DatabaseContract.LectureTable;

/**
 * Created by raghav on 14/10/15.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "markme.db";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_COURSE_TABLE = "CREATE TABLE IF NOT EXISTS " + CourseTable.TABLE_NAME + " (" +
                CourseTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                CourseTable.COLUMN_COURSE_NAME + " TEXT NOT NULL, " + //course name
                CourseTable.COLUMN_COURSE_INST_ID + " TEXT, " + //course id in the institute
                CourseTable.COLUMN_COURSE_ENGAGED_LECTURES + " INTEGER DEFAULT(0), " + //engaged lectures
                CourseTable.COLUMN_COURSE_ATTENDED_LECTURES + " INTEGER DEFAULT(0), " + //attended lectures
                CourseTable.COLUMN_COURSE_ABSENT_LECTURES + " INTEGER DEFAULT(0), " + //lectures not attended
                CourseTable.COLUMN_COURSE_MAX_LECTURES + " INTEGER DEFAULT(0)" + //max lectures
                ");";

        final String SQL_CREATE_LECTURE_TABLE = "CREATE TABLE IF NOT EXISTS " + LectureTable.TABLE_NAME + " (" +
                LectureTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                LectureTable.COLUMN_LECTURE_COURSE_ID + " TEXT NOT NULL, " + //course id in course table
                LectureTable.COLUMN_LECTURE_COURSE_NAME + " TEXT NOT NULL, " + //lecture course name
                LectureTable.COLUMN_LECTURE_START_TIME + " LONG DEFAULT(0), " + //lecture start time
                LectureTable.COLUMN_LECTURE_END_TIME + " LONG DEFAULT(0), " + //lecture end time
                LectureTable.COLUMN_LECTURE_DAY + " TEXT NOT NULL, " + //lectures not attended
                LectureTable.COLUMN_LECTURE_LOCATION + " TEXT" + //max lectures
                ");";

        db.execSQL(SQL_CREATE_COURSE_TABLE);
        db.execSQL(SQL_CREATE_LECTURE_TABLE);
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
