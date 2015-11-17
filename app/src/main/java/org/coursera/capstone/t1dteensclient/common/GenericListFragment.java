package org.coursera.capstone.t1dteensclient.common;

/*
Copyright 2014 The Android Open Source Project

parts of the file used under the Apache License, Version 2.0

source: http://developer.android.com/intl/ru/samples/SwipeRefreshListFragment/src/
com.example.android.swiperefreshlistfragment/SwipeRefreshListFragment.html#l50
*/

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import org.coursera.capstone.t1dteensclient.Constants;
import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.adapters.UserListAdapter;
import org.coursera.capstone.t1dteensclient.client.RequestResult;
import org.coursera.capstone.t1dteensclient.controllers.SvcController;
import org.coursera.capstone.t1dteensclient.entities.User;
import org.coursera.capstone.t1dteensclient.entities.enums.UserType;

import retrofit.RetrofitError;

import static org.coursera.capstone.t1dteensclient.Constants.*;


public class GenericListFragment extends ListFragment
        implements FloatingActionButton.OnClickListener{

    public SwipeRefreshLayout mSwipeRefreshLayout;
    FrameLayout mListFragmentView;
    public TextView msgView;
    public LinearLayout msgLayout;
    public ListView mList;

    // FAB button onClickListener
    @Override
    public void onClick(View v) {

    }

    public interface FragmentCallbacks {
       void onRegister(User user);
    }

    public  FragmentCallbacks mCallbacks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mListFragmentView = (FrameLayout) super.onCreateView(inflater, container, savedInstanceState);

        mList = (ListView) mListFragmentView.findViewById(android.R.id.list);
        mList.setDivider(null);
        mList.setPadding(10, 5, 10, 5);

        // inflates message layout
        msgLayout = (LinearLayout) inflater.inflate(R.layout.message, null);
        msgView = (TextView) msgLayout.findViewById(R.id.msgView);
        mListFragmentView.addView(msgLayout);

        mSwipeRefreshLayout = new ListFragmentSwipeRefreshLayout(container.getContext());

        mSwipeRefreshLayout.addView(mListFragmentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mSwipeRefreshLayout.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        return mSwipeRefreshLayout;
    }

    public void addFABButton(LayoutInflater inflater, int drawable) {
        FloatingActionButton fab;// inflates FAB layout to the FrameLayout containing ListView
        ViewGroup lframe = (ViewGroup) mList.getParent();
        inflater.inflate(R.layout.fab, lframe, true);

        fab = (FloatingActionButton) lframe.findViewById(R.id.fab);
        fab.attachToListView(mList);
        fab.setOnClickListener(this);
        fab.setImageResource(drawable);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (FragmentCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }


    private class ListFragmentSwipeRefreshLayout extends SwipeRefreshLayout {

        public ListFragmentSwipeRefreshLayout(Context context) {
            super(context);
        }

        @Override
        public boolean canChildScrollUp() {
            final ListView listView = getListView();
            if (listView.getVisibility() == View.VISIBLE) {
                return canListViewScrollUp(listView);
            } else {
                return false;
            }
        }
    }

    private static boolean canListViewScrollUp(ListView listView) {
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            // For ICS and above we can call canScrollVertically() to determine this
            return ViewCompat.canScrollVertically(listView, -1);
        } else {
            // Pre-ICS we need to manually check the first visible item and the child view's top
            // value
            return listView.getChildCount() > 0 &&
                    (listView.getFirstVisiblePosition() > 0
                            || listView.getChildAt(0).getTop() < listView.getPaddingTop());
        }
    }



}
