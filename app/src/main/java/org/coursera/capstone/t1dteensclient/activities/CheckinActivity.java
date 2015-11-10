package org.coursera.capstone.t1dteensclient.activities;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.TimePicker;

import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.adapters.AnswersListAdapter;
import org.coursera.capstone.t1dteensclient.common.LifecycleLoggingActivity;
import org.coursera.capstone.t1dteensclient.common.TimePickerFragment;
import org.coursera.capstone.t1dteensclient.entities.Answer;
import org.coursera.capstone.t1dteensclient.entities.CheckIn;
import org.coursera.capstone.t1dteensclient.provider.ContentProviderObserver;

import java.util.List;

public class CheckinActivity extends FragmentActivity {

    ContentProviderObserver observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checkin);
    }

    public void showTimePickerDialog(View v) {

        TimePickerFragment dialog = new TimePickerFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("timeView", v.getId());

        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "timePicker");
    }
}
