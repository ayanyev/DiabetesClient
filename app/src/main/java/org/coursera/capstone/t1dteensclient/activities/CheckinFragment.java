package org.coursera.capstone.t1dteensclient.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.adapters.AnswersListAdapter;
import org.coursera.capstone.t1dteensclient.entities.CheckIn;
import org.coursera.capstone.t1dteensclient.entities.enums.CheckInStatus;

import java.util.ArrayList;
import java.util.List;

public class CheckinFragment extends Fragment  implements Button.OnClickListener{

    CheckIn mCheckin;
    AnswersListAdapter mAdapter;
    LinearLayout mButtonLayout;
    ListView mListAnswers;
    TextView mID, mTimestamp;
    Button mButtonSave, mButtonSkip;

    public CheckinFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);

        View view = inflater.inflate(R.layout.fragment_checkin, container, false);

            mListAnswers = (ListView) view.findViewById(R.id.answersList);
            mButtonLayout = (LinearLayout) view.findViewById(R.id.buttonsLayout);
            mButtonSave = (Button) view.findViewById(R.id.buttonCheckinSave);
            mButtonSkip = (Button) view.findViewById(R.id.buttonCheckinSkip);
            mID = (TextView) view.findViewById(R.id.checkinID);
            mTimestamp = (TextView) view.findViewById(R.id.timeTaken);

            mCheckin = getArguments().getParcelable("checkin");
            boolean isEditable = getArguments().getBoolean("ifAnswersAreEditable");

            mAdapter = new AnswersListAdapter(this.getActivity(),
                                                mCheckin != null ? mCheckin.getAnswers() : null,
                                                isEditable);
            mListAnswers.setAdapter(mAdapter);

            if (isEditable) {
                mButtonSave.setOnClickListener(this);
                mButtonSkip.setOnClickListener(this);
                getActivity().setTitle("New checkin");
                mID.setVisibility(View.GONE);
                mTimestamp.setVisibility(View.GONE);
            } else {
                mButtonLayout.setVisibility(View.GONE);
                getActivity().setTitle("Checkin details");
                mID.setText("ID: " + String.valueOf(mCheckin.get_id()));
                mTimestamp.setText("CREATED: " + Utils.timestampToDateTime(getActivity(), mCheckin.getTimestamp()));
            }

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.buttonCheckinSave:

                if (validateInput()) {

                    mCheckin.setAnswers(mAdapter.getItems());
                    mCheckin.setStatus(CheckInStatus.PASSED);

                    if (mCheckin.saveIt(getActivity()) != null) {

                        getActivity().setResult(Activity.RESULT_OK);
                    } else
                        getActivity().setResult(Activity.RESULT_CANCELED);

                    getActivity().finish();
                    break;
                }

            case R.id.buttonCheckinSkip:

                if (validateInput()) {

                    mCheckin.setStatus(CheckInStatus.SKIPPED);

                    if (mCheckin.saveIt(getActivity()) != null) {

                        getActivity().setResult(Activity.RESULT_OK);
                    } else
                        getActivity().setResult(Activity.RESULT_CANCELED);

                    getActivity().finish();
                    break;
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
