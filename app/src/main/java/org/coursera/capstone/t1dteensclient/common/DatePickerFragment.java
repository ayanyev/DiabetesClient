package org.coursera.capstone.t1dteensclient.common;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import org.coursera.capstone.t1dteensclient.Utils;
import org.joda.time.DateTime;

import static android.app.DatePickerDialog.OnDateSetListener;

public class DatePickerFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener{

    public DatePickerFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int year = 2000;
        int month = 1;
        int day = 1;

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(),
                (OnDateSetListener)getActivity(), year, month, day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        int viewId = getArguments().getInt("dateView");
        EditText dateView = (EditText) getActivity().findViewById(viewId);

        DateTime time = new DateTime(year, monthOfYear, dayOfMonth, 0, 0);
        dateView.setText(Utils.timestampToDate(getActivity(), time.toDate()));
    }
}