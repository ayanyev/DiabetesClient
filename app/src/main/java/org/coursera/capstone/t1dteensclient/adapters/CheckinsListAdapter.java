package org.coursera.capstone.t1dteensclient.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.coursera.capstone.t1dteensclient.Constants;
import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.activities.CheckinActivity;
import org.coursera.capstone.t1dteensclient.entities.CheckIn;
import org.coursera.capstone.t1dteensclient.entities.enums.CheckInStatus;
import org.joda.time.DateTime;

public class CheckinsListAdapter extends CursorAdapter {

    Context mContext;
    CheckIn mCheckin = null;

    public CheckinsListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mContext = context;
    }

    @Override
    public CheckIn getItem(int position) {
        return mCheckin;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_checkin, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        mCheckin = (new CheckIn()).fromCursorToPOJO(cursor, -1, null);

        ImageView image = (ImageView) view.findViewById(R.id.checkinListItem_image);
        TextView status = (TextView) view.findViewById(R.id.checkinListItem_status);
        if (mCheckin.getStatus() == CheckInStatus.PASSED) {
            image.setImageResource(R.drawable.passed);
        } else if (mCheckin.getStatus() == CheckInStatus.SKIPPED){
            image.setImageResource(R.drawable.skipped);
        }
        status.setText("STATUS: " + String.valueOf(mCheckin.getStatus()).toLowerCase());

        TextView id = (TextView) view.findViewById(R.id.checkinListItem_id);
        id.setText(String.valueOf("ID: " + String.valueOf(mCheckin.get_id())));
        TextView timestamp = (TextView) view.findViewById(R.id.checkinListItem_timestamp);
        timestamp.setText(Utils.timestampToDateTime(mContext, mCheckin.getTimestamp()));

//        ImageView menu = (ImageView) view.findViewById(R.id.menu);
//        menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                final String[] items = {"Edit", "Delete"};
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                builder.setItems(items, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int item) {
//
//                        switch (item){
//
//                            case 0:
//
//                                if (mCheckin.getStatus() == CheckInStatus.PASSED) {
//
//                                    Intent intent = new Intent(mContext, CheckinActivity.class);
//                                    intent.putExtra("checkin", mCheckin.loadAnswers(mContext));
//                                    intent.setAction(Constants.EDITCHECKIN);
//                                    mContext.startActivity(intent);
//
//                                } else if (mCheckin.getStatus() == CheckInStatus.SKIPPED){
//
//                                    Toast.makeText(mContext,
//                                            R.string.skipped_checkin_click, Toast.LENGTH_LONG)
//                                            .show();
//                                }
//
//                            case 1:
//
//                                mCheckin.deleteIt(mContext);
//                        }
//
//                    }
//                });
//                AlertDialog alert = builder.create();
//                alert.show();
//            }
//        });
    }
}
