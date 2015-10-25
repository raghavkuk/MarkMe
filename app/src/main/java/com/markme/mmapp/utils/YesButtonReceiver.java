package com.markme.mmapp.utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.markme.mmapp.data.Course;
import com.markme.mmapp.db.DatabaseAPI;
import com.markme.mmapp.ui.SummaryFragment;

public class YesButtonReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("ATTENDING")){

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(intent.getIntExtra("notification_id", -1));
            DatabaseAPI databaseAPI = new DatabaseAPI(context);
            String courseId = intent.getStringExtra("course_id");
            Course course = databaseAPI.getCourse(courseId);
            databaseAPI.updateAttendedLectures(courseId, course.getLecturesAttended() + 1);
            databaseAPI.updateEngagedLectures(courseId, course.getLecturesEngaged() + 1);
            SummaryFragment.newInstance().refresh(context);
        }
    }
}
