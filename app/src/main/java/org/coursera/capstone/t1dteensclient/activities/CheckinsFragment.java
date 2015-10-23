package org.coursera.capstone.t1dteensclient.activities;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.coursera.capstone.t1dteensclient.adapters.CheckinsListAdapter;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;

public class CheckinsFragment extends ListFragment implements LoaderManager.LoaderCallbacks {

    CheckinsListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getLoaderManager().initLoader(0, null, this);

        mAdapter = new CheckinsListAdapter(getActivity(), null, false);

        setListAdapter(mAdapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getActivity(),
                                ServiceContract.CHECKINS_DATA_URI,
                                null,
                                null,
                                null,
                                null);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

       mAdapter.changeCursor((Cursor) data);

    }



    @Override
    public void onLoaderReset(Loader loader) {

        mAdapter.changeCursor(null);

    }
}
