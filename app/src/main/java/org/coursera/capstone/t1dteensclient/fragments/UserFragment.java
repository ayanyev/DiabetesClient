package org.coursera.capstone.t1dteensclient.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.coursera.capstone.t1dteensclient.Constants;
import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.controllers.SvcController;
import org.coursera.capstone.t1dteensclient.entities.Answer;
import org.coursera.capstone.t1dteensclient.entities.CheckIn;
import org.coursera.capstone.t1dteensclient.entities.User;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;

import static org.coursera.capstone.t1dteensclient.provider.ServiceContract.*;

public class UserFragment extends Fragment implements LoaderManager.LoaderCallbacks{

    PagerAdapter mAdapter;
    ViewPager mViewPager;
    TabLayout mTabs;
    ArrayList<Answer> answers = new ArrayList<>();
    public final static int LOCAL_USER = 0;
    public final static int REMOTE_USER = 1;
    int mUserType;
    User mUser;
    private static SvcController mController;
    public static final String USER_TYPE = "userType";
    public static final String USER = "user";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_user, container, false);

        setHasOptionsMenu(true);

        mUserType = getArguments().getInt(USER_TYPE);
        mUser = getArguments().getParcelable(USER);

        mTabs = (TabLayout) view.findViewById(R.id.userTabView);
        mTabs.addTab(mTabs.newTab().setText("Day"));
        mTabs.addTab(mTabs.newTab().setText("Week"));
        mTabs.addTab(mTabs.newTab().setText("2Weeks"));
        mTabs.addTab(mTabs.newTab().setText("Month"));

        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(3);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));
        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (mUserType == REMOTE_USER) {
            getActivity().setTitle(mUser.toString().toUpperCase() + "'s feedback");
            mAdapter = new PagerAdapter(getFragmentManager(), mTabs.getTabCount(), getAnswers(mUser));
            mViewPager.setAdapter(mAdapter);
        }
        else {
            getActivity().setTitle("My feedback");
            getLoaderManager().initLoader(0, null, this).forceLoad();
        }
        return view;
    }

    private ArrayList<Answer> getAnswers(User mUser) {

        ArrayList<Answer> a = new ArrayList<>();
        for (CheckIn c : mUser.getCheckIns()) {

            if (c.getAnswers().size() > 0 && c.getAnswers() != null)
                a.addAll(c.getAnswers());
        }
        return  a;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        Loader loader;
        long period = System.currentTimeMillis() - Constants.DAY_IN_MILLIS * 30;

        String selection = ANSWERS_COLUMN_TIMESTAMP + " > ?";
//        + " AND " + ANSWERS_COLUMN_QUESTION_ID + " IN (1,2)";
        String[] selectionArgs = new String[]{String.valueOf(period)};
        loader = new CursorLoader(getActivity(),
                ANSWERS_DATA_URI,
                null,
                selection,
                selectionArgs,
                ANSWERS_COLUMN_TIMESTAMP);

//        if (mUserType == LOCAL_USER) {
//
//
//        }
//        else {
//
//
//
//            mController = new SvcController(getActivity());
//            loader = new AnswersLoader(getActivity(), mUser.getId(), period);
//        }

        return loader;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        switch (mUserType) {

            case LOCAL_USER:

                Cursor cursor = (Cursor) data;
                if (cursor != null && cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        answers.add(new Answer().fromCursorToPOJO(cursor));
                        cursor.moveToNext();
                    }
                }
                break;

            case REMOTE_USER:

                answers = (ArrayList<Answer>) data;
                break;

        }

        mAdapter = new PagerAdapter(getFragmentManager(), mTabs.getTabCount(), answers);
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader loader) {

//        mViewPager.setAdapter(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (mUserType == REMOTE_USER)
            inflater.inflate(R.menu.user_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            // shows full user info
            case R.id.user_info:
                AlertDialog.Builder builder = new AlertDialog
                        .Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
                builder.setTitle("User info");
                builder.setIcon(R.drawable.user_48);

                final TextView info = new TextView(getActivity());
                info.setText(mUser.toFullInfo(getActivity()));
                builder.setView(info);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private static class AnswersLoader extends AsyncTaskLoader<List<Answer>> {

        Context context;
        long period;
        long userId;

        public AnswersLoader(Context context, long userId, long period) {
            super(context);
            this.context = context;
            this.period = period;
            this.userId = userId;
        }

        @Override
        public List<Answer> loadInBackground() {

            List<Answer> answers = new ArrayList<>();
            try {

                List<CheckIn> result = mController.getCheckins(userId, period);

                for (CheckIn checkin : result) {
                    answers.addAll(checkin.getAnswers());
                }

            } catch (RetrofitError retrofitError) {

                return null;
            }
            return answers;
        }

        @Override
        public void deliverResult(List<Answer> data) {
            super.deliverResult(data);
        }
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        int mNumOfTabs;
        ArrayList<Answer> answers;

        public PagerAdapter(FragmentManager fm, int NumOfTabs, ArrayList<Answer> answers) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
            this.answers = answers;
        }

        @Override
        public Fragment getItem(int position) {

            return ChartsFragment.newInstance(position, answers);
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}



