package org.coursera.capstone.t1dteensclient;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import org.coursera.capstone.t1dteensclient.common.TimePickerFragment;
import org.coursera.capstone.t1dteensclient.entities.User;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;

import java.util.Objects;

import static org.coursera.capstone.t1dteensclient.Constants.*;

public class Utils {

    private static SharedPreferences prefs;

    public static Boolean isUserRemembered(Context context){

        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        return !Objects.equals(prefs.getString(CURRENT_USERNAME, GUEST_USERNAME), GUEST_USERNAME);
    }

    public static String getCurrentUserName(Context context){

        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        return prefs.getString(CURRENT_USERNAME, GUEST_USERNAME);
    }

    public static String getCurrentPassword(Context context){

        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        return prefs.getString(CURRENT_PASSWORD, GUEST_PASSWORD );
    }


    public static long getCurrentUserId(Context context) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        return prefs.getLong(CURRENT_ID, GUEST_ID);
    }

    public static void rememberCurrentUserCredentials(Context context, User user){

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(CURRENT_USERNAME, user.getUsername())
                    .putString(CURRENT_PASSWORD, user.getPassword())
                    .putLong(CURRENT_ID, user.getId())
                    .apply();
    }

    public static void setDummyUserCredetials(Context context){
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(CURRENT_USERNAME, "teen2")
                .putString(CURRENT_PASSWORD, "2222")
                .putLong(CURRENT_ID, 13)
                .apply();
    }

    public static void setGuestUserCredentials(Context context) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(CURRENT_USERNAME, GUEST_USERNAME)
                .putString(CURRENT_PASSWORD, GUEST_PASSWORD)
                .putLong(CURRENT_ID, GUEST_ID)
                .apply();
    }

    public static void requestSyncOnDemand(Context context, Uri uri) {

        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        // flag to force run sync immediately
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        // puts an uri to sync
        settingsBundle.putString("uri", uri.toString());

        ContentResolver.requestSync(accountManager.getAccountsByType(Constants.ACCOUNT_TYPE)[0],
                ServiceContract.AUTHORITY, settingsBundle);
    }

    public void showTimePickerDialog(FragmentManager fm, View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(fm, "timePicker");
    }

}
