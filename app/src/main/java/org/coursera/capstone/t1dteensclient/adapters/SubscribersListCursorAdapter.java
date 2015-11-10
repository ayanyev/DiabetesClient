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
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.controllers.SvcController;
import org.coursera.capstone.t1dteensclient.entities.Relation;
import org.coursera.capstone.t1dteensclient.entities.User;
import org.coursera.capstone.t1dteensclient.entities.enums.RelationStatus;
import org.joda.time.DateTime;

import java.sql.Timestamp;

public class SubscribersListCursorAdapter extends CursorAdapter {

    final String TAG = getClass().getSimpleName();
    SvcController mController;

    public SubscribersListCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mController = new SvcController(context);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_subscriber, parent, false);

        Log.d(TAG, "New view created");

        ViewHolder holder = new ViewHolder();

        holder.fullName = (TextView) view.findViewById(R.id.userListItem_fullName);
        holder.type = (TextView) view.findViewById(R.id.userListItem_type);
        holder.checkins = (TextView) view.findViewById(R.id.userListItem_checkins);
        holder.since = (TextView) view.findViewById(R.id.userListItem_since);
        holder.acceptButton = (ImageButton) view.findViewById(R.id.userListItem_acceptButton);
        holder.declineButton = (ImageButton) view.findViewById(R.id.userListItem_declineButton);
        holder.actionButton = (Button) view.findViewById(R.id.userListItem_actionButton);

        Relation relation = (new Relation()).fromCursorToPOJO(cursor, -1);
        // gets the user associated with subscription
        holder.user = mController.getUserById(relation.getSubscriber());

        view.setTag(holder);
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Log.d(TAG, "View is binded");

        ViewHolder holder = (ViewHolder) view.getTag();
        Relation relation = (new Relation()).fromCursorToPOJO(cursor, -1);

        MyOnClickListener onClickListener = new MyOnClickListener(context, relation);
        holder.acceptButton.setOnClickListener(onClickListener);
        holder.declineButton.setOnClickListener(onClickListener);
        holder.actionButton.setOnClickListener(onClickListener);

        holder.fullName.setText(holder.user.getFirstName().toUpperCase()
                + " " + holder.user.getLastName().toUpperCase());
        holder.type.setText(holder.user.getUserType().toString());
        holder.checkins.setText(String.valueOf(holder.user.getCheckIns().size()) + " checkins");
        holder.since.setText("since " + new DateTime(holder.user.getTimestamp()).toString("dd MMM yy"));

        if (relation.getStatus() == RelationStatus.CONFIRMED) {
            holder.acceptButton.setVisibility(View.GONE);
            holder.declineButton.setVisibility(View.GONE);
            holder.actionButton.setText(R.string.Unfollow);
            holder.actionButton.setEnabled(true);
            holder.actionButton.setVisibility(View.VISIBLE);
        } else if (relation.getStatus() == RelationStatus.PENDING) {
            holder.acceptButton.setVisibility(View.VISIBLE);
            holder.declineButton.setVisibility(View.VISIBLE);
            holder.actionButton.setVisibility(View.GONE);
        } else if (relation.getStatus() == RelationStatus.REJECTED) {
            holder.acceptButton.setVisibility(View.GONE);
            holder.declineButton.setVisibility(View.GONE);
            holder.actionButton.setText(R.string.Rejected);
            holder.actionButton.setEnabled(false);
            holder.actionButton.setVisibility(View.VISIBLE);
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

            }
        }
    }
}
