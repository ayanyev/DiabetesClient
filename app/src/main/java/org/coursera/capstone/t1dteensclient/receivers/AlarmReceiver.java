package org.coursera.capstone.t1dteensclient.receivers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.activities.CheckinActivity;

import java.util.Calendar;

import static org.coursera.capstone.t1dteensclient.Constants.*;

public class AlarmReceiver extends BroadcastReceiver {

    AlarmManager alarmMgr;
    PendingIntent[] pIntents;

    @Override
    public void onReceive(Context context, Intent intent) {

        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        pIntents = new PendingIntent[3];

        String action = intent.getAction();

        if (action.equals("android.intent.action.BOOT_COMPLETED")
                || intent.getAction().equals(STARTALARMS)) {

            String[] values = intent.getStringArrayExtra("reminders values");

            for (int i = 0; i < values.length; i++)
                setAlarm(context, i, values[i]);

        // cancelling all alarms
        } else if (action.equals(STOPALARMS)) {

            if (alarmMgr != null) {
                for (PendingIntent pIntent : pIntents)
                    alarmMgr.cancel(pIntent);
            }

        // re-setting single alarm by it's intent
        } else if (action.equals(SETALARM)){

            int index = intent.getIntExtra("reminder key", 0);
            String time = intent.getStringExtra("reminder value");

            setAlarm(context, index, time);

        // doing smthng when alarm has been fired
        } else if (action.equals(STARTNOTIFICATION) || action.equals(STARTNOTIFICATIONFORDELAYED)){

            // re-setting alarm if not for delayed chechin
            if (!action.equals(STARTNOTIFICATIONFORDELAYED))
                setAlarm(context, intent.getIntExtra("index", 0), intent.getStringExtra("time"));

            NotificationCompat.Builder mBuilder =
                    (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.notification_small)
                            .setContentTitle("Hey, " + Utils.getCurrentUserFirstName(context))
                            .setContentText("it's time for a new checkin")
                            .setTicker(Utils.getCurrentUserFirstName(context) + ", it's time for a new checkin")
                            .addAction(R.drawable.action_take, "Take",
                                    getPendingIntent(context, NEWCHECKIN))
                            .addAction(R.drawable.action_skip, "Skip",
                                    getPendingIntent(context, SKIPCHECKIN))
                            .addAction(R.drawable.action_delay, "Delay",
                                    getPendingIntent(context, DELAYCHECKIN))
                            .setPriority(NotificationCompat.PRIORITY_MAX);

            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(NEWCHECKIN_NOTIFICATION, mBuilder.build());

        }
    }

    public void setAlarm(Context context, int alarmIndex, String alarmTime) {

        Intent receiverIntent = new Intent(context, AlarmReceiver.class);
        receiverIntent.setAction(STARTNOTIFICATION)
                .putExtra("index", alarmIndex)
                .putExtra("time", alarmTime);

        pIntents[alarmIndex] = PendingIntent.getBroadcast(context, alarmIndex, receiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        long t = getAlarmTime(alarmTime);

        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, getAlarmTime(alarmTime), pIntents[alarmIndex]);
    }

    private long getAlarmTime(String alarmTime) {

        String[] d = alarmTime.substring(1, 6).split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(d[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(d[1]));
        calendar.set(Calendar.SECOND, 0);

        long time = calendar.getTimeInMillis();

        if ( time < System.currentTimeMillis()) time = (1000 * 60 * 60 * 24) + time;

        return time;
    }

    private PendingIntent getPendingIntent(Context context, String action) {

        Intent intent = new Intent(context, CheckinActivity.class);
        intent.setAction(action);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return PendingIntent.getActivity(context, NEWCHECKIN_NOTIFICATION,
                intent, PendingIntent.FLAG_ONE_SHOT);
    }
}