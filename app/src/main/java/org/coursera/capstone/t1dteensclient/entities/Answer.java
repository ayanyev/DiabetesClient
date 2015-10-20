package org.coursera.capstone.t1dteensclient.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;

import java.sql.Timestamp;
import java.util.Date;

public class Answer implements EntityInterface{

    private Long answer_id = null;
    private Integer questionId;
    @JsonIgnore
    private Long checkInId;
    private String text;
    private Date timestamp;

    public Answer() {
    }

    public Answer(int questionId) {
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

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
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
        cv.putNull(ServiceContract.ANSWERS_COLUMN_ANSWER_ID);
        cv.put(ServiceContract.ANSWERS_COLUMN_QUESTION_ID, questionId);
        cv.put(ServiceContract.ANSWERS_COLUMN_CHECKIN_ID, checkInId);
        cv.put(ServiceContract.ANSWERS_COLUMN_TEXT, text);

        Date ct = new Date(System.currentTimeMillis());
        Log.d("Answer - timestamp : ", ct.toString());
        cv.put(ServiceContract.CHECKINS_COLUMN_TIMESTAMP, ct.getTime());
        return cv;
    }

    @Override
    public Uri saveIt(Context context) {

        // inserts checking record to table ANSWERS
        Uri uri = context.getContentResolver()
                .insert(ServiceContract.ANSWERS_DATA_URI, this.toContentValues());
        return null;
    }

    public Answer fromCursorToPOJO (Cursor cursor){

        Long id;

        id = cursor.getLong(cursor
                .getColumnIndex(ServiceContract.ANSWERS_COLUMN_ANSWER_ID));
        this.setAnswer_id(id == 0 ? null : id);

        this.setQuestionId(cursor.getInt(cursor
                .getColumnIndex(ServiceContract.ANSWERS_COLUMN_QUESTION_ID)));

        id = cursor.getLong(cursor
                .getColumnIndex(ServiceContract.ANSWERS_COLUMN_CHECKIN_ID));
        this.setCheckInId(id == 0 ? null : id);

        this.setText(cursor.getString(cursor
                .getColumnIndex(ServiceContract.ANSWERS_COLUMN_TEXT)));
        this.setTimestamp(new Timestamp(cursor.getLong(cursor
                .getColumnIndex(ServiceContract.ANSWERS_COLUMN_TIMESTAMP))));

        return this;
    }
}
