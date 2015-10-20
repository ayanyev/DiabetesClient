package org.coursera.capstone.t1dteensclient.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public abstract class LifecycleLoggingActivity extends AppCompatActivity {

	private final String TAG = getClass().getSimpleName();
//	protected VideoSvcApp mMyApp;

	/**
	 * Hook method called when a new instance of Activity is created. One time
	 * initialization code should go here e.g. UI layout, some class scope
	 * variable initialization. if finish() is called from onCreate no other
	 * lifecycle callbacks are called except for onDestroy().
	 *
	 * @param savedInstanceState
	 *            object that contains saved state information.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {

			Log.d(TAG, "onCreate(): activity re-created");

		} else {

			Log.d(TAG, "onCreate(): activity created anew");
		}

//		mMyApp = (VideoSvcApp)this.getApplicationContext();
	}

	/**
	 * Hook method called after onCreate() or after onRestart() (when the
	 * activity is being restarted from stopped state). Should re-acquire
	 * resources relinquished when activity was stopped (onStop()) or acquire
	 * those resources for the first time after onCreate().
	 */
	@Override
	protected void onStart() {

		super.onStart();

		Log.d(TAG, "onStart() - the activity is about to become visible");
	}

	/**
	 * Hook method called after onRestoreStateInstance(Bundle) only if there is
	 * a prior saved instance state in Bundle object. onResume() is called
	 * immediately after onStart(). onResume() is called when user resumes
	 * activity from paused state (onPause()) User can begin interacting with
	 * activity. Place to start animations, acquire exclusive resources, such as
	 * the camera.
	 */
	@Override
	protected void onResume() {

		super.onResume();
//		mMyApp.setCurrentActivity(this);
		Log.d(TAG, "onResume");
	}

	/**
	 * Hook method called when an Activity loses focus but is still visible in
	 * background. May be followed by onStop() or onResume(). Delegate more CPU
	 * intensive operation to onStop for seamless transition to next activity.
	 * Save persistent state (onSaveInstanceState()) in case app is killed.
	 * Often used to release exclusive resources.
	 */
	@Override
	protected void onPause() {

//		clearReferences();
		super.onPause();
		Log.d(TAG,
				"onPause() - another activity is taking focus (this activity is about to be \"paused\")");
	}

	/**
	 * Called when Activity is no longer visible. Release resources that may
	 * cause memory leak. Save instance state (onSaveInstanceState()) in case
	 * activity is killed.
	 */
	@Override
	protected void onStop() {

		super.onStop();
		Log.d(TAG,
				"onStop() - the activity is no longer visible (it is now \"stopped\")");
	}

	/**
	 * Hook method called when user restarts a stopped activity. Is followed by
	 * a call to onStart() and onResume().
	 */
	@Override
	protected void onRestart() {

		super.onRestart();
		Log.d(TAG, "onRestart() - the activity is about to be restarted()");
	}

	/**
	 * Hook method that gives a final chance to release resources and stop
	 * spawned threads. onDestroy() may not always be called-when system kills
	 * hosting process
	 */
	@Override
	protected void onDestroy() {

//		clearReferences();
		super.onDestroy();
		Log.d(TAG, "onDestroy() - the activity is about to be destroyed");
	}

/*	private void clearReferences(){
		Activity currActivity = mMyApp.getCurrentActivity();
		if (currActivity != null && currActivity.equals(this))
			mMyApp.setCurrentActivity(null);
	}*/

}
