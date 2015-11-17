package org.coursera.capstone.t1dteensclient.activities;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;

public class SubscriptionsFragment extends Fragment {

    private static final int SUBSCRIPTIONS_FRAGMENT = 0;
    private static final int SUBSCRIBERS_FRAGMENT = 1;

    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_subscriptions, container, false);

        TabLayout tabs = (TabLayout) view.findViewById(R.id.subscriptionsTabView);

        tabs.addTab(tabs.newTab().setText("You follow"));
        tabs.addTab(tabs.newTab().setText("You're followed by"));
        tabs.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabs.getTabAt(0).select();

        Utils.requestSyncOnDemand(getActivity().getApplicationContext(),
                ServiceContract.RELATIONS_DATA_URI);

        viewPager = (ViewPager) view.findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getFragmentManager(), tabs.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
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
    public void onDetach() {
        super.onDetach();
        viewPager.clearOnPageChangeListeners();
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {

                case SUBSCRIPTIONS_FRAGMENT:
                    return new SubscriptionsListFragment();
                case SUBSCRIBERS_FRAGMENT:
                    return new SubscribersListFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
