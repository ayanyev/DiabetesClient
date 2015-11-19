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
import android.widget.TextView;

import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.activities.MainActivity;
import org.coursera.capstone.t1dteensclient.common.GenericLoaderFragment;
import org.coursera.capstone.t1dteensclient.controllers.SvcController;
import org.coursera.capstone.t1dteensclient.entities.Relation;
import org.coursera.capstone.t1dteensclient.entities.User;
import org.coursera.capstone.t1dteensclient.entities.enums.RelationStatus;
import org.joda.time.DateTime;

import java.sql.Timestamp;

public class UserListCursorAdapter extends CursorAdapter {

    final String TAG = getClass().getSimpleName();
    SvcController mController;
    ViewHolder holder;

    public UserListCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mController = new SvcController(context);
    }

    @Override
    public Object getItem(int position) {
        return holder.user;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_user, parent, false);
        holder = new ViewHolder();

        holder.fullName = (TextView) view.findViewById(R.id.userListItem_fullName);
        holder.type = (TextView) view.findViewById(R.id.userListItem_type);
        holder.checkins = (TextView) view.findViewById(R.id.userListItem_checkins);
        holder.since = (TextView) view.findViewById(R.id.userListItem_since);
        holder.followButton = (Button) view.findViewById(R.id.userListItem_followButton);

        Relation relation = (new Relation()).fromCursorToPOJO(cursor, -1);
        // gets the user associated with subscription
        holder.user = mController.getUserById(relation.getSubscription());

        view.setTag(holder);
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        holder = (ViewHolder) view.getTag();

        Relation relation = (new Relation()).fromCursorToPOJO(cursor, -1);

        MyOnClickListener onClickListener = new MyOnClickListener(context, relation);
        holder.followButton.setOnClickListener(onClickListener);

        holder.fullName.setText(holder.user.getFirstName().toUpperCase()
                            + " " + holder.user.getLastName().toUpperCase());
        holder.type.setText(holder.user.getUserType().toString());
        holder.checkins.setText(String.valueOf(holder.user.getCheckIns().size()) + " checkins");
        holder.since.setText("since " + new DateTime(holder.user.getTimestamp()).toString("dd MMM yy"));

        if (relation.getStatus() == RelationStatus.CONFIRMED) {
            holder.followButton.setText(R.string.Unfollow);
            holder.followButton.setEnabled(true);
        } else if (relation.getStatus() == RelationStatus.PENDING) {
            holder.followButton.setText(R.string.Pending);
            holder.followButton.setEnabled(false);
        } else if (relation.getStatus() == RelationStatus.REJECTED) {
            holder.followButton.setText(R.string.Rejected);
            holder.followButton.setEnabled(false);
        }
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

                case R.id.userListItem_followButton:

                    mRelation.setStatus(RelationStatus.REJECTED);
                    Log.d(TAG, "Unfollow button pressed");
                    break;
            }

            if (mRelation.updateIt(mContext) > 0) {

                Log.d(TAG, "Relation successfully updated");
                notifyDataSetChanged();

                if (mRelation.getStatus() == RelationStatus.CONFIRMED) {
                    holder.followButton.setText(R.string.Unfollow);
                    holder.followButton.setEnabled(true);
                } else if (mRelation.getStatus() == RelationStatus.PENDING) {
                    holder.followButton.setText(R.string.Pending);
                    holder.followButton.setEnabled(false);
                } else if (mRelation.getStatus() == RelationStatus.REJECTED) {
                    holder.followButton.setText(R.string.Rejected);
                    holder.followButton.setEnabled(false);
                }
            }
        }
    }

    private static class ViewHolder  {

        User user = null;
        TextView fullName;
        TextView type;
        TextView checkins;
        TextView since;
        Button followButton;
    }
}
