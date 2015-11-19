package org.coursera.capstone.t1dteensclient.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceFragment;
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
import android.widget.TextView;

import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.common.LifecycleLoggingActivity;
import org.coursera.capstone.t1dteensclient.fragments.CheckinsListFragment;
import org.coursera.capstone.t1dteensclient.fragments.PreferencesFragment;
import org.coursera.capstone.t1dteensclient.fragments.SubscriptionsFragment;
import org.coursera.capstone.t1dteensclient.fragments.UserFragment;

public class MainActivity extends LifecycleLoggingActivity
        implements DatePickerDialog.OnDateSetListener {


    private final String TAG = getClass().getSimpleName();

    public static final String FRAGMENT_TAG = "This is fragment";
    private static final int USER_FRAGMENT = 0;
    private static final int CHECKINS_FRAGMENT = 1;
    private static final int SUBSCRIPTIONS_FRAGMENT = 2;
    private static final int PREFERENCES_FRAGMENT = 3;
    private static final int LOGOUT = 4;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mMenuItems;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private Context mContext;
    private TextView login;

    Fragment fragment = null;
    private PreferenceFragment prefFragment = null;
    private static int mCurrentFragment = USER_FRAGMENT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        // TODO remove after testing
//        login = (TextView) findViewById(R.id.currentLogin);
//        login.setText(Utils.getCurrentUserName(this).toUpperCase());

        ////////
        mTitle = getTitle();
        mDrawerTitle = getResources().getString(R.string.app_name);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // sets the mAdapter for the drawer's menu
        mMenuItems = getResources().getStringArray(R.array.drawer_items_array);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, mMenuItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // TODO change starting frame
        if (savedInstanceState != null)
            mCurrentFragment = savedInstanceState.getInt(FRAGMENT_TAG, USER_FRAGMENT);
        else
            setFragment(mCurrentFragment);


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
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {

            if (position == LOGOUT) {

                Utils.setGuestUserCredentials(mContext);
                finish();
                startActivity(new Intent(mContext, LoginActivity.class));

            } else
                setFragment(position);
        }
    }

    private void setFragment(int position) {

        mCurrentFragment = position;

        switch (position) {

            case USER_FRAGMENT:
                if (prefFragment != null) getFragmentManager().beginTransaction()
                        .remove(getFragmentManager().findFragmentById(prefFragment.getId()))
                        .commit();
                prefFragment = null;
                fragment = new UserFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("which userType", UserFragment.LOCAL_USER);
                fragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment, FRAGMENT_TAG)
                    .commit();
                break;
            case CHECKINS_FRAGMENT:
                if (prefFragment != null) getFragmentManager().beginTransaction()
                        .remove(getFragmentManager().findFragmentById(prefFragment.getId()))
                        .commit();
                prefFragment = null;
                fragment =  new CheckinsListFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment, FRAGMENT_TAG)
                        .commit();
                break;
            case SUBSCRIPTIONS_FRAGMENT:
                if (prefFragment != null) getFragmentManager().beginTransaction()
                        .remove(getFragmentManager().findFragmentById(prefFragment.getId()))
                        .commit();
                prefFragment = null;
                fragment =  new SubscriptionsFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment, FRAGMENT_TAG)
                        .commit();
                break;
            case PREFERENCES_FRAGMENT:
                if (fragment != null) getSupportFragmentManager().beginTransaction()
                        .remove(getSupportFragmentManager().findFragmentById(fragment.getId()))
                        .commit();
                fragment = null;
                prefFragment = new PreferencesFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, prefFragment)
                        .commit();
                break;
        }

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        mTitle = mMenuItems[position];
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(FRAGMENT_TAG, mCurrentFragment);
        super.onSaveInstanceState(outState);
    }
}
