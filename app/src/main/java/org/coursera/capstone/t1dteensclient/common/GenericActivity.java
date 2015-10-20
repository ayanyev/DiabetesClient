package org.coursera.capstone.t1dteensclient.common;

import android.app.DatePickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * This Activity provides a framework that automatically handles
 * runtime configuration changes in conjunction with an instance of
 * OpsType, which must implement the ConfigurableOps interface.  It
 * also extends LifecycleLoggingActivity so that all lifecycle hook
 * method calls are automatically logged.
 */


public class GenericActivity<OpsType extends ConfigurableOps> 
       extends LifecycleLoggingActivity implements DatePickerDialog.OnDateSetListener {
    /**
     * Used to retain the OpsType state between runtime configuration
     * changes.
     */

    private final String TAG = getClass().getSimpleName();

    private final RetainedFragmentManager mRetainedFragmentManager 
        = new RetainedFragmentManager(this.getFragmentManager(),
                                      TAG);
    private OpsType mOpsInstance;

    private EditText date;

    public OpsType onCreate(Class<OpsType> opsType) {
/*        // Call up to the super class.
        super.onCreate(savedInstanceState);*/

        Log.d(TAG,"onCreate");


        try {
            // Handle configuration-related events, including the
            // initial creation of an Activity and any subsequent
            // runtime configuration changes.
            return handleConfiguration(opsType);
        } catch (InstantiationException e) {
            Log.d(TAG,
                  "handleConfiguration "
                  + e);
            // Propagate this as a runtime exception.
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            Log.d(TAG,
                  "handleConfiguration "
                  + e);
            // Propagate this as a runtime exception.
            throw new RuntimeException(e);
        }
    }

    /**
     * Handle hardware (re)configurations, such as rotating the
     * display.
     *
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    public OpsType handleConfiguration(Class<OpsType> opsType)
        throws InstantiationException, IllegalAccessException {

        // If this method returns true it's the first time the
        // Activity has been created.
        if (mRetainedFragmentManager.firstTimeIn()) {
            Log.d(TAG,
                  "First time onCreate() call");

            // Initialize the GenericActivity fields.
            return initialize(opsType);
        } else {
            // The RetainedFragmentManager was previously initialized,
            // which means that a runtime configuration change
            // occured.
            Log.d(TAG,
                  "Second or subsequent onCreate() call");

            // Try to obtain the OpsType instance from the
            // RetainedFragmentManager.
            mOpsInstance =
                mRetainedFragmentManager.get(opsType.getSimpleName());

            // This check shouldn't be necessary under normal
            // circumstances, but it's better to lose state than to
            // crash!
            if (mOpsInstance == null) 
                // Initialize the GenericActivity fields.
                return initialize(opsType);
            else {
                // Inform it that the runtime configuration change has
                // completed.
                mOpsInstance.onConfiguration(this,
                        false);
                return  mOpsInstance;
            }
        }
    }

    /**
     * Initialize the GenericActivity fields.
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    private OpsType initialize(Class<OpsType> opsType)
            throws InstantiationException, IllegalAccessException {
        // Create the OpsType object.
        mOpsInstance = opsType.newInstance();

        // Put the OpsInstance into the RetainedFragmentManager under
        // the simple name.
        mRetainedFragmentManager.put(opsType.getSimpleName(),
                mOpsInstance);

        // Perform the first initialization.
        mOpsInstance.onConfiguration(this,
                                     true);

        return  mOpsInstance;
    }

    /**
     * Return the initialized OpsType instance for use by the
     * application.
     */
    public OpsType getOps() {
        return mOpsInstance;
    }

    /**
     * Return the initialized OpsType instance for use by the
     * application.
     */
    public RetainedFragmentManager getRetainedFragmentManager() {
        return mRetainedFragmentManager;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }

    public void showDatePickerDialog(View v) {

//      shows Date Picker dialog
        DatePickerFragment dialog = new DatePickerFragment();
        dialog.show(getFragmentManager(), "datePicker");
    }
}

