package org.coursera.capstone.t1dteensclient.activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.common.LifecycleLoggingActivity;
import org.coursera.capstone.t1dteensclient.entities.CheckIn;
import org.coursera.capstone.t1dteensclient.entities.enums.CheckInStatus;
import org.coursera.capstone.t1dteensclient.fragments.CheckinFragment;
import org.coursera.capstone.t1dteensclient.receivers.AlarmReceiver;

import static org.coursera.capstone.t1dteensclient.Constants.*;

public class CheckinActivity extends LifecycleLoggingActivity {

    private static final String CHECKIN_FRAGMENT = "checkin fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String action = getIntent().getAction();
        CheckIn checkin = null;
        boolean editable = false;

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (action.equals(NEWCHECKIN) || action.equals(SHOWCHECKIN) || action.equals(EDITCHECKIN)) {

            mNotificationManager.cancel(NEWCHECKIN_NOTIFICATION);

            setContentView(R.layout.activity_checkin);
            if (action.equals(NEWCHECKIN)){
                checkin = new CheckIn();
                checkin.setUser_id(Utils.getCurrentUserId(this));
                checkin.setNewAnswers(this);
                editable = true;
            } else if (action.equals(SHOWCHECKIN)){
                checkin = getIntent().getParcelableExtra("checkin");
                editable = false;
            } else if (action.equals(EDITCHECKIN)){
                checkin = getIntent().getParcelableExtra("checkin");
                editable = true;
            }

            Bundle bundle = new Bundle();
            bundle.putParcelable("checkin", checkin);
            bundle.putBoolean("ifAnswersAreEditable", editable);

            Fragment fragment = new CheckinFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.checkin_activity_content_frame, fragment, CHECKIN_FRAGMENT)
                    .commit();
        }

        if (action.equals(SKIPCHECKIN)){

            mNotificationManager.cancel(NEWCHECKIN_NOTIFICATION);

            // creates empty checkin
            checkin = new CheckIn();
            checkin.setUser_id(Utils.getCurrentUserId(this));
            checkin.setStatus(CheckInStatus.SKIPPED);
            Uri uri = checkin.saveIt(this);
            finish();
            return;
        }

        if (action.equals(DELAYCHECKIN)){

            mNotificationManager.cancel(NEWCHECKIN_NOTIFICATION);

            // starts alarm to notify about delayed checkin
            Intent receiverIntent = new Intent(this, AlarmReceiver.class);
            receiverIntent.setAction(STARTNOTIFICATIONFORDELAYED);

            PendingIntent alarmIntent =
                    PendingIntent.getBroadcast(this, DELAYED_ALARM, receiverIntent, 0);

            AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmMgr.set(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + 10 * 1000, //DELAY_INTERVAL,
                    alarmIntent);
            finish();
            return;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
