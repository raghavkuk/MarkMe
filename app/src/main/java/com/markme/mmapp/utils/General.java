package com.markme.mmapp.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import com.markme.mmapp.data.Lecture;
import com.markme.mmapp.db.DatabaseAPI;

import java.util.ArrayList;
import java.util.Calendar;

public class General {

    public static void initializeAlarms(Context context) {
        DatabaseAPI dbApi = new DatabaseAPI(context);
        Calendar c = Calendar.getInstance();
        ArrayList<Lecture> todaysLectures = dbApi.getAllLectures(c.get(Calendar.DAY_OF_WEEK));
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        for (int i = 0; i < todaysLectures.size(); i++) {

            Calendar calendarReminder = Calendar.getInstance();
            calendarReminder.setTimeInMillis(System.currentTimeMillis());

            String startTime = todaysLectures.get(i).getStartTime();

            String[] splitArray = startTime.split(":");

            int hourReminder = Integer.valueOf(splitArray[0]);

            int minuteReminder = Integer.valueOf(splitArray[1]);

            if (minuteReminder < 5) {
                minuteReminder = 55 + minuteReminder;
                if (hourReminder != 0)
                    hourReminder = hourReminder - 1;
            } else {
                minuteReminder = minuteReminder - 5;
            }


            calendarReminder.set(Calendar.HOUR_OF_DAY, hourReminder);
            calendarReminder.set(Calendar.MINUTE, minuteReminder);

            if (calendarReminder.getTimeInMillis() >= System.currentTimeMillis()) {

                Intent intent = new Intent(context, BootReceiver.class);
                intent.setAction("CLASS_REMINDER");
                intent.putExtra("course_name", todaysLectures.get(i).getCourseName());
                intent.putExtra("start_time", todaysLectures.get(i).getStartTime());
                intent.putExtra("location", todaysLectures.get(i).getLocation());

                PendingIntent reminderIntent = PendingIntent.getBroadcast(context, i, intent, 0);

                if (Build.VERSION.SDK_INT < 19)
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendarReminder.getTimeInMillis(), reminderIntent);
                else
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarReminder.getTimeInMillis(), reminderIntent);

            }

            int minuteAttendance, hourAttendance;

            minuteAttendance = minuteReminder + 10;
            hourAttendance = hourReminder;

            if (minuteAttendance > 60) {
                minuteAttendance = minuteAttendance % 60;
                hourAttendance += 1;
            }

            Calendar calendarAttendance = Calendar.getInstance();
            calendarAttendance.setTimeInMillis(System.currentTimeMillis());
            calendarAttendance.set(Calendar.HOUR_OF_DAY, hourAttendance);
            calendarAttendance.set(Calendar.MINUTE, minuteAttendance);

            if (calendarAttendance.getTimeInMillis() >= System.currentTimeMillis()) {
                Intent intent = new Intent(context, BootReceiver.class);
                intent.setAction("MARK_ATTENDANCE");
                intent.putExtra("course_id", todaysLectures.get(i).getCourseId());

                PendingIntent attendanceIntent = PendingIntent.getBroadcast(context, i+100, intent, 0); // 100 is arbitrary

                if (Build.VERSION.SDK_INT < 19)
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendarAttendance.getTimeInMillis(), attendanceIntent);
                else
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarAttendance.getTimeInMillis(), attendanceIntent);
            }

        }
    }
}
