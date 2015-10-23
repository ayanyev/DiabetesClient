package org.coursera.capstone.t1dteensclient.activities;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.common.GenericFragment;
import org.coursera.capstone.t1dteensclient.common.LifecycleLoggingActivity;
import org.coursera.capstone.t1dteensclient.entities.User;

/**
 * Created by Александр on 22.10.2015.
 */
public class NewGenericActivity extends LifecycleLoggingActivity
        implements DatePickerDialog.OnDateSetListener,
        GenericFragment.FragmentCallbacks {


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
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mMenuItems = getResources().getStringArray(R.array.drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mMenuItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
//                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
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
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {

            Fragment fragment = null;

            switch (position) {

                case HOME_FRAGMENT:
                    fragment = new MainFragment();
                case CHECKINS_FRAGMENT:
                    fragment = new CheckinsFragment();
                case SUBSCRIPTIONS_FRAGMENT:
                    fragment = new SubscriptionsFragment();
                case PREFERENCES_FRAGMENT:
                    fragment = new PreferencesFragment();
                case LOGOUT_FRAGMENT:

                case LOGIN_FRAGMENT:
                    fragment = new LoginFragment();
            }

            getFragmentManager().beginTransaction()
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
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onRegister(User user) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }
}
