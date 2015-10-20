package org.coursera.capstone.t1dteensclient.entities;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import org.coursera.capstone.t1dteensclient.entities.enums.AnswerType;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Question implements  EntityInterface {

    private long question_id;
    private String text;
    private AnswerType answerType;
    private Boolean required;
    private Date timestamp;
    private List<Answer> answers = new ArrayList<>();
    private List<Option> options = new ArrayList<>();

    public Question() {}

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

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public long getId() {
        return question_id;
    }

    public void setId(long id) {
        this.question_id = id;
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

    @Override
    public ContentValues toContentValues() {

        ContentValues cv = new ContentValues();

        cv.put(ServiceContract.QUESTIONS_COLUMN_QUESTION_ID, question_id);
        cv.put(ServiceContract.QUESTIONS_COLUMN_REQUIRED, required);
        cv.put(ServiceContract.QUESTIONS_COLUMN_ANSWER_TYPE, String.valueOf(answerType));
        cv.put(ServiceContract.QUESTIONS_COLUMN_TEXT, text);
        cv.put(ServiceContract.QUESTIONS_COLUMN_TIMESTAMP,
                                (new Date(System.currentTimeMillis()).getTime()));

        return cv;
    }

    @Override
    public Uri saveIt(Context context) {
        return null;
    }
}
