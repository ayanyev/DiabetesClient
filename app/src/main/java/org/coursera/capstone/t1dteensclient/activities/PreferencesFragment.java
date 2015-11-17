package org.coursera.capstone.t1dteensclient.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;

import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.SwitchPreference;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.coursera.capstone.t1dteensclient.Constants;
import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;
import org.coursera.capstone.t1dteensclient.receivers.AlarmReceiver;
import org.joda.time.DateTime;

import java.util.Objects;

public class PreferencesFragment extends PreferenceFragment
                                    implements DatePickerDialog.OnDateSetListener,
                                                TimePickerDialog.OnTimeSetListener,
                                                SharedPreferences.OnSharedPreferenceChangeListener,
                                                Preference.OnPreferenceChangeListener,
                                                Preference.OnPreferenceClickListener{

    Preference dob;
    String prefKeyToSetTime = null;
    boolean userDetailsChanged = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Preference rem1, rem2, rem3;
        SwitchPreference remEnabled;

        setRetainInstance(true);

        View view = super.onCreateView(inflater, container, savedInstanceState);

        dob = findPreference(Constants.KEY_CURRENT_DOB);
        rem1 = findPreference(Constants.KEY_REMINDER_TIME_1);
        rem2 = findPreference(Constants.KEY_REMINDER_TIME_2);
        rem3 = findPreference(Constants.KEY_REMINDER_TIME_3);
        remEnabled = (SwitchPreference) findPreference(Constants.KEY_REMINDERS_ENABLED);

        dob.setOnPreferenceClickListener(this);
        rem1.setOnPreferenceClickListener(this);
        rem2.setOnPreferenceClickListener(this);
        rem3.setOnPreferenceClickListener(this);
        remEnabled.setOnPreferenceChangeListener(this);

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        initSummary(getPreferenceScreen());

        return view;
    }

    private void manageAlarmReceiver(Boolean isEnabled) {

        SharedPreferences preferences = getPreferenceManager().getSharedPreferences();

        ComponentName receiver = new ComponentName(getActivity(), AlarmReceiver.class);
        PackageManager pm = getActivity().getPackageManager();
        Intent intent = new Intent();

        // makes receiver enabled when reminders are enabled
        if (isEnabled) {
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

            String[] remindersValues = new String[]{preferences.getString(Constants.KEY_REMINDER_TIME_1, "T09:00"),
                                                    preferences.getString(Constants.KEY_REMINDER_TIME_2, "T14:00"),
                                                    preferences.getString(Constants.KEY_REMINDER_TIME_3, "T20:00")};
            // intent to start alarms
            intent.setAction(Constants.STARTALARMS);
            intent.putExtra("reminders values", remindersValues);
            getActivity().sendBroadcast(intent);

        } else {

            // intent to stop alarms
            intent.setAction(Constants.STOPALARMS);
            getActivity().sendBroadcast(intent);
            // makes receiver enabled when reminders are enabled
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        String key = preference.getKey();

        if (Objects.equals(key, Constants.KEY_CURRENT_DOB))
            showDateDialog();
        if (Objects.equals(key, Constants.KEY_REMINDER_TIME_1))
            showTimeDialog(preference);
        if (Objects.equals(key, Constants.KEY_REMINDER_TIME_2))
            showTimeDialog(preference);
        if (Objects.equals(key, Constants.KEY_REMINDER_TIME_3))
            showTimeDialog(preference);

        return false;
    }

    private void showTimeDialog(Preference preference){

        int hour = 9;
        int minute = 0;

        prefKeyToSetTime = preference.getKey();

        String[] d = preference.getSharedPreferences().getString(prefKeyToSetTime, "T09:00").substring(1,6).split(":");


        if (d.length > 1) {
            hour = Integer.parseInt(d[0]);
            minute = Integer.parseInt(d[1].trim());
        }

        new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity())).show();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        DateTime time = new DateTime(2000, 1, 1, hourOfDay, minute);
        getPreferenceManager().getSharedPreferences().edit()
                .putString(prefKeyToSetTime, "T".concat(time.toString("HH:mm"))).commit();

    }

    private void showDateDialog(){

        int year = 2000;
        int month = 1;
        int day = 1;

        String[] d = dob.getSummary().toString().split("-", 3);

        if (d.length > 0) {
            year = Integer.parseInt(d[0]);
            month = Integer.parseInt(d[1]);
            day = Integer.parseInt(d[2]);
        }

        new DatePickerDialog(this.getActivity(), this, year, month, day).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        DateTime time = new DateTime(year, monthOfYear, dayOfMonth, 0, 0);
        getPreferenceManager().getSharedPreferences().edit()
                .putString(Constants.KEY_CURRENT_DOB, time.toString("YYYY-MM-dd")).commit();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference.getKey().equals(Constants.KEY_REMINDERS_ENABLED))
            manageAlarmReceiver((Boolean) newValue);

        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        // if user data changet sets the flag to update user data on server
        if (key.contains("currentUser")) userDetailsChanged = true;
        // updates preference's summary
        updatePrefSummary(findPreference(key));
        //
        if (key.contains("reminder_")){

            Intent intent = new Intent(Constants.SETALARM);
            intent.putExtra("reminder key", Integer.parseInt(key.substring(key.length() -1 )) - 1);
            intent.putExtra("reminder value", sharedPreferences.getString(key, "T09:00"));
            getActivity().sendBroadcast(intent);
        }
    }

    private void initSummary(Preference p) {
        if (p instanceof PreferenceGroup) {
            PreferenceGroup pGrp = (PreferenceGroup) p;
            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                initSummary(pGrp.getPreference(i));
            }
        } else {
            updatePrefSummary(p);
        }
    }

    private void updatePrefSummary(Preference p) {

        Preference pref = (Preference) p;

        if (p.getKey().equals(Constants.KEY_CURRENT_ID))
            p.setSummary(String.valueOf(pref.getSharedPreferences().getLong(Constants.KEY_CURRENT_ID, 0)));
        if (p.getKey().contains("reminder_")) {
            DateTime dt = null;
            try {
                dt = new DateTime(pref.getSharedPreferences().getString(p.getKey(), "9:00"));
            } catch (Exception e) {
                dt = new DateTime("T9:00");
            }
            p.setSummary(dt.toString(Utils.getTimePattern(getActivity())));
        }
        if (p.getKey().equals(Constants.KEY_CURRENT_DOB))
            p.setSummary(pref.getSharedPreferences().getString(p.getKey(), ""));

        if (p instanceof ListPreference) {
            ListPreference listPref = (ListPreference) p;
            p.setSummary(listPref.getEntry());
        }
        if (p instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
            if (p.getTitle().toString().contains("assword"))
            {
                p.setSummary("******");
            } else {
                p.setSummary(editTextPref.getText());
            }
        }
        if (p instanceof MultiSelectListPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
            p.setSummary(editTextPref.getText());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (userDetailsChanged)
            Utils.requestSyncOnDemand(getActivity(), ServiceContract.USER_DETAILS_URI);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}
