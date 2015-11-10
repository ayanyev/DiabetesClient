package org.coursera.capstone.t1dteensclient.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.coursera.capstone.t1dteensclient.provider.ServiceContract;

import java.sql.Timestamp;
import java.util.Date;

import static org.coursera.capstone.t1dteensclient.provider.ServiceContract.*;

public class Option implements EntityInterface, Parcelable {

    @JsonIgnore
    private Long _id;
    @JsonProperty("id")
    private long optionId;
    private long questionId;
    private String text;
    private byte weight;
    private Date timestamp;

    public Option() {
        this._id = null;
    }

    public Option(String text, long questionId) {
        this.text = text;
        this.questionId = questionId;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public long getOptionId() {
        return optionId;
    }

    public void setOptionId(long optionId) {
        this.optionId = optionId;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public byte getWeight() {
        return weight;
    }

    public void setWeight(byte weight) {
        this.weight = weight;
    }

    @Override
    public ContentValues toContentValues() {

        ContentValues cv = new ContentValues();

        if (_id != null) cv.put(OPTIONS_COLUMN_ID, _id);
        cv.put(OPTIONS_COLUMN_OPTION_ID, optionId);
        cv.put(OPTIONS_COLUMN_QUESTION_ID, questionId);
        cv.put(OPTIONS_COLUMN_TEXT, text);
        cv.put(OPTIONS_COLUMN_WEIGHT, weight);
        cv.put(OPTIONS_COLUMN_TIMESTAMP,
                (new Date(System.currentTimeMillis()).getTime()));

        return cv;
    }

    @Override
    public Uri saveIt(Context context) {
        return null;
    }

    @Override
    public <T> int updateIt(Context context) {
        return 0;
    }

    public Option fromCursorToPOJO(Cursor cursor) {

        Long id;

        this.set_id(cursor.getLong(cursor
                .getColumnIndex(OPTIONS_COLUMN_ID)));
        id = cursor.getLong(cursor
                .getColumnIndex(OPTIONS_COLUMN_OPTION_ID));
        this.setOptionId((id == 0) ? null : id);
        this.setQuestionId(cursor.getLong(cursor
                .getColumnIndex(OPTIONS_COLUMN_ID)));
        this.setText(cursor.getString(cursor
                .getColumnIndex(OPTIONS_COLUMN_TEXT)));
        this.setWeight((byte) cursor.getInt(cursor
                .getColumnIndex(OPTIONS_COLUMN_WEIGHT)));
        this.setTimestamp(new Timestamp(cursor.getLong(cursor
                .getColumnIndex(QUESTIONS_COLUMN_TIMESTAMP))));

        return this;
    }

    protected Option(Parcel in) {
        _id = in.readLong();
        optionId = in.readLong();
        questionId = in.readLong();
        text = in.readString();
        weight = in.readByte();
        long tmpTimestamp = in.readLong();
        timestamp = tmpTimestamp != -1 ? new Date(tmpTimestamp) : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeLong(optionId);
        dest.writeLong(questionId);
        dest.writeString(text);
        dest.writeByte(weight);
        dest.writeLong(timestamp != null ? timestamp.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Option> CREATOR = new Parcelable.Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel in) {
            return new Option(in);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
        }
    };

    @Override
    public String toString() {
        return this.text;
    }
}
