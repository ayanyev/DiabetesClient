package org.coursera.capstone.t1dteensclient.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.widget.Toast;

import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.activities.LoginActivity;
import org.coursera.capstone.t1dteensclient.client.RequestResult;
import org.coursera.capstone.t1dteensclient.common.ConfigurableOps;
import org.coursera.capstone.t1dteensclient.common.GenericActivity;
import org.coursera.capstone.t1dteensclient.common.GenericAsyncTask;
import org.coursera.capstone.t1dteensclient.common.GenericAsyncTaskOps;
import org.coursera.capstone.t1dteensclient.controllers.SvcController;
import org.coursera.capstone.t1dteensclient.entities.User;

import java.lang.ref.WeakReference;

public class LoginOps implements ConfigurableOps,
                                    GenericAsyncTaskOps<User, Void, RequestResult>,
                                    LoginActivity.LoginCallbacks{

    private static final String TAG =
            LoginOps.class.getSimpleName();


    private WeakReference<LoginActivity> mActivity;
    private Context mApplicationContext;
    private GenericAsyncTask<User, Void, RequestResult, LoginOps> mAsyncTask;
    private SvcController mController;

    @Override
    public void onConfiguration(GenericActivity activity, boolean firstTimeIn) {

        mActivity = new WeakReference<>((LoginActivity) activity);

        if (firstTimeIn) {

            mApplicationContext = activity.getApplicationContext();
            mController = new SvcController(mApplicationContext);
        }
    }

    // callback from LoginActivity after register button pressed

    @Override
    public void onRegister(User mUser) {

        mAsyncTask = new GenericAsyncTask<>(this);
        mAsyncTask.execute(mUser);
    }

    @Override
    public RequestResult doInBackground(User... params) {
        return mController.register(params[0]);
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onPostExecute(RequestResult result) {

        User user = result.getUser();

        if (result.getState() == RequestResult.UserState.CONFLICT) {

            TextInputLayout username = (TextInputLayout) mActivity.get().findViewById(R.id.username_layout);
            username.setError("username '" + user.getUsername() + "' already exists");
            username.requestFocus();

            mActivity.get().showProgress(false);

        } else if (result.getState() == RequestResult.UserState.SERVER_ERROR) {

            mActivity.get().showProgress(false);

            Toast.makeText(mActivity.get(),
                    "Registration failed",
                    Toast.LENGTH_LONG).show();

        } else if (result.getState() == RequestResult.UserState.ADDED){

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mApplicationContext);

            prefs.edit().putString("username", user.getUsername())
                    .putString("password", user.getPassword())
                    .commit();

            mActivity.get().showProgress(false);

            Toast.makeText(mActivity.get(),
                    user.getUsername().toUpperCase() + " was successfully registered",
                    Toast.LENGTH_LONG).show();

            Intent data = new Intent();
            data.putExtra("userId", user.getId());
            mActivity.get().setResult(Activity.RESULT_OK, data);
            mActivity.get().finish();
        }
    }
}
