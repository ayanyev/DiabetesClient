package org.coursera.capstone.t1dteensclient.entities;

import org.coursera.capstone.t1dteensclient.entities.enums.AnswerType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Question {

    private long id;
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
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
