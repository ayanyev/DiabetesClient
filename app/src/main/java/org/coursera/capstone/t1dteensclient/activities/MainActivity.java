package org.coursera.capstone.t1dteensclient.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
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

import org.coursera.capstone.t1dteensclient.Constants;
import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.common.GenericListFragment;
import org.coursera.capstone.t1dteensclient.common.LifecycleLoggingActivity;
import org.coursera.capstone.t1dteensclient.entities.User;
import org.coursera.capstone.t1dteensclient.provider.ContentProviderObserver;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;
import org.coursera.capstone.t1dteensclient.sync.SyncAdapter;

public class MainActivity extends LifecycleLoggingActivity implements DatePickerDialog.OnDateSetListener,
        GenericListFragment.FragmentCallbacks {


    private final String TAG = getClass().getSimpleName();
    Account mAccount;
    ContentResolver mResolver;
    ContentProviderObserver observer;
    Fragment mFragment;

    private static final String FRAGMENT_TAG = "This is fragment";
    private static final int HOME_FRAGMENT = 0;
    private static final int CHECKINS_FRAGMENT = 1;
    private static final int SUBSCRIPTIONS_FRAGMENT = 2;
    private static final int PREFERENCES_FRAGMENT = 3;
    private static final int LOGOUT = 4;
    public static final int LOGIN_FRAGMENT = 5;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mMenuItems;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private Context mContext;
    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        // TODO remove after testing
        login = (TextView) findViewById(R.id.currentLogin);
        login.setText(Utils.getCurrentUserName(this).toUpperCase());

        mResolver = getContentResolver();
        mAccount = CreateSyncAccount(this);
        observer = new ContentProviderObserver(new Handler(), mAccount);
        mResolver.registerContentObserver(ServiceContract.DATABASE_URI, true, observer);

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
        if (savedInstanceState == null) {
            setFragment(CHECKINS_FRAGMENT);
        }
    }

    @Override
    public void onRegister(User user) {
//        super.onRegister(user);

        Utils.rememberCurrentUserCredentials(mContext, user);

        mFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction()
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
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        if (accountManager.addAccountExplicitly(newAccount, null, null)) {

            ContentResolver.setSyncAutomatically(newAccount, ServiceContract.AUTHORITY, true);

/*            ContentResolver.addPeriodicSync(
                    newAccount,
                    ServiceContract.AUTHORITY,
                    Bundle.EMPTY,
                    SYNC_INTERVAL);*/

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

            if (position == LOGOUT) {

                Utils.setGuestUserCredentials(mContext);
                finish();

            } else
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
                fragment = new CheckinsListFragment();
                break;
            case SUBSCRIPTIONS_FRAGMENT:
                fragment = new SubscriptionsFragment();
                break;
            case PREFERENCES_FRAGMENT:
                fragment = new PreferencesFragment();
                break;
            case LOGIN_FRAGMENT:
                fragment = new LoginFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment, FRAGMENT_TAG)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        mTitle = mMenuItems[position];
        mDrawerLayout.closeDrawer(mDrawerList);
    }
}
