package org.coursera.capstone.t1dteensclient.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.coursera.capstone.t1dteensclient.provider.ServiceContract;

import java.sql.Timestamp;
import java.util.Date;

import static org.coursera.capstone.t1dteensclient.provider.ServiceContract.*;

public class Answer implements EntityInterface, Parcelable {

    @JsonIgnore
    private Long _id;
    @JsonProperty("id")
    private Long answer_id;
    private Long questionId;
    @JsonIgnore
    private Question question;
    @JsonIgnore
    private Long checkInId;
    private String text;
    private Date timestamp;

    public Answer() {
    }

    public Answer(Question question) {
        this.questionId = question.getQuestion_id();
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(Long answer_id) {
        this.answer_id = answer_id;
    }

    public long getQuestionId() {
        return questionId;
    }

    public Long getCheckInId() {
        return checkInId;
    }

    public void setCheckInId(Long checkInId) {
        this.checkInId = checkInId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public ContentValues toContentValues(){

        ContentValues cv = new ContentValues();

        cv.put(ANSWERS_COLUMN_ID, _id);
        cv.put(ANSWERS_COLUMN_ANSWER_ID, answer_id);
        cv.put(ANSWERS_COLUMN_QUESTION_ID, questionId);
        cv.put(ANSWERS_COLUMN_CHECKIN_ID, checkInId);
        cv.put(ANSWERS_COLUMN_TEXT, text);
        cv.put(ANSWERS_COLUMN_TIMESTAMP, new Date(System.currentTimeMillis()).getTime());

        return cv;
    }

    @Override
    public Uri saveIt(Context context) {

        // inserts checking record to table ANSWERS
        Uri uri = context.getContentResolver()
                .insert(ANSWERS_DATA_URI, this.toContentValues());
        return null;
    }

    @Override
    public <T> int updateIt(Context context) {
        return 0;
    }

    public Answer fromCursorToPOJO (Cursor cursor){

        Long id;

        id = cursor.getLong(cursor
                .getColumnIndex(ANSWERS_COLUMN_ANSWER_ID));
        this.setAnswer_id(id == 0 ? null : id);

        this.setQuestionId(cursor.getLong(cursor
                .getColumnIndex(ANSWERS_COLUMN_QUESTION_ID)));

        id = cursor.getLong(cursor
                .getColumnIndex(ANSWERS_COLUMN_CHECKIN_ID));
        this.setCheckInId(id == 0 ? null : id);

        this.setText(cursor.getString(cursor
                .getColumnIndex(ANSWERS_COLUMN_TEXT)));
        this.setTimestamp(new Timestamp(cursor.getLong(cursor
                .getColumnIndex(ANSWERS_COLUMN_TIMESTAMP))));

        return this;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this._id);
        dest.writeValue(this.answer_id);
        dest.writeValue(this.questionId);
        dest.writeParcelable(this.question, flags);
        dest.writeValue(this.checkInId);
        dest.writeString(this.text);
        dest.writeLong(timestamp != null ? timestamp.getTime() : -1);
    }

    protected Answer(Parcel in) {
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.answer_id = (Long) in.readValue(Long.class.getClassLoader());
        this.questionId = (Long) in.readValue(Long.class.getClassLoader());
        this.question = in.readParcelable(Question.class.getClassLoader());
        this.checkInId = (Long) in.readValue(Long.class.getClassLoader());
        this.text = in.readString();
        long tmpTimestamp = in.readLong();
        this.timestamp = tmpTimestamp == -1 ? null : new Date(tmpTimestamp);
    }

    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
        public Answer createFromParcel(Parcel source) {
            return new Answer(source);
        }

        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };
}