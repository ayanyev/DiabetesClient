package org.coursera.capstone.t1dteensclient.activities;

import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.coursera.capstone.t1dteensclient.Constants;
import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.client.RequestResult;
import org.coursera.capstone.t1dteensclient.entities.Answer;
import org.coursera.capstone.t1dteensclient.entities.User;
import org.coursera.capstone.t1dteensclient.entities.enums.UserType;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;

import static org.coursera.capstone.t1dteensclient.provider.ServiceContract.*;

public class UserFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    PagerAdapter mAdapter;
    ViewPager mViewPager;
    TabLayout mTabs;
    ArrayList<Answer> answers = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_user, container, false);

        getLoaderManager().initLoader(0, null, this).forceLoad();

        mTabs = (TabLayout) view.findViewById(R.id.userTabView);
        mTabs.addTab(mTabs.newTab().setText("Day"));
        mTabs.addTab(mTabs.newTab().setText("Week"));
        mTabs.addTab(mTabs.newTab().setText("2Weeks"));
        mTabs.addTab(mTabs.newTab().setText("Month"));

        mAdapter = new PagerAdapter(getFragmentManager(), mTabs.getTabCount(), answers);

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

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        long period = System.currentTimeMillis() - Constants.DAY_IN_MILLIS * 30;

        String selection = ANSWERS_COLUMN_TIMESTAMP + " > ?";
//        + " AND " + ANSWERS_COLUMN_QUESTION_ID + " IN (1,2)";
        String[] selectionArgs = new String[]{String.valueOf(period)};

        return new CursorLoader(getActivity(),
                ANSWERS_DATA_URI,
                null,
                selection,
                selectionArgs,
                ANSWERS_COLUMN_TIMESTAMP);
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor cursor) {

        if (cursor != null && cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                answers.add(new Answer().fromCursorToPOJO(cursor));
                cursor.moveToNext();
            }
        }
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader loader) {

//        mViewPager.setAdapter(null);
    }

    private static class UserListLoader extends AsyncTaskLoader {

        Context context;

        public UserListLoader(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public Object loadInBackground() {

            List<Long> ids = new ArrayList<>();
            List<User> users;
            Cursor cursor;

            try {

                RequestResult result = mController.getAllUsers(UserType.TEEN);
                users = result.getUsers();
                // adds current user id to eviction list
                ids.add(Utils.getCurrentUserId(context));

                // finds all distinct subscription (user) ids
                cursor = context.getContentResolver().query(RELATIONS_DATA_URI_DISTINCT,
                        new String[]{"subscription"},
                        null,
                        null,
                        null);
                // removes users from add subscription list if it's already in it
                if (cursor != null && cursor.moveToFirst()) {
                    // makes list of distinct ids
                    while (!cursor.isAfterLast()) {
                        ids.add(cursor.getLong(cursor
                                .getColumnIndex(RELATIONS_COLUMN_SUBSCRIPTION)));
                        cursor.moveToNext();
                    }
                }
                List<User> evictionList = new ArrayList<>();

                for (User user : users) {
                    if (ids.contains(user.getId()))
                        evictionList.add(user);
                }

                users.removeAll(evictionList);
                cursor.close();
                result.setUsers(users);

                return result;

            } catch (RetrofitError retrofitError) {
                return new RequestResult(RequestResult.Message.FAILED_TO_CONNECT_TO_SERVER);
            }
        }

        @Override
        public void deliverResult(Object data) {
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



