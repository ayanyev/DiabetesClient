package org.coursera.capstone.t1dteensclient.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.coursera.capstone.t1dteensclient.Constants;
import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.client.RequestResult;
import org.coursera.capstone.t1dteensclient.common.GenericListFragment;
import org.coursera.capstone.t1dteensclient.controllers.SvcController;
import org.coursera.capstone.t1dteensclient.entities.User;
import org.coursera.capstone.t1dteensclient.entities.enums.UserGender;
import org.coursera.capstone.t1dteensclient.entities.enums.UserType;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import retrofit.RetrofitError;

public class LoginFragment extends GenericListFragment
        implements DatePickerDialog.OnDateSetListener {

    private EditText mUsername, mPassword, mFirstName, mLastName, mDateOfBirth, mMedRecord;
    private Spinner  mGender, mUserType;
    private View mProgressView;
    private View mRegisterFormView;
    Button mRegisterButton;
    Button mDatePickerButton;
    private Activity mActivity;
    FragmentAsyncTask mAsyncTask;
    SvcController mController;

    public LoginFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        initializeViews(rootView);

        mActivity = getActivity();

        mController = new SvcController(mActivity);

        return rootView;
    }

    private void initializeViews(View rootView) {

        mUsername = (EditText) rootView.findViewById(R.id.username);
        mPassword = (EditText) rootView.findViewById(R.id.password);
        mFirstName = (EditText) rootView.findViewById(R.id.firstName);
        mLastName = (EditText) rootView.findViewById(R.id.lastName);
        mGender = (Spinner) rootView.findViewById(R.id.gender);
        mUserType = (Spinner) rootView.findViewById(R.id.user_type);
        mDateOfBirth = (EditText) rootView.findViewById(R.id.dob);
        mMedRecord = (EditText) rootView.findViewById(R.id.medRecord);

        mRegisterButton = (Button) rootView.findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgress(true);
                mAsyncTask = new FragmentAsyncTask();
                mAsyncTask.execute(makeNewUser());
            }
        });

        mRegisterFormView = rootView.findViewById(R.id.register_form);
        mProgressView = rootView.findViewById(R.id.login_progress);

        mDateOfBirth = (EditText) rootView.findViewById(R.id.dob);
        mDatePickerButton = (Button) rootView.findViewById(R.id.btn_pick_dob);
    }

    private User makeNewUser()  {

        User user = new User();

        user.setUsername(mUsername.getText().toString());
        user.setPassword(mPassword.getText().toString());
        user.setFirstName(mFirstName.getText().toString());
        user.setLastName(mLastName.getText().toString());
        user.setGender(UserGender.valueOf(mGender.getSelectedItem().toString()));
        user.setUserType(UserType.valueOf(mUserType.getSelectedItem().toString()));
        user.setMedicalRecord(Integer.valueOf(mMedRecord.getText().toString()));

        SimpleDateFormat dt = new SimpleDateFormat(Constants.DATE_PATTERN);
        try {
            user.setDateOfBirth(dt.parse(mDateOfBirth.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return user;
    }

    private class FragmentAsyncTask extends AsyncTask<User, Void, RequestResult> {

        @Override
        public RequestResult doInBackground(User... params) {

            try {
                return mController.register(params[0]);
            } catch (RetrofitError retrofitError) {
                return new RequestResult(RequestResult.Message.FAILED_TO_CONNECT_TO_SERVER, (User) null);
            }
        }

        @Override
        public void onPostExecute(RequestResult result) {

            User user = result.getUser();

            if (result.getMessage() == RequestResult.Message.CONFLICT) {

                TextInputLayout username = (TextInputLayout) mActivity.findViewById(R.id.username_layout);
                username.setError("username '" + user.getUsername() + "' already exists");
                username.requestFocus();

                showProgress(false);

            } else if (result.getMessage() == RequestResult.Message.SERVER_ERROR) {

                showProgress(false);

                Toast.makeText(mActivity,
                        "Registration failed",
                        Toast.LENGTH_LONG).show();

            } else if (result.getMessage() == RequestResult.Message.FAILED_TO_CONNECT_TO_SERVER) {

                showProgress(false);

                Toast.makeText(mActivity,
                        "Failed to connect to server\nCheck internet connection",
                        Toast.LENGTH_LONG).show();

            } else if (result.getMessage() == RequestResult.Message.ADDED) {

                showProgress(false);

                Toast.makeText(mActivity,
                        user.getUsername().toUpperCase() + " was successfully registered",
                        Toast.LENGTH_LONG).show();

                mCallbacks.onRegister(user);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        super.onDateSet(view, year, monthOfYear, dayOfMonth);

        DateTime dt = new DateTime(year, monthOfYear, dayOfMonth, 0, 0);
        mDateOfBirth.setText(dt.toString(Constants.DATE_PATTERN));
    }
}
