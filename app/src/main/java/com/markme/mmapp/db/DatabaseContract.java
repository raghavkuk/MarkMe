package com.markme.mmapp.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by raghav on 14/10/15.
 */
public class DatabaseContract {

    public static final String CONTENT_AUTHORITY = "com.markme.mmapp.dataprovider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_COURSES = "courses";
    public static final String PATH_LECTURES = "lectures";

    public static final class CourseTable implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_COURSES).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COURSES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COURSES;

        public static final String TABLE_NAME = "course_table";

        public static final String COLUMN_COURSE_NAME = "course_name";
        public static final String COLUMN_COURSE_INST_ID = "course_id";
        public static final String COLUMN_COURSE_ENGAGED_LECTURES = "lectures_engaged";
        public static final String COLUMN_COURSE_ATTENDED_LECTURES = "lectures_attended";
        public static final String COLUMN_COURSE_ABSENT_LECTURES = "lectures_absent";
        public static final String COLUMN_COURSE_MAX_LECTURES = "max_lectures";

    }

    public static final class LectureTable implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LECTURES).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LECTURES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LECTURES;

        public static final String TABLE_NAME = "lecture_table";

        public static final String COLUMN_LECTURE_COURSE_ID = "lecture_course_id";
        public static final String COLUMN_LECTURE_COURSE_NAME = "lecture_course_name";
        public static final String COLUMN_LECTURE_START_TIME = "lecture_start_time";
        public static final String COLUMN_LECTURE_END_TIME= "lecture_end_time";
        public static final String COLUMN_LECTURE_DAY = "lecture_day";
        public static final String COLUMN_LECTURE_LOCATION = "lecture_location";

    }

}
