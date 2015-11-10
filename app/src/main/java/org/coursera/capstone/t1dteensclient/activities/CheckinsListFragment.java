package org.coursera.capstone.t1dteensclient.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.adapters.CheckinsListAdapter;
import org.coursera.capstone.t1dteensclient.common.GenericListFragment;
import org.coursera.capstone.t1dteensclient.common.GenericLoaderFragment;
import org.coursera.capstone.t1dteensclient.entities.CheckIn;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;

import static org.coursera.capstone.t1dteensclient.provider.ServiceContract.*;

public class CheckinsListFragment extends GenericLoaderFragment {

    private static final int CREATE_CHECKIN = 1;
    private CheckinsListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mAdapter = new CheckinsListAdapter(getActivity(), null, false);
        setListAdapter(mAdapter);

        //TODO consider where to place after the testing
        Utils.requestSyncOnDemand(getActivity().getApplicationContext(),
                QUESTIONS_DATA_URI);

        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getActivity(),
                                CHECKINS_DATA_URI,
                                null,
                                null,
                                null,
                                CHECKINS_COLUMN_TIMESTAMP);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        super.onLoadFinished(loader, data);
        mAdapter.changeCursor((Cursor) data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        super.onLoaderReset(loader);
        mAdapter.changeCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.checkins_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.add_checkin:

                // creates new checkin and sets empty answers
                CheckIn checkin = new CheckIn();
                checkin.setNewAnswers(getActivity());

                Intent intent = new Intent(this.getActivity(), CheckinActivity.class);
                intent.putExtra("checkin", checkin);
                
                startActivityForResult(intent, CREATE_CHECKIN);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CREATE_CHECKIN){

            if (resultCode == Activity.RESULT_OK)
                restartLoader().forceLoad();
            else
                Toast.makeText(getActivity(), R.string.checkin_save_fail, Toast.LENGTH_LONG).show();

        }
    }
}
