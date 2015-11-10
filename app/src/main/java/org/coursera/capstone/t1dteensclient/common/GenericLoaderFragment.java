package org.coursera.capstone.t1dteensclient.common;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.coursera.capstone.t1dteensclient.adapters.UserListCursorAdapter;

public class GenericLoaderFragment extends GenericListFragment implements LoaderManager.LoaderCallbacks{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        getLoaderManager().initLoader(0, null, this).forceLoad();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                restartLoader().forceLoad();
            }
        });

        // workaround to set swiperefreshlayout refreshing
        // https://code.google.com/p/android/issues/detail?id=77712
        mSwipeRefreshLayout.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        mSwipeRefreshLayout.setRefreshing(true);

        return view;
    }

    public Loader restartLoader() {

        return getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
