package org.coursera.capstone.t1dteensclient.sync;

import android.accounts.Account;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import org.coursera.capstone.t1dteensclient.controllers.SvcController;
import org.coursera.capstone.t1dteensclient.entities.CheckIn;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;

import java.util.ArrayList;
import java.util.List;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    ContentResolver mContentResolver;
    SvcController mController;
    SharedPreferences prefs;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        mContentResolver = context.getContentResolver();
        mController = new SvcController(context.getApplicationContext());
        prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              SyncResult syncResult) {

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        ContentValues cv;
        String[] projection;
        String selection;
        String[] selectionArgs;
        Uri uri;
        Long lastCheckinSync;

        uri = ServiceContract.CHECKINS_DATA_URI;
        projection = null;
        selection = "timestamp > ?";
        selectionArgs = new String[]{String.valueOf(prefs.getLong("lastCheckinSync", 0))};

        // list of checkins to be sent to server
        List<CheckIn> checkins = new ArrayList<>();

        try {
            Cursor cursor =
                    provider.query(uri,
                            projection,
                            selection,
                            selectionArgs,
                            null);

            if (cursor.moveToFirst()) {

                while (!cursor.isAfterLast()) {

                    // get ID of item in local db
                    int checkin_id = cursor.getInt(cursor
                            .getColumnIndex(ServiceContract.CHECKINS_COLUMN_ID));

                    // query for answers which belong to checkin
                    uri = ServiceContract.ANSWERS_DATA_URI;
                    projection = null;
                    selection = "checkin_id = ?";
                    selectionArgs = new String[]{String.valueOf(checkin_id)};

                    Cursor cursor2 =
                            provider.query(uri,
                                    projection,
                                    selection,
                                    selectionArgs,
                                    null);

                    checkins.add((new CheckIn()).fromCursorToPOJO(cursor, cursor.getPosition(), cursor2));

                    cursor2.close();
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // result of remote request with checkin_ids assigned
        checkins = mController.bulkAddCheckins(checkins);

        // updates checkin_id in local db
        uri = ServiceContract.CHECKINS_DATA_URI;
        selection = "timestamp = ?";

        for (CheckIn ci : checkins){

            cv = ci.toContentValues();
            selectionArgs = new String[]{String.valueOf(ci.getTimestamp().getTime())};

            try {
                provider.update(uri, cv, selection, selectionArgs);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        prefs.edit().putLong("lastCheckinSync", System.currentTimeMillis()).commit();

        Log.d("Sync Adapter", "onPerformSync reached");

    }
}
