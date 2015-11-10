package org.coursera.capstone.t1dteensclient.common;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import org.joda.time.DateTime;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        int viewId = getArguments().getInt("timeView");
        EditText timeView = (EditText) getActivity().findViewById(viewId);

        DateTime time = new DateTime(2015, 1, 1, hourOfDay, minute);

        if (DateFormat.is24HourFormat(getActivity()))
            timeView.setText(time.toString("H:mm"));
        else
            timeView.setText(time.toString("h:mm a"));
    }
}
