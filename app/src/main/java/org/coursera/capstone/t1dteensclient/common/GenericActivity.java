package org.coursera.capstone.t1dteensclient.common;

import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;

import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.activities.CheckinsListFragment;
import org.coursera.capstone.t1dteensclient.activities.LoginFragment;
import org.coursera.capstone.t1dteensclient.activities.MainFragment;
import org.coursera.capstone.t1dteensclient.activities.PreferencesFragment;
import org.coursera.capstone.t1dteensclient.activities.SubscriptionsFragment;
import org.coursera.capstone.t1dteensclient.entities.User;

public class GenericActivity extends LifecycleLoggingActivity
        implements DatePickerDialog.OnDateSetListener,
                    GenericListFragment.FragmentCallbacks {

    private static final int HOME_FRAGMENT = 0;
    private static final int CHECKINS_FRAGMENT = 1;
    private static final int SUBSCRIPTIONS_FRAGMENT = 2;
    private static final int PREFERENCES_FRAGMENT = 3;
    private static final int LOGOUT_FRAGMENT = 4;
    public static final int LOGIN_FRAGMENT = 5;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mMenuItems;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // sets the adapter for the drawer's menu
        mMenuItems = getResources().getStringArray(R.array.drawer_items_array);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, mMenuItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRegister(User user) {

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {

            Fragment fragment = null;

            switch (position) {

                case HOME_FRAGMENT:
                    fragment = new MainFragment();
                case CHECKINS_FRAGMENT:
                    fragment = new CheckinsListFragment();
                case SUBSCRIPTIONS_FRAGMENT:
                    fragment = new SubscriptionsFragment();
                case PREFERENCES_FRAGMENT:
                    fragment = new PreferencesFragment();
                case LOGOUT_FRAGMENT:

                case LOGIN_FRAGMENT:
                    fragment = new LoginFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

            // Highlight the selected item, update the title, and close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(mMenuItems[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        getActionBar().setTitle(title);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }



}

