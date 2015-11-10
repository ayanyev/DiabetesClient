package org.coursera.capstone.t1dteensclient.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import org.coursera.capstone.t1dteensclient.Constants;
import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.adapters.UserListCursorAdapter;
import org.coursera.capstone.t1dteensclient.common.GenericLoaderFragment;
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

        ListView list;
        FloatingActionButton fab;
        ViewGroup view;

        view = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);

        // fab button init
        if (view != null) {

            // inflates FAB layout to the FrameLayout containing ListView

            list = (ListView) view.findViewById(android.R.id.list);
            // parent FrameLayout
            ViewGroup lframe = (ViewGroup) list.getParent();
            inflater.inflate(R.layout.fab, lframe, true);

            fab = (FloatingActionButton) lframe.findViewById(R.id.fab);
            fab.attachToListView(list);
            fab.setOnClickListener(this);
        }

        return view;
    }

    @Override
    public void onClick(View v) {

        Fragment fragment = new AddSubscriptionFragment();
        Bundle bundle = new Bundle();

        bundle.putInt("Init key", Constants.INIT_ASYNC_TASK);
        fragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
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
            msgView.setText("Currently there is no user on subscriptions list\nTry add one by pressing the button in right bottom corner");
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
