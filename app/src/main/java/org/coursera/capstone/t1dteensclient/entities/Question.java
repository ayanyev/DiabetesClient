package org.coursera.capstone.t1dteensclient.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.coursera.capstone.t1dteensclient.entities.enums.AnswerType;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.coursera.capstone.t1dteensclient.provider.ServiceContract.*;

public class Question implements  EntityInterface, Parcelable {

    @JsonIgnore
    private Long _id;
    @JsonProperty("id")
    private long question_id;
    private String text;
    private AnswerType answerType;
    private Boolean required;
    private Boolean rateable;
    private int order;
    private Date timestamp;
    private List<Option> options = new ArrayList<>();

    public Question() {
        this._id = null;
    }


    public Boolean getRateable() {
        return rateable;
    }

    public void setRateable(Boolean rateable) {
        this.rateable = rateable;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public long getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(long question_id) {
        this.question_id = question_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AnswerType getAnswerType() {
        return answerType;
    }

    public void setAnswerType(AnswerType answerType) {
        this.answerType = answerType;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Question fromCursorToPOJO(Cursor question, int position, Cursor options) {

        // fills QUESTION fields from cursor

        Long id;

        if (position != -1) question.moveToPosition(position);

        this.set_id(question.getLong(question
                .getColumnIndex(QUESTIONS_COLUMN_ID)));
        id = question.getLong(question
                .getColumnIndex(QUESTIONS_COLUMN_QUESTION_ID));
        this.setQuestion_id((id == 0) ? null : id);
        this.setText(question.getString(question
                .getColumnIndex(QUESTIONS_COLUMN_TEXT)));
        this.setAnswerType(AnswerType.valueOf(question.getString(question
                .getColumnIndex(QUESTIONS_COLUMN_ANSWER_TYPE))));
        this.setRequired(question.getInt(question
                .getColumnIndex(QUESTIONS_COLUMN_REQUIRED)) == 1);
        this.setRequired(question.getInt(question
                .getColumnIndex(QUESTIONS_COLUMN_ORDER)) == 1);
        this.setTimestamp(new Timestamp(question.getLong(question
                .getColumnIndex(QUESTIONS_COLUMN_TIMESTAMP))));

        // if options cursor is not empty
        if ((options != null) && (options.moveToFirst())) {
            Option option = new Option();
            while (!options.isAfterLast()) {
                this.getOptions().add(option.fromCursorToPOJO(options));
                options.moveToNext();
            }
        }

        return this;
    }

    @Override
    public ContentValues toContentValues() {

        ContentValues cv = new ContentValues();

        if (_id != null) cv.put(QUESTIONS_COLUMN_ID, _id);
        cv.put(QUESTIONS_COLUMN_QUESTION_ID, question_id);
        cv.put(QUESTIONS_COLUMN_REQUIRED, required);
        cv.put(QUESTIONS_COLUMN_ORDER, order);
        cv.put(QUESTIONS_COLUMN_ANSWER_TYPE, String.valueOf(answerType));
        cv.put(QUESTIONS_COLUMN_TEXT, text);
        cv.put(QUESTIONS_COLUMN_TIMESTAMP,
                (new Date(System.currentTimeMillis()).getTime()));

        return cv;
    }

    @Override
    public Uri saveIt(Context context) {
        return null;
    }

    @Override
    public int updateIt(Context context) {
        return 0;
    }

    public Question loadOptions(final Context context) {

        final long questionId = this.get_id();

        if(this.getAnswerType() == AnswerType.OPTIONS) {

            try {
                this.options = (new AsyncTask<Void, Void, List<Option>>() {
                    @Override
                    protected List<Option> doInBackground(Void... params) {

                        List<Option> optionListList = new ArrayList<>();

                        // gets current options for question
                        Cursor options = context.getContentResolver()
                                .query(OPTIONS_DATA_URI,
                                        null,
                                        QUESTIONS_COLUMN_QUESTION_ID + " = ?",
                                        new String[]{String.valueOf(questionId)},
                                        null);

                        if (options != null && options.moveToFirst()) {
                            while (!options.isAfterLast()) {
                                optionListList.add((new Option()).fromCursorToPOJO(options));
                                options.moveToNext();
                            }
                            return optionListList;
                        } else
                            return null;
                    }
                }.execute()).get();

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return this;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this._id);
        dest.writeLong(this.question_id);
        dest.writeString(this.text);
        dest.writeInt(this.answerType == null ? -1 : this.answerType.ordinal());
        dest.writeValue(this.required);
        dest.writeInt(this.order);
        dest.writeLong(timestamp != null ? timestamp.getTime() : -1);
        dest.writeTypedList(options);
    }

    protected Question(Parcel in) {
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.question_id = in.readLong();
        this.text = in.readString();
        int tmpAnswerType = in.readInt();
        this.answerType = tmpAnswerType == -1 ? null : AnswerType.values()[tmpAnswerType];
        this.required = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.order = in.readInt();
        long tmpTimestamp = in.readLong();
        this.timestamp = tmpTimestamp == -1 ? null : new Date(tmpTimestamp);
        this.options = in.createTypedArrayList(Option.CREATOR);
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };


}