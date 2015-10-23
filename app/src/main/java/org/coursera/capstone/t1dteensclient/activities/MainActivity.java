package org.coursera.capstone.t1dteensclient.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import org.coursera.capstone.t1dteensclient.Constants;
import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.common.GenericFragment;
import org.coursera.capstone.t1dteensclient.common.LifecycleLoggingActivity;
import org.coursera.capstone.t1dteensclient.entities.User;
import org.coursera.capstone.t1dteensclient.provider.ContentProviderObserver;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;
import org.coursera.capstone.t1dteensclient.sync.SyncAdapter;

public class MainActivity extends LifecycleLoggingActivity implements DatePickerDialog.OnDateSetListener,
        GenericFragment.FragmentCallbacks {

    private final String TAG = getClass().getSimpleName();
    SharedPreferences prefs;
    String loggedAs;
    Account mAccount;
    ContentResolver mResolver;
    ContentProviderObserver observer;
    SyncAdapter mSyncAdapter;
    Fragment mFragment;

    ///////
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
    ///////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check if user is logged in
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

/*        // TODO. REMOVE AFTER TESTING DONE
        prefs.edit().putString("username", "teen2")
                    .putString("password", "2222")
                    .commit();*/
        loggedAs = prefs.getString("username", "guest");

        if (loggedAs.equals("guest")) {

            setFragment(LOGIN_FRAGMENT);

        } else {

            mResolver = getContentResolver();
            mAccount = CreateSyncAccount(this);
            observer = new ContentProviderObserver(new Handler(), mAccount);
            mResolver.registerContentObserver(ServiceContract.CHECKINS_DATA_URI, true, observer);

            ////////
            mTitle = getTitle();
            mDrawerTitle = getResources().getString(R.string.app_name);
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

            if (savedInstanceState == null) {
                setFragment(HOME_FRAGMENT);
            }
            ////////


        }
    }

    @Override
    public void onRegister(User user) {
//        super.onRegister(user);

        prefs.edit().putString("username", user.getUsername())
                .putString("password", user.getPassword())
                .apply();

        mFragment = new MainFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, mFragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mResolver.unregisterContentObserver(observer);
    }

    // TODO create real account not dummy
    public static Account CreateSyncAccount(Context context) {

        Account newAccount = new Account(Constants.ACCOUNT, Constants.ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(ACCOUNT_SERVICE);

        if (accountManager.addAccountExplicitly(newAccount, null, null)) {

            ContentResolver.setSyncAutomatically(newAccount, ServiceContract.AUTHORITY, true);

        } else {

        }

        return newAccount;
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

            setFragment(position);
        }
    }

    private void setFragment(int position) {

        Fragment fragment = null;

        switch (position) {

            case HOME_FRAGMENT:
                fragment = new MainFragment();
                break;
            case CHECKINS_FRAGMENT:
                fragment = new CheckinsFragment();
                break;
            case SUBSCRIPTIONS_FRAGMENT:
                fragment = new SubscriptionsFragment();
                break;
            case PREFERENCES_FRAGMENT:
                fragment = new PreferencesFragment();
                break;
            case LOGOUT_FRAGMENT:
                break;
            case LOGIN_FRAGMENT:
                fragment = new LoginFragment();
                break;
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        mTitle = mMenuItems[position];
        mDrawerLayout.closeDrawer(mDrawerList);
    }


    @Override
    public void setTitle(CharSequence title) {
        getActionBar().setTitle(title);
    }
}
