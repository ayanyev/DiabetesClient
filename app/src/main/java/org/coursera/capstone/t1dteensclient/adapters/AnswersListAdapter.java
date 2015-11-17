package org.coursera.capstone.t1dteensclient.adapters;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.coursera.capstone.t1dteensclient.R;
import org.coursera.capstone.t1dteensclient.common.TimePickerFragment;
import org.coursera.capstone.t1dteensclient.entities.Answer;
import org.coursera.capstone.t1dteensclient.entities.Option;
import org.coursera.capstone.t1dteensclient.entities.Question;
import org.coursera.capstone.t1dteensclient.entities.enums.AnswerType;

import java.util.List;

public class AnswersListAdapter extends ArrayAdapter<Answer> {

    public static final int TEXT_TYPE = 0;
    public static final int OPTIONS_TYPE = 1;
    public static final int TIME_PICKER_TYPE = 2;
    public static final int NUMBER_TYPE = 3;
    private static final int TYPES_COUNT = 4;

    LayoutInflater mInflater;
    List<Answer> mAnswers;
    Context mContext;
    ViewHolder mHolder;
    boolean mEditable;

    public AnswersListAdapter(Context context, List<Answer> answers, boolean editable) {
        super(context, -1, answers);

        mContext = context;
        mAnswers = answers;
        mInflater = LayoutInflater.from(context);
        mEditable = editable;
    }

    public void changeData(List<Answer> answers){

        mAnswers = answers;
        notifyDataSetChanged();
    }

    @Override
    public Answer getItem(int position) {
        return super.getItem(position).loadQuestion(mContext);
    }

