package org.coursera.capstone.t1dteensclient.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.adapters.SubscribersListCursorAdapter;
import org.coursera.capstone.t1dteensclient.adapters.UserListCursorAdapter;
import org.coursera.capstone.t1dteensclient.common.GenericLoaderFragment;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;

public class  SubscribersListFragment extends GenericLoaderFragment {

    SubscribersListCursorAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup view;

        // sets GenericListFragment mAdapter
        mAdapter = new SubscribersListCursorAdapter(getActivity(), null, false);
        setListAdapter(mAdapter);

        view = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);


        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String selection = "subscription = ?";
        String[] selectionArgs = new String[]{String.valueOf(Utils.getCurrentUserId(getActivity()))};

        return
                new CursorLoader(getActivity(),
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
            msgView.setText("Currently no user is following you\nSwipe down to refresh");
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

}
