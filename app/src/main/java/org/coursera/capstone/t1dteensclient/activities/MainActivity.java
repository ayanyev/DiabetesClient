package org.coursera.capstone.t1dteensclient.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import org.coursera.capstone.t1dteensclient.Constants;
import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.common.GenericActivity;
import org.coursera.capstone.t1dteensclient.entities.Answer;
import org.coursera.capstone.t1dteensclient.entities.CheckIn;
import org.coursera.capstone.t1dteensclient.entities.enums.CheckInStatus;
import org.coursera.capstone.t1dteensclient.presenter.LoginOps;
import org.coursera.capstone.t1dteensclient.provider.ContentProviderObserver;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;
import org.coursera.capstone.t1dteensclient.sync.SyncAdapter;

public class MainActivity extends GenericActivity<LoginOps> {



    private final String TAG = getClass().getSimpleName();
    private static final int REQUEST_LOG_IN = 0;
    private SharedPreferences prefs;
    private String loggedAs;
    Account mAccount;
    private ContentResolver mResolver;
    ContentProviderObserver observer;
    SyncAdapter mSyncAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check if user is logged in
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // TODO. REMOVE AFTER TESTING DONE
        prefs.edit().putString("username", "teen2")
                    .putString("password", "2222")
                    .commit();

        loggedAs = prefs.getString("username", "guest");

        if (loggedAs.equals("guest")){

            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOG_IN);

        } else {

            mResolver = getContentResolver();
            mAccount = CreateSyncAccount(this);
            observer = new ContentProviderObserver(new Handler(), mAccount);
            mResolver.registerContentObserver(ServiceContract.CHECKINS_DATA_URI, true, observer);

            CheckIn ci1 = new CheckIn();
            Answer a1 = new Answer(1);
            a1.setQuestionId(1);
            Answer a2 = new Answer(2);
            a1.setQuestionId(2);

            ci1.getAnswers().add(a1);
            ci1.getAnswers().add(a2);
            ci1.setStatus(CheckInStatus.PASSED);
            ci1.setUser_id((long) 4);

            ci1.saveIt(getApplicationContext());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
//        mResolver.unregisterContentObserver(observer);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((resultCode == RESULT_OK) && (requestCode == REQUEST_LOG_IN)) {

            Log.d(TAG, "User ID: " + String.valueOf(data.getLongExtra("userId", 0)));
        }
    }

    // TODO create real account not dummy
    public static Account CreateSyncAccount(Context context) {

        Account newAccount = new Account(Constants.ACCOUNT, Constants.ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(ACCOUNT_SERVICE);

        if (accountManager.addAccountExplicitly(newAccount, null, null)) {

            ContentResolver.setSyncAutomatically(newAccount, ServiceContract.AUTHORITY, true);

        } else {

        }

        return newAccount;
    }
}
