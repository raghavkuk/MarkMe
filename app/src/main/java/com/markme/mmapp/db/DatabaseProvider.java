package com.markme.mmapp.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.markme.mmapp.db.DatabaseContract.CourseTable;
import com.markme.mmapp.db.DatabaseContract.LectureTable;

/**
 * Created by raghav on 14/10/15.
 */
public class DatabaseProvider extends ContentProvider {

    static final int COURSES = 0;
    static final int COURSE_SINGLE = 1;

    static final int LECTURES = 2;
    static final int LECTURE_SINGLE = 3;

    // The URI Matcher used by this content provider.
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private DatabaseOpenHelper mOpenHelper;

    static UriMatcher buildUriMatcher() {

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, DatabaseContract.PATH_COURSES, COURSES);
        matcher.addURI(authority, DatabaseContract.PATH_COURSES + "/#", COURSE_SINGLE);
        matcher.addURI(authority, DatabaseContract.PATH_LECTURES, LECTURES);
        matcher.addURI(authority, DatabaseContract.PATH_LECTURES + "/#", LECTURE_SINGLE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;
        int uriMatched = uriMatcher.match(uri);

        switch (uriMatched) {

            case COURSES:

            case COURSE_SINGLE:
                cursor = mOpenHelper.getReadableDatabase().query(CourseTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case LECTURES:

            case LECTURE_SINGLE:
                cursor = mOpenHelper.getReadableDatabase().query(LectureTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = uriMatcher.match(uri);

        switch (match) {

            case COURSES:
                return CourseTable.CONTENT_TYPE;
            case COURSE_SINGLE:
                return CourseTable.CONTENT_ITEM_TYPE;
            case LECTURES:
                return LectureTable.CONTENT_TYPE;
            case LECTURE_SINGLE:
                return LectureTable.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long rowID = -1;

        switch (uriMatcher.match(uri)) {

            case COURSES:
                rowID = mOpenHelper.getWritableDatabase().insert(CourseTable.TABLE_NAME, null, values);
                break;

            case LECTURES:
                rowID = mOpenHelper.getWritableDatabase().insert(LectureTable.TABLE_NAME, null, values);
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri + " URI NO.: " + uriMatcher.match(uri));

        }

        if (rowID == -1) {
            return null;
        }

        Uri retURI = ContentUris.withAppendedId(uri, rowID);
        getContext().getContentResolver().notifyChange(uri, null);
        return retURI;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int rowsAffected;

        switch (uriMatcher.match(uri)) {

            case COURSES:
                rowsAffected = mOpenHelper.getWritableDatabase().delete(CourseTable.TABLE_NAME, selection, selectionArgs);
                break;

            case LECTURES:
                rowsAffected = mOpenHelper.getWritableDatabase().delete(LectureTable.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        if (rowsAffected > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsAffected;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int rowsAffected = -1;

        switch (uriMatcher.match(uri)) {

            case COURSES:
                rowsAffected = mOpenHelper.getWritableDatabase().update(CourseTable.TABLE_NAME, values, selection, selectionArgs);
                break;

            case LECTURES:
                rowsAffected = mOpenHelper.getWritableDatabase().update(LectureTable.TABLE_NAME, values, selection, selectionArgs);
                break;

        }

        if (rowsAffected > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsAffected;
    }
}