    public List<Answer> getItems(){

        return mAnswers;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public int getItemViewType(int position) {

        Answer item = getItem(position);
        if (item.getQuestion().getAnswerType() == AnswerType.TEXT)
            return TEXT_TYPE;
        else if (item.getQuestion().getAnswerType() == AnswerType.OPTIONS)
            return OPTIONS_TYPE;
        else if (item.getQuestion().getAnswerType() == AnswerType.TIME)
            return TIME_PICKER_TYPE;
        else if (item.getQuestion().getAnswerType() == AnswerType.NUMBER)
            return NUMBER_TYPE;
        else return -1;
    }

    @Override
    public int getViewTypeCount() {
        return TYPES_COUNT;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Answer answer = mAnswers.get(position);
        Question question = answer.getQuestion().loadOptions(mContext);
        int type = getItemViewType(position);
        mHolder = new ViewHolder();

        if (convertView == null) {

            switch (type) {
                case TEXT_TYPE:
                    convertView = mInflater.inflate(R.layout.answer_text, parent, false);
                    mHolder.question = (TextView) convertView.findViewById(R.id.questionText);
                    mHolder.answer = (EditText) convertView.findViewById(R.id.answerText);
                    mHolder.answer.setEnabled(mEditable);
                    convertView.setTag(mHolder);
                    break;
                case NUMBER_TYPE:
                    convertView = mInflater.inflate(R.layout.answer_number, parent, false);
                    mHolder.question = (TextView) convertView.findViewById(R.id.questionText);
                    mHolder.answer = (EditText) convertView.findViewById(R.id.answerText);
                    mHolder.answer.setEnabled(mEditable);
                    convertView.setTag(mHolder);
                    break;
                case OPTIONS_TYPE:
                    convertView = mInflater.inflate(R.layout.answer_option, parent, false);
                    mHolder.question = (TextView) convertView.findViewById(R.id.questionText);
                    mHolder.options = (Spinner) convertView.findViewById(R.id.answerOptions);
                    mHolder.options.setEnabled(mEditable);
                    convertView.setTag(mHolder);
                    break;
                case TIME_PICKER_TYPE:
                    convertView = mInflater.inflate(R.layout.answer_time, parent, false);
                    mHolder.question = (TextView) convertView.findViewById(R.id.questionText);
                    mHolder.time = (EditText) convertView.findViewById(R.id.answerTime);
                    mHolder.time.setEnabled(mEditable);
                    convertView.setTag(mHolder);
                    break;
            }

        } else
                mHolder = (ViewHolder) convertView.getTag();

        switch (type) {
            case TEXT_TYPE:
                mHolder.question.setText(question.getText());
                mHolder.answer.setText(answer.getText());
                if (mEditable)
                    mHolder.answer.addTextChangedListener(new textInputOnTextChangedListener(position));
//                } else {
//                    mHolder.answer.setText(answer.getText());
//                }
                break;
            case OPTIONS_TYPE:
                mHolder.question.setText(question.getText());
                // set adapter for the spinner with defaultOption as default selection
                ArrayAdapter<Option> spinnerAdapter =
                        new ArrayAdapter<>(mContext,
                                android.R.layout.simple_spinner_item,
                                question.getOptions());
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mHolder.options.setAdapter(spinnerAdapter);
                for (Option option : question.getOptions()){
                    if (option.getText().equals(answer.getText())) {
                        mHolder.options.setSelection(spinnerAdapter.getPosition(option));
                        break;
                    }
                if (mEditable)
                    mHolder.options.setOnItemSelectedListener(new spinnerOnItemSelectedListener(position));
//                else {
//
//                    for (Option option : question.getOptions()){
//                        if (option.getText().equals(answer.getText())) {
//                            mHolder.options.setSelection(spinnerAdapter.getPosition(option));
//                            break;
//                        }
//                    }
                }
                break;
            case TIME_PICKER_TYPE:
                mHolder.question.setText(question.getText());
                mHolder.time.setText(answer.getText());
                if (mEditable)
                    mHolder.time.addTextChangedListener(new textInputOnTextChangedListener(position));
//                else
//                    mHolder.time.setText(answer.getText());
                break;
            case NUMBER_TYPE:
                mHolder.question.setText(question.getText());
                mHolder.answer.setText(answer.getText());
                if (mEditable)
                    mHolder.answer.addTextChangedListener(new textInputOnTextChangedListener(position));
//                else
//                    mHolder.answer.setText(answer.getText());
                break;
        }
        return convertView;
    }

    public boolean validate(int position, View listItem) {

        int type = getItemViewType(position);

        if (mAnswers.get(position).getText() == null &&
                (type == NUMBER_TYPE || type == TEXT_TYPE || type == TIME_PICKER_TYPE)) {

            EditText t = null;

            switch (type) {
                case NUMBER_TYPE:
                    t = (EditText) listItem.findViewById(R.id.answerText);
                case TEXT_TYPE:
                    t = (EditText) listItem.findViewById(R.id.answerText);
                case TIME_PICKER_TYPE:
                    t = (EditText) listItem.findViewById(R.id.answerTime);
            }

            t.setError("must not be empty");
            return false;
        }
        return true;
    }

    private class textInputOnTextChangedListener implements TextWatcher{

        int adapterPosition;
        public textInputOnTextChangedListener(int adapterPosition) {
            this.adapterPosition = adapterPosition;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            // updates answer text and value in reals time

            getItem(adapterPosition).setText(s.toString());
            if (getItemViewType(adapterPosition) == NUMBER_TYPE && !s.toString().equals(""))
                getItem(adapterPosition).setValue(Integer.parseInt(s.toString()));
        }
    }

    private class spinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener{

        int adapterPosition;
        public spinnerOnItemSelectedListener(int adapterPosition) {
            this.adapterPosition = adapterPosition;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            // updates answer text and value in reals time
            Option selectedOption = (Option) parent.getItemAtPosition(position);
            getItem(adapterPosition).setText(selectedOption.getText());
            getItem(adapterPosition).setValue((int) selectedOption.getWeight());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private static class ViewHolder  {

        TextView question;
        Spinner options;
        EditText answer;
        EditText time;
    }
}
