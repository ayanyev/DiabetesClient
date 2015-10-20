package org.coursera.capstone.t1dteensclient.entities;

import java.util.Date;

public class Option {

    private long id;
    private String text;
    private Question questionId;
    private Date timestamp;

    public Option() {
    }

    public Option(String text, Question questionId) {
        this.text = text;
        this.questionId = questionId;
    }

    public Question getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Question questionId) {
        this.questionId = questionId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
