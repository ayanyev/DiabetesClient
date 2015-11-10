package org.coursera.capstone.t1dteensclient.common;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.coursera.capstone.t1dteensclient.R;

public abstract class LifecycleLoggingActivity extends AppCompatActivity {

	private final String TAG = getClass().getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_generic);

		if (savedInstanceState != null) {
			Log.d(TAG, "onCreate(): activity re-created");
		} else {
			Log.d(TAG, "onCreate(): activity created anew");
		}
	}

	@Override
	protected void onStart() {

		super.onStart();

		Log.d(TAG, "onStart() - the activity is about to become visible");
	}

	@Override
	protected void onResume() {

		super.onResume();
		Log.d(TAG, "onResume");
	}

	@Override
	protected void onPause() {

		super.onPause();
		Log.d(TAG,
				"onPause() - another activity is taking focus (this activity is about to be \"paused\")");
	}

	@Override
	protected void onStop() {

		super.onStop();
		Log.d(TAG,
				"onStop() - the activity is no longer visible (it is now \"stopped\")");
	}

	@Override
	protected void onRestart() {

		super.onRestart();
		Log.d(TAG, "onRestart() - the activity is about to be restarted()");
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		Log.d(TAG, "onDestroy() - the activity is about to be destroyed");
	}

	public void showDatePickerDialog(View v) {

//      shows Date Picker dialog
		DatePickerFragment dialog = new DatePickerFragment();
		dialog.show(getFragmentManager(), "datePicker");
	}

	public void showTimePickerDialog(View v) {
		TimePickerFragment dialog = new TimePickerFragment();
		dialog.show(getSupportFragmentManager(), "timePicker");
	}

}
