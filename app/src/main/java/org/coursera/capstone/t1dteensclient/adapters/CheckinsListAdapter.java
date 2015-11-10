package org.coursera.capstone.t1dteensclient.adapters;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.entities.CheckIn;
import org.coursera.capstone.t1dteensclient.entities.enums.CheckInStatus;
import org.joda.time.DateTime;

public class CheckinsListAdapter extends CursorAdapter {

    Context mContext;

    public CheckinsListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_checkin, parent, false);
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
        status.setText("STATUS: " + String.valueOf(ci.getStatus()).toLowerCase());

        TextView id = (TextView) view.findViewById(R.id.checkinListItem_id);
        id.setText(String.valueOf("ID: " + String.valueOf(ci.get_id())));
        TextView timestamp = (TextView) view.findViewById(R.id.checkinListItem_timestamp);
        String timepattern = "MMM dd, YYYY @ " + (DateFormat.is24HourFormat(mContext) ? "H:mm" : "h:mm a");
        timestamp.setText(new DateTime(ci.getTimestamp()).toString(timepattern));

    }
}
