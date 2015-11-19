package org.coursera.capstone.t1dteensclient.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.common.LifecycleLoggingActivity;
import org.coursera.capstone.t1dteensclient.fragments.UserFragment;

public class UserActivity extends LifecycleLoggingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);

        Fragment fragment = new UserFragment();
        fragment.setArguments(getIntent().getExtras().getBundle("args"));

        getSupportFragmentManager().beginTransaction()
                .add(R.id.user_activity_content_frame, fragment, null)
                .commit();
    }
}
