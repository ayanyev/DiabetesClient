package org.coursera.capstone.t1dteensclient.common;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.coursera.capstone.t1dteensclient.Constants;
import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.provider.ContentProviderObserver;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;

public abstract class LifecycleLoggingActivity extends AppCompatActivity {

	public Account mAccount;
	public ContentProviderObserver observer;
	public ContentResolver mResolver;

	private final String TAG = getClass().getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mResolver = getContentResolver();
		mAccount = CreateSyncAccount(this);
		observer = new ContentProviderObserver(new Handler(), mAccount);
		mResolver.registerContentObserver(ServiceContract.DATABASE_URI, true, observer);

		if (savedInstanceState != null) {
			Log.d(TAG, "onCreate(): activity re-created");
		} else {
			Log.d(TAG, "onCreate(): activity created anew");
		}
	}

	// TODO create real account not dummy
	// TODO make newAccount singleton
	public static Account CreateSyncAccount(Context context) {

		Account newAccount = new Account(Constants.ACCOUNT, Constants.ACCOUNT_TYPE);
		// Get an instance of the Android account manager
		AccountManager accountManager =
				(AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

		if (accountManager.addAccountExplicitly(newAccount, null, null)) {

			ContentResolver.setSyncAutomatically(newAccount, ServiceContract.AUTHORITY, true);

/*            ContentResolver.addPeriodicSync(
                    newAccount,
                    ServiceContract.AUTHORITY,
                    Bundle.EMPTY,
                    SYNC_INTERVAL);*/

		} else {

		}
		return newAccount;
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
		mResolver.unregisterContentObserver(observer);
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

		DatePickerFragment dialog = new DatePickerFragment();

		Bundle bundle = new Bundle();
		bundle.putInt("dateView", v.getId());

		dialog.setArguments(bundle);
		dialog.show(getFragmentManager(), "datePicker");
	}

	public void showTimePickerDialog(View v) {

		TimePickerFragment dialog = new TimePickerFragment();

		Bundle bundle = new Bundle();
		bundle.putInt("timeView", v.getId());

		dialog.setArguments(bundle);
		dialog.show(getSupportFragmentManager(), "timePicker");
	}

}
