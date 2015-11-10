package org.coursera.capstone.t1dteensclient.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.adapters.AnswersListAdapter;
import org.coursera.capstone.t1dteensclient.entities.CheckIn;
import org.coursera.capstone.t1dteensclient.entities.enums.CheckInStatus;

import java.util.ArrayList;
import java.util.List;

public class CheckinActivityFragment extends Fragment  implements Button.OnClickListener{

    CheckIn mCheckin;
    AnswersListAdapter mAdapter;
    Button mButtonSave;
    ListView mListAnswers;

    public CheckinActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);

        mCheckin = getActivity().getIntent().getParcelableExtra("checkin");
        mAdapter = new AnswersListAdapter(this.getActivity(), mCheckin.getAnswers());


        View view = inflater.inflate(R.layout.fragment_checkin, container, false);

        mListAnswers = (ListView) view.findViewById(R.id.answersList);
        mListAnswers.setAdapter(mAdapter);

        mButtonSave = (Button) view.findViewById(R.id.buttonSaveCheckin);
        mButtonSave.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.buttonSaveCheckin:

                if (validateInput()) {

                    mCheckin.setAnswers(mAdapter.getItems());
                    mCheckin.setStatus(CheckInStatus.PASSED);
                    mCheckin.setUser_id(Utils.getCurrentUserId(getActivity()));

                    if (mCheckin.saveIt(getActivity()) != null) {

                        getActivity().setResult(Activity.RESULT_OK);
                    } else
                        getActivity().setResult(Activity.RESULT_CANCELED);

                    getActivity().finish();
                }
        }
    }

    private boolean validateInput() {

        List<Boolean> validateResult = new ArrayList<>();

        for (int i = 0; i < mAdapter.getCount() - 1; i++)
            validateResult.add(mAdapter.validate(i, mListAnswers.getChildAt(i)));

        return !validateResult.contains(false);

    }
}
