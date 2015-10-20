package org.coursera.capstone.t1dteensclient.entities;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;

import java.util.Date;

public class Option implements EntityInterface {

    private long optionId;
    private String text;
    private int questionId;
    private Date timestamp;
    private byte weight;

    public Option() {
    }

    public Option(String text, int questionId) {
        this.text = text;
        this.questionId = questionId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public long getId() {
        return optionId;
    }

    public void setId(long id) {
        this.optionId = id;
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

        cv.put(ServiceContract.OPTIONS_COLUMN_OPTION_ID, optionId);
        cv.put(ServiceContract.OPTIONS_COLUMN_QUESTION_ID, questionId);
        cv.put(ServiceContract.OPTIONS_COLUMN_TEXT, text);
        cv.put(ServiceContract.OPTIONS_COLUMN_WEIGHT, weight);
        cv.put(ServiceContract.QUESTIONS_COLUMN_TIMESTAMP,
                (new Date(System.currentTimeMillis()).getTime()));

        return cv;
    }

    @Override
    public Uri saveIt(Context context) {
        return null;
    }
}
