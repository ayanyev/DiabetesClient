package org.coursera.capstone.t1dteensclient.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.entities.CheckIn;
import org.coursera.capstone.t1dteensclient.entities.enums.CheckInStatus;

public class CheckinsListAdapter extends CursorAdapter {

    public CheckinsListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_checkin, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        CheckIn ci = (new CheckIn()).fromCursorToPOJO(cursor, -1, null);

        ImageView image = (ImageView) view.findViewById(R.id.checkinListItem_image);
        TextView status = (TextView) view.findViewById(R.id.checkinListItem_status);
        if (ci.getStatus() == CheckInStatus.PASSED) {
            image.setImageResource(R.drawable.passed);
        } else if (ci.getStatus() == CheckInStatus.SKIPPED){
            image.setImageResource(R.drawable.skipped);
        }
        status.setText(String.valueOf(ci.getStatus()).toLowerCase());

        TextView timestamp = (TextView) view.findViewById(R.id.checkinListItem_timestamp);
        timestamp.setText(ci.getTimestamp().toString());
    }
}
