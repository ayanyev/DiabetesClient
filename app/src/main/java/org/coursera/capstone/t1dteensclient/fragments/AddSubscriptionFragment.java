package org.coursera.capstone.t1dteensclient.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.adapters.UserListAdapter;
import org.coursera.capstone.t1dteensclient.client.RequestResult;
import org.coursera.capstone.t1dteensclient.common.GenericLoaderFragment;
import org.coursera.capstone.t1dteensclient.controllers.SvcController;
import org.coursera.capstone.t1dteensclient.entities.Relation;
import org.coursera.capstone.t1dteensclient.entities.User;
import org.coursera.capstone.t1dteensclient.entities.enums.UserType;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;

import static org.coursera.capstone.t1dteensclient.provider.ServiceContract.*;

public class AddSubscriptionFragment extends GenericLoaderFragment {

    UserListAdapter mAdapter;
    private static SvcController mController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle("Add subscriptions");

        // sets GenericListFragment mAdapter
        mAdapter = new UserListAdapter(getActivity(), null);
        setListAdapter(mAdapter);

        mController = new SvcController(getActivity());
        View view =  super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        return new UserListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        super.onLoadFinished(loader, data);
        RequestResult result = (RequestResult) data;

        if (result.getMessage() == RequestResult.Message.SERVER_ERROR) {

            Toast.makeText(getActivity(),
                    "Server error",
                    Toast.LENGTH_LONG).show();

        } else if (result.getMessage() == RequestResult.Message.FAILED_TO_CONNECT_TO_SERVER) {

            Toast.makeText(getActivity(),
                    "Failed to connect to server\nCheck internet connection",
                    Toast.LENGTH_LONG).show();

        } else if (result.getMessage() == RequestResult.Message.OK) {

            if (result.getUsers().size() > 0){
                msgLayout.setVisibility(View.GONE);
                mAdapter.changeData(result.getUsers());
            }
            else {
                msgView.setText("Currently there is no userType to add\nSwipe down to refresh");
                msgLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        super.onLoaderReset(loader);
        mAdapter.changeData(null);
    }

    private static class UserListLoader extends AsyncTaskLoader{

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
                // adds current userType id to eviction list
                ids.add(Utils.getCurrentUserId(context));

                // finds all distinct subscription (userType) ids
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

}
