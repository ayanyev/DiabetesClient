package org.coursera.capstone.t1dteensclient;

import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.view.View;

import org.coursera.capstone.t1dteensclient.common.TimePickerFragment;
import org.coursera.capstone.t1dteensclient.entities.User;
import org.coursera.capstone.t1dteensclient.entities.enums.UserGender;
import org.coursera.capstone.t1dteensclient.entities.enums.UserType;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.Objects;

import static org.coursera.capstone.t1dteensclient.Constants.*;

public class Utils {

    private static SharedPreferences prefs;

    public static Boolean isUserRemembered(Context context){

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return !Objects.equals(prefs.getString(KEY_CURRENT_USERNAME, KEY_GUEST_USERNAME), KEY_GUEST_USERNAME);
    }

    public static String getCurrentUserName(Context context){

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(KEY_CURRENT_USERNAME, KEY_GUEST_USERNAME);
    }

    public static String getCurrentPassword(Context context){

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(KEY_CURRENT_PASSWORD, KEY_GUEST_PASSWORD);
    }


    public static long getCurrentUserId(Context context) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getLong(KEY_CURRENT_ID, GUEST_ID);
    }

    public static UserType getCurrentUserType(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return UserType.valueOf(prefs.getString(KEY_CURRENT_TYPE, String.valueOf(UserType.TEEN)));
    }

    public static UserGender getCurrentUserGender(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return UserGender.valueOf(prefs.getString(KEY_CURRENT_GENDER, String.valueOf(UserGender.MALE)));
    }

    public static int getCurrentUserMedrec(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(prefs.getString(KEY_CURRENT_MEDREC, null));
    }

    public static Date getCurrentUserDOB(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        DateTime dt = new DateTime(prefs.getString(KEY_CURRENT_DOB, null));
        return dt.toDate();
    }

    public static String getCurrentUserLastName(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(KEY_CURRENT_LASTNAME, "");
    }

    public static String getCurrentUserFirstName(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(KEY_CURRENT_FIRSTNAME, "");
    }

    public static String getCurrentUserSharePolicy(Context context) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return new StringBuilder()
                .append((prefs.getBoolean(KEY_FIRSTNAME_POLICY, true)) ? 1 : 0)
                .append((prefs.getBoolean(KEY_LASTNAME_POLICY, true)) ? 1 : 0)
                .append((prefs.getBoolean(KEY_DOB_POLICY, true)) ? 1 : 0)
                .append((prefs.getBoolean(KEY_MEDREC_POLICY, true)) ? 1 : 0)
                .append((prefs.getBoolean(KEY_GENDER_POLICY, true)) ? 1 : 0)
                .append((prefs.getBoolean(KEY_CHECKINS_POLICY, true)) ? 1 : 0)
                .toString();
    }

    public static void rememberCurrentUserCredentials(Context context, User user){

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putLong(KEY_CURRENT_ID, user.getId())
                    .putString(KEY_CURRENT_USERNAME, user.getUsername())
                    .putString(KEY_CURRENT_PASSWORD, user.getPassword())
                    .putString(KEY_CURRENT_FIRSTNAME, user.getFirstName())
                    .putString(KEY_CURRENT_LASTNAME, user.getLastName())
                    .putInt(KEY_CURRENT_MEDREC, user.getMedicalRecord())
                .putString(KEY_CURRENT_GENDER, String.valueOf(user.getGender()))
                .putString(KEY_CURRENT_DOB, user.getDateOfBirth().toString())
                .apply();
    }

    public static void setDummyUserCredetials(Context context){
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(KEY_CURRENT_USERNAME, "teen2")
                .putString(KEY_CURRENT_PASSWORD, "2222")
                .putLong(KEY_CURRENT_ID, 13)
                .apply();
    }

    public static void setGuestUserCredentials(Context context) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(KEY_CURRENT_USERNAME, KEY_GUEST_USERNAME)
                .putString(KEY_CURRENT_PASSWORD, KEY_GUEST_PASSWORD)
                .putLong(KEY_CURRENT_ID, GUEST_ID)
                .apply();
    }

    public static User createCurrentUser(Context context) {

        User currentUser = new User();
        currentUser.setId(getCurrentUserId(context));
        currentUser.setUsername(getCurrentUserName(context));
        currentUser.setPassword(getCurrentPassword(context));
        currentUser.setFirstName(getCurrentUserFirstName(context));
        currentUser.setLastName(getCurrentUserLastName(context));
        currentUser.setDateOfBirth(getCurrentUserDOB(context));
        currentUser.setMedicalRecord(getCurrentUserMedrec(context));
        currentUser.setEnabled(true);
        currentUser.setGender(getCurrentUserGender(context));
        currentUser.setUserType(getCurrentUserType(context));
        currentUser.setTimestamp(null);
        currentUser.setSharePolicy(getCurrentUserSharePolicy(context));

        return currentUser;
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

    public static void showTimePickerDialog(FragmentManager fm, View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(fm, "timePicker");
    }

    public static String timestampToDateTime(Context context, Date timestamp) {

        DateTime dt = new DateTime(timestamp);
        String pattern = DateFormat.is24HourFormat(context) ? "dd MMM YYYY @ H:mm"
                                                            : "MMM dd, YYYY @ h:mm a";
        return dt.toString(pattern);
    }

    public static String timestampToDate(Context context, Date timestamp) {

        DateTime dt = new DateTime(timestamp);
        String pattern = DateFormat.is24HourFormat(context) ? "dd MMM YYYY"
                                                            : "MMM dd, YYYY";
        return dt.toString(pattern);
    }

    public static String timestampToTime(Context context, Date timestamp) {

        DateTime dt = new DateTime(timestamp);
        String pattern = DateFormat.is24HourFormat(context) ? "H:mm"
                                                            : "h:mm a";
        return dt.toString(pattern);
    }

    public static String getTimePattern(Context context) {

        return DateFormat.is24HourFormat(context) ? "H:mm" : "h:mm a";
    }

    public static String getDatePattern(Context context) {

        return DateFormat.is24HourFormat(context) ? "dd MMM YYYY" : "MMM dd, YYYY";
    }

    public static String getDateTimePattern(Context context) {

        return DateFormat.is24HourFormat(context) ? "dd MMM, H:mm" : "MMM dd, h:mm a";
    }


    public static String longToDate(Context context, long timestamp) {

        DateTime dt = new DateTime(timestamp);
        String pattern = DateFormat.is24HourFormat(context) ? "dd MMM YYYY"
                : "MMM dd, YYYY";
        return dt.toString(pattern);
    }


}
