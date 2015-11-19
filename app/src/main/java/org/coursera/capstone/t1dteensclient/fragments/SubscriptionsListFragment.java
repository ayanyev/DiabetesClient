package org.coursera.capstone.t1dteensclient.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.melnykov.fab.FloatingActionButton;

import org.coursera.capstone.t1dteensclient.Constants;
import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.activities.UserActivity;
import org.coursera.capstone.t1dteensclient.adapters.UserListCursorAdapter;
import org.coursera.capstone.t1dteensclient.common.GenericLoaderFragment;
import org.coursera.capstone.t1dteensclient.entities.User;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;

public class SubscriptionsListFragment extends GenericLoaderFragment
        implements FloatingActionButton.OnClickListener {

    UserListCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // sets GenericListFragment mAdapter
        mAdapter = new UserListCursorAdapter(getActivity(), null, false);
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);

        addFABButton(inflater, R.drawable.account_plus);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fab:

                Fragment fragment = new AddSubscriptionFragment();
                Bundle bundle = new Bundle();

                bundle.putInt("Init key", Constants.INIT_ASYNC_TASK);
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String selection = "subscriber = ?";
        String[] selectionArgs = new String[]{String.valueOf(Utils.getCurrentUserId(getActivity()))};

        return new CursorLoader(getActivity(),
                ServiceContract.RELATIONS_DATA_URI,
                null,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        super.onLoadFinished(loader, data);

        Cursor cursor = (Cursor) data;

        if (cursor == null || cursor.getCount() == 0) {
            msgView.setText("Currently there is no userType on subscriptions list\nTry add one by pressing the button in right bottom corner");
            msgLayout.setVisibility(View.VISIBLE);
        } else {
            msgLayout.setVisibility(View.GONE);
            mAdapter.changeCursor((Cursor) data);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        super.onLoaderReset(loader);
        mAdapter.changeCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        User user = (User) mAdapter.getItem(position);

        Bundle bundle = new Bundle();
        bundle.putInt(UserFragment.USER_TYPE, UserFragment.REMOTE_USER);
        bundle.putParcelable(UserFragment.USER, user);

        Intent intent = new Intent(getActivity(), UserActivity.class);
        intent.putExtra("args", bundle);

        startActivity(intent);

    }
}
