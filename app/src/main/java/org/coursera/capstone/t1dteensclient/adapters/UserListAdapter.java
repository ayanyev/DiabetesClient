package org.coursera.capstone.t1dteensclient.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.coursera.capstone.t1dteensclient.Constants;
import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.activities.MainActivity;
import org.coursera.capstone.t1dteensclient.entities.Relation;
import org.coursera.capstone.t1dteensclient.entities.User;
import org.coursera.capstone.t1dteensclient.fragments.AddSubscriptionFragment;
import org.coursera.capstone.t1dteensclient.fragments.SubscriptionsFragment;
import org.coursera.capstone.t1dteensclient.fragments.SubscriptionsListFragment;
import org.joda.time.DateTime;

import java.util.List;

public class UserListAdapter extends BaseAdapter {

    private final SharedPreferences prefs;
    LayoutInflater mInflater;
    List<User> mUsers;
    Context mContext;

    public UserListAdapter(Context context, List<User> users) {

        mUsers = users;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void changeData(List<User> users){

        mUsers = users;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (mUsers != null ? mUsers.size() : 0);
    }

    @Override
    public Object getItem(int position) {
        return (mUsers != null ? mUsers.get(position) : 0);
    }

    @Override
    public long getItemId(int position) {
        return (mUsers != null ? mUsers.get(position).getId() : 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        User user = mUsers.get(position);

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_user, parent, false);

            holder = new ViewHolder();
            holder.fullName = (TextView) convertView.findViewById(R.id.userListItem_fullName);
            holder.type = (TextView) convertView.findViewById(R.id.userListItem_type);
            holder.checkins = (TextView) convertView.findViewById(R.id.userListItem_checkins);
            holder.since = (TextView) convertView.findViewById(R.id.userListItem_since);
            holder.followButton = (Button) convertView.findViewById(R.id.userListItem_followButton);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        convertView.setVisibility(user.getId() == prefs.getLong("currentUserId", 13) ? View.GONE : View.VISIBLE);

        holder.fullName.setText(user.getFirstName().toUpperCase() + " " + user.getLastName().toUpperCase());
        holder.type.setText(user.getUserType().toString());
        holder.checkins.setText(String.valueOf(user.getCheckIns().size()) + " checkins");
        holder.since.setText("since " + new DateTime(user.getTimestamp()).toString("dd MMM yy"));
        holder.followButton.setText(R.string.Follow);

        holder.followButton.setOnClickListener(new adapterOnClickListener(position));

        return convertView;
    }

    private class adapterOnClickListener implements View.OnClickListener{

        int position;
        public adapterOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {

            Relation relation = new Relation();
            relation.setSubscriber(Utils.getCurrentUserId(mContext));
            relation.setSubscription(mUsers.get(position).getId());

            if (relation.saveIt(mContext) != null) {
                ((Button) v).setText(R.string.pending);
                v.setEnabled(false);
            }

            ((MainActivity) mContext).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new SubscriptionsFragment())
                    .commit();
        }
    }

    private static class ViewHolder  {

        TextView fullName;
        TextView type;
        TextView checkins;
        TextView since;
        Button followButton;
    }
}
