package com.markme.mmapp.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.markme.mmapp.R;
import com.markme.mmapp.ui.HomeActivity;

/**
 * Created by raghav on 23/10/15.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") ||
                intent.getAction().equals("ANDROID.INTENT.ACTION.DATE_CHANGED")) {
            General.initializeAlarms(context);
        }

        if (intent.getAction().equals("CLASS_REMINDER")) {

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.app_icon);

            String className = intent.getStringExtra("course_name");
            String startTime = intent.getStringExtra("start_time");
            String location = intent.getStringExtra("location");

            mBuilder.setContentTitle("Class at " + startTime + " in " + location);
            mBuilder.setContentText(className);
            mBuilder.setAutoCancel(false);

            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.setBigContentTitle("MarkMe");
            bigTextStyle.bigText(className + " class at " + startTime + " in " + location);
            mBuilder.setStyle(bigTextStyle);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.setColor(context.getResources().getColor(R.color.app_primary));
                mBuilder.setSmallIcon(R.drawable.app_icon);
            }

            Intent resultIntent = new Intent(context, HomeActivity.class);
            mBuilder.setContentIntent(PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT));

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());
        }

        if (intent.getAction().equals("MARK_ATTENDANCE")) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

            String classId = intent.getStringExtra("course_id");

            Intent yesButtonIntent = new Intent(context, YesButtonReceiver.class);
            yesButtonIntent.setAction("ATTENDING");
            yesButtonIntent.putExtra("course_id", classId);
            yesButtonIntent.putExtra("notification_id", 1);
            PendingIntent yesPendingButtonIntent = PendingIntent.getBroadcast(context, 500, yesButtonIntent, 0); //500 is arbitrary

            Intent noButtonIntent = new Intent(context, YesButtonReceiver.class);
            noButtonIntent.setAction("NOT_ATTENDING");
            noButtonIntent.putExtra("course_id", classId);
            noButtonIntent.putExtra("notification_id", 1);
            PendingIntent noPendingButtonIntent = PendingIntent.getBroadcast(context, 501, yesButtonIntent, 0);

            RemoteViews customView = new RemoteViews(context.getPackageName(), R.layout.custom_notification);

            customView.setOnClickPendingIntent(R.id.yes_button_custom_notification, yesPendingButtonIntent);
            customView.setOnClickPendingIntent(R.id.no_button_custom_notification, noPendingButtonIntent);

            mBuilder.setSmallIcon(R.drawable.app_icon)
                    .setContent(customView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.setColor(context.getResources().getColor(R.color.app_primary));
                mBuilder.setSmallIcon(R.drawable.app_icon);
            }
            mNotificationManager.notify(1, mBuilder.build());

        }
    }
}
