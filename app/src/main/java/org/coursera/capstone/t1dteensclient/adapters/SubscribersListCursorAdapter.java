package org.coursera.capstone.t1dteensclient.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.controllers.SvcController;
import org.coursera.capstone.t1dteensclient.entities.Relation;
import org.coursera.capstone.t1dteensclient.entities.User;
import org.coursera.capstone.t1dteensclient.entities.enums.RelationStatus;
import org.joda.time.DateTime;

import java.sql.Timestamp;

public class SubscribersListCursorAdapter extends CursorAdapter {

    final String TAG = getClass().getSimpleName();
    SvcController mController;
    ViewHolder mHolder;

    public SubscribersListCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mController = new SvcController(context);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_subscriber, parent, false);

        Log.d(TAG, "New view created");

        mHolder = new ViewHolder();

        mHolder.fullName = (TextView) view.findViewById(R.id.userListItem_fullName);
        mHolder.type = (TextView) view.findViewById(R.id.userListItem_type);
        mHolder.checkins = (TextView) view.findViewById(R.id.userListItem_checkins);
        mHolder.since = (TextView) view.findViewById(R.id.userListItem_since);
        mHolder.acceptButton = (ImageButton) view.findViewById(R.id.userListItem_acceptButton);
        mHolder.declineButton = (ImageButton) view.findViewById(R.id.userListItem_declineButton);
        mHolder.actionButton = (Button) view.findViewById(R.id.userListItem_actionButton);

        Relation relation = (new Relation()).fromCursorToPOJO(cursor, -1);
        // gets the user associated with subscription
        mHolder.user = mController.getUserById(relation.getSubscriber());

        view.setTag(mHolder);
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Log.d(TAG, "View is binded");

        mHolder = (ViewHolder) view.getTag();
        Relation relation = (new Relation()).fromCursorToPOJO(cursor, -1);

        MyOnClickListener onClickListener = new MyOnClickListener(context, relation);
        mHolder.acceptButton.setOnClickListener(onClickListener);
        mHolder.declineButton.setOnClickListener(onClickListener);
        mHolder.actionButton.setOnClickListener(onClickListener);

        mHolder.fullName.setText(mHolder.user.toString().toUpperCase());
        mHolder.type.setText(mHolder.user.getUserType().toString());
        mHolder.checkins.setText(String.valueOf(mHolder.user.getCheckIns().size()) + " checkins");
        mHolder.since.setText("since " + new DateTime(mHolder.user.getTimestamp()).toString(Utils.getDatePattern(context)));

        if (relation.getStatus() == RelationStatus.CONFIRMED) {
            mHolder.acceptButton.setVisibility(View.GONE);
            mHolder.declineButton.setVisibility(View.GONE);
            mHolder.actionButton.setText(R.string.Unfollow);
            mHolder.actionButton.setEnabled(true);
            mHolder.actionButton.setVisibility(View.VISIBLE);
        } else if (relation.getStatus() == RelationStatus.PENDING) {
            mHolder.acceptButton.setVisibility(View.VISIBLE);
            mHolder.declineButton.setVisibility(View.VISIBLE);
            mHolder.actionButton.setVisibility(View.GONE);
        } else if (relation.getStatus() == RelationStatus.REJECTED) {
            mHolder.acceptButton.setVisibility(View.GONE);
            mHolder.declineButton.setVisibility(View.GONE);
            mHolder.actionButton.setText(R.string.Rejected);
            mHolder.actionButton.setEnabled(false);
            mHolder.actionButton.setVisibility(View.VISIBLE);
        }
    }

    private static class ViewHolder  {

        User user;
        TextView fullName;
        TextView type;
        TextView checkins;
        TextView since;
        ImageButton acceptButton;
        ImageButton declineButton;
        Button actionButton;
    }

    private class MyOnClickListener implements View.OnClickListener{

        Context mContext;
        Relation mRelation;
        public MyOnClickListener(Context context, Relation relation) {

            mContext = context;
            mRelation = relation;
        }

        @Override
        public void onClick(View v) {

            mRelation.setTimestamp(new Timestamp(System.currentTimeMillis()));

            switch (v.getId()){

                case R.id.userListItem_declineButton:

                    mRelation.setStatus(RelationStatus.REJECTED);
                    Log.d(TAG, "Decline button pressed");
                    break;

                case R.id.userListItem_actionButton:

                    mRelation.setStatus(RelationStatus.REJECTED);
                    Log.d(TAG, "Unfollow button pressed");
                    break;

                case R.id.userListItem_acceptButton:

                    mRelation.setStatus(RelationStatus.CONFIRMED);
                    Log.d(TAG, "Accept button pressed");
                    break;
            }

            if (mRelation.updateIt(mContext) > 0) {


                Log.d(TAG, "Relation successfully updated");
                notifyDataSetChanged();

                if (mRelation.getStatus() == RelationStatus.CONFIRMED) {
                    mHolder.acceptButton.setVisibility(View.GONE);
                    mHolder.declineButton.setVisibility(View.GONE);
                    mHolder.actionButton.setText(R.string.Unfollow);
                    mHolder.actionButton.setEnabled(true);
                    mHolder.actionButton.setVisibility(View.VISIBLE);
                } else if (mRelation.getStatus() == RelationStatus.PENDING) {
                    mHolder.acceptButton.setVisibility(View.VISIBLE);
                    mHolder.declineButton.setVisibility(View.VISIBLE);
                    mHolder.actionButton.setVisibility(View.GONE);
                } else if (mRelation.getStatus() == RelationStatus.REJECTED) {
                    mHolder.acceptButton.setVisibility(View.GONE);
                    mHolder.declineButton.setVisibility(View.GONE);
                    mHolder.actionButton.setText(R.string.Rejected);
                    mHolder.actionButton.setEnabled(false);
                    mHolder.actionButton.setVisibility(View.VISIBLE);
                }

            }
        }
    }
}
