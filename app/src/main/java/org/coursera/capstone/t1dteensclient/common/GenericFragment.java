package org.coursera.capstone.t1dteensclient.common;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import org.coursera.capstone.t1dteensclient.controllers.SvcController;
import org.coursera.capstone.t1dteensclient.entities.User;


public class GenericFragment extends Fragment implements LoaderManager.LoaderCallbacks {

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    public interface FragmentCallbacks {
       void onRegister(User user);
    }

    public  FragmentCallbacks mCallbacks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (FragmentCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
