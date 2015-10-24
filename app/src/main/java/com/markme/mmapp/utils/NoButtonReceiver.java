package com.markme.mmapp.utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.markme.mmapp.data.Course;
import com.markme.mmapp.db.DatabaseAPI;

/**
 * Created by raghav on 24/10/15.
 */
public class NoButtonReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("ATTENDING")){

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(intent.getIntExtra("notificaiton_id", -1));
            DatabaseAPI databaseAPI = new DatabaseAPI(context);
            String courseId = intent.getStringExtra("course_id");
            Course course = databaseAPI.getCourse(courseId);
            databaseAPI.updateEngagedLectures(courseId, course.getLecturesEngaged()+1);

        }
    }
}