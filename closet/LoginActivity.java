package org.coursera.capstone.t1dteensclient.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import org.coursera.capstone.t1dteensclient.Constants;
import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.common.GenericActivity;
import org.coursera.capstone.t1dteensclient.entities.User;
import org.coursera.capstone.t1dteensclient.entities.enums.UserGender;
import org.coursera.capstone.t1dteensclient.entities.enums.UserType;
import org.coursera.capstone.t1dteensclient.presenter.MainActivityOps;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity
        extends GenericActivity<MainActivityOps> {

    private SharedPreferences prefs;

    public interface LoginCallbacks {
        void onRegister(User user);
    }

    private LoginCallbacks mCallbacks;

    // UI references.
    private EditText mUsername, mPassword, mFirstName, mLastName, mDateOfBirth, mMedRecord;
    private Spinner  mGender, mUserType;
    private View mProgressView;
    private View mRegisterFormView;
    private Button mRegisterButton;
    private Button mDatePickerButton;
    private MainActivityOps mLoginOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mLoginOps = super.onCreate(MainActivityOps.class);

        mCallbacks = (MainActivityOps) mLoginOps;

        initializeViews();

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    }

    private void initializeViews() {

        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mFirstName = (EditText) findViewById(R.id.firstName);
        mLastName = (EditText) findViewById(R.id.lastName);
        mGender = (Spinner) findViewById(R.id.gender);
        mUserType = (Spinner) findViewById(R.id.user_type);
        mDateOfBirth = (EditText) findViewById(R.id.dob);
        mMedRecord = (EditText) findViewById(R.id.medRecord);

        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgress(true);
                mCallbacks.onRegister(makeNewUser());
            }
        });

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.login_progress);

        mDateOfBirth = (EditText) findViewById(R.id.dob);
        mDatePickerButton = (Button) findViewById(R.id.btn_pick_dob);
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

    private void initViews() {

/*        ArrayAdapter<CharSequence> adapter;

        Spinner userTypeSpinner = (Spinner) findViewById(R.id.user_type);
        adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.user_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(adapter);

        Spinner genderSpinner = (Spinner) findViewById(R.id.gender);
        adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.genders_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);*/
    }

    private void attemptLogin() {
/*        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsername.setError(null);
        mPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mUsername.setError(getString(R.string.error_field_required));
            focusView = mUsername;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mUsername.setError(getString(R.string.error_invalid_email));
            focusView = mUsername;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            mCallbacks.onRegister(new User());
        }*/
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
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
        super.onDateSet(view, year, monthOfYear, dayOfMonth);

        DateTime dt = new DateTime(year, monthOfYear, dayOfMonth, 0, 0);
        mDateOfBirth.setText(dt.toString(Constants.DATE_PATTERN));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCallbacks = null;
    }
}

