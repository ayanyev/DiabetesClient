package org.coursera.capstone.t1dteensclient.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.coursera.capstone.t1dteensclient.Constants;
import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.client.RequestResult;
import org.coursera.capstone.t1dteensclient.controllers.SvcController;
import org.coursera.capstone.t1dteensclient.entities.User;

public class LoginActivity extends Activity {

    private static final int MAIN_ACTIVITY_REQUEST_CODE = 0;
    private Button mLoginButton;
    private Button mRegisterButton;
    private EditText mPasswordInput;
    private EditText mUsernameInput;
    private CheckBox mRememberCheckBox;
    private SvcController mController;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();

        // TODO. REMOVE AFTER TESTING DONE
//        Utils.setGuestUserCredentials(this);

        // if user is remembered on the device starts MainActivity,
        // else - go on with creating LoginActivity

        if (Utils.isUserRemembered(this)) {

            // TODO think about starting MainActivity in case the user is deactivated or removed from sever
            startMainActivity();

        } else {

            setContentView(R.layout.activity_login);

            initViews();

            mLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // makes login views visible
                    if (mUsernameInput.getVisibility() == View.GONE) {

                        mUsernameInput.setVisibility(View.VISIBLE);
                        mPasswordInput.setVisibility(View.VISIBLE);
                        mRememberCheckBox.setVisibility(View.VISIBLE);

                        // send user request to server and if user exists and is active
                        // starts MainActivity
                    } else {

                        String username = mUsernameInput.getText().toString();
                        String password = mPasswordInput.getText().toString();

                        // light input validation
                        if (!username.isEmpty() && !password.isEmpty()) {

                            // checks if a user exists with given credentials
                            User userToSignIn = isUserActive(username, password);

                            if (userToSignIn != null) {

                                // sets views to initial state
                                initViews();

                                // put user credentials into shared preferences on demand
                                if (mRememberCheckBox.isChecked()) {
                                    // sets password because it is not present in server response
                                    userToSignIn.setPassword(password);
                                    Utils.rememberCurrentUserCredentials(mContext, userToSignIn);
                                }

                                startMainActivity();
                            }
                        } else {
                            if (username.isEmpty()) mUsernameInput.setError("must no be empty");
                            if (password.isEmpty()) mPasswordInput.setError("must no be empty");
                        }
                    }
                }
            });

            mRegisterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    private void startMainActivity() {

        startActivityForResult(new Intent(this, MainActivity.class), MAIN_ACTIVITY_REQUEST_CODE);
    }

    private User isUserActive(String username, String password){

        mController = new SvcController(this);

        RequestResult result = mController.getUserByCredentials(new User(username, password));

        if (result.getMessage() == RequestResult.Message.FAILED_TO_CONNECT_TO_SERVER) {

            Toast.makeText(this,
                    "Failed to connect to server\nCheck internet connection",
                    Toast.LENGTH_LONG).show();
        } else if (result.getMessage() == RequestResult.Message.USER_NOT_FOUND) {

            Toast.makeText(this,
                    "Wrong credentials",
                    Toast.LENGTH_LONG).show();
        } else if (result.getMessage() == RequestResult.Message.WRONG_PASSWORD) {

            Toast.makeText(this,
                    "Wrong password",
                    Toast.LENGTH_LONG).show();

        } else if (result.getMessage() == RequestResult.Message.USER_ACTIVE) {

            return result.getUser();
        }
        return null;
    }

    private void initViews() {

        mLoginButton = (Button) findViewById(R.id.button_login);
        mRegisterButton = (Button) findViewById(R.id.button_register);
        mUsernameInput = (EditText) findViewById(R.id.input_username);
        mPasswordInput = (EditText) findViewById(R.id.input_password);
        mRememberCheckBox = (CheckBox) findViewById(R.id.checkBox_remember);

        mUsernameInput.getText().clear();
        mPasswordInput.getText().clear();

        mUsernameInput.setVisibility(View.GONE);
        mPasswordInput.setVisibility(View.GONE);
        mRememberCheckBox.setVisibility(View.GONE);
    }

}
