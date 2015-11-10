package org.coursera.capstone.t1dteensclient.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.coursera.capstone.t1dteensclient.entities.enums.AnswerType;
import org.coursera.capstone.t1dteensclient.entities.enums.CheckInStatus;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.coursera.capstone.t1dteensclient.provider.ServiceContract.*;

public class CheckIn implements EntityInterface, Parcelable {

    @JsonIgnore
    private Long _id;
    @JsonProperty("id")
    private Long checkin_id = null;
    private Long user_id;
    private CheckInStatus status;
    private List<Answer> answers = new ArrayList<>();
    private Date timestamp;

    public CheckIn() {
        this._id = null;
        this.checkin_id = null;
    }

    public CheckIn(long user_id) {
        this.user_id = user_id;
        this.checkin_id = null;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Long getCheckin_id() {
        return checkin_id;
    }

    public void setCheckin_id(Long checkin_id) {
        this.checkin_id = checkin_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public CheckInStatus getStatus() {
        return status;
    }

    public void setStatus(CheckInStatus status) {
        this.status = status;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    // sets new empty answers from currently available questions
    public void setNewAnswers(final Context context){

        try {
            this.answers = (new AsyncTask<Void, Void, List<Answer>>() {
                @Override
                protected List<Answer> doInBackground(Void...params) {

                    List<Answer> answers = new ArrayList<>();

                    // gets current questions
                    Cursor questions = context.getContentResolver()
                            .query(QUESTIONS_DATA_URI, null, null, null, QUESTIONS_COLUMN_ORDER);

                    if (questions != null && questions.moveToFirst()){

                        while (!questions.isAfterLast()){

                            Question question = (new Question()).fromCursorToPOJO(questions, -1, null);

                            // gets options for the current question
                            if (question.getAnswerType() == AnswerType.OPTIONS){
                                Cursor options = context.getContentResolver()
                                        .query(OPTIONS_DATA_URI,
                                                null,
                                                OPTIONS_COLUMN_QUESTION_ID + " = ?",
                                                new String[]{String.valueOf(question.getQuestion_id())},
                                                null);
                                if ((options != null) && (options.moveToFirst())) {
                                    while (!options.isAfterLast()) {
                                        question.getOptions().add(new Option().fromCursorToPOJO(options));
                                        options.moveToNext();
                                    }
                                }
                            }
                            answers.add(new Answer(question));
                            questions.moveToNext();
                        }
                    }
                    return answers;
                }
            }.execute()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ContentValues toContentValues(){

        ContentValues cv = new ContentValues();
        if (_id != null) cv.put(CHECKINS_COLUMN_ID, _id);
        cv.put(CHECKINS_COLUMN_CHECKING_ID, checkin_id);
        cv.put(CHECKINS_COLUMN_USER_ID, user_id);
        cv.put(CHECKINS_COLUMN_STATUS, String.valueOf(status));
        cv.put(CHECKINS_COLUMN_TIMESTAMP,
                new Date(System.currentTimeMillis()).getTime());

        return cv;
    }

    @Override
    public Uri saveIt(final Context context) {

        try {
            return (new AsyncTask<ContentValues, Void, Uri>() {
                @Override
                protected Uri doInBackground(ContentValues... params) {

                    // inserts new CHECKIN to local db
                    Uri uri = context.getContentResolver()
                            .insert(CHECKINS_DATA_URI, params[0]);

                    // inserts answers records to table ANSWERS
                    if (answers.size() > 0) {

                        ContentValues cvs[] = new ContentValues[answers.size()];
                        int i = 0;
                        for (Answer answer : answers) {
                            answer.setCheckInId(Long.valueOf(uri.getLastPathSegment()));
                            cvs[i] = answer.toContentValues();
                            i++;
                        }
                        context.getContentResolver().bulkInsert(ANSWERS_DATA_URI, cvs);
                    }
                    return uri;
                }

                @Override
                protected void onPostExecute(Uri uri) {
                    super.onPostExecute(uri);

                    // notify Content Observer that data changed to start Sync Adapter
                    context.getContentResolver().notifyChange(uri, null);
                }
            }.execute(this.toContentValues())).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CheckIn fromCursorToPOJO(Cursor checkins, int position, Cursor answers) {

        // fills CHECKIN fields from cursor

        Long id;

        if (position != -1) checkins.moveToPosition(position);

        this.set_id(checkins.getLong(checkins
                .getColumnIndex(CHECKINS_COLUMN_ID)));
        id = checkins.getLong(checkins
                .getColumnIndex(CHECKINS_COLUMN_CHECKING_ID));
        this.setCheckin_id(id == 0 ? null : id);

        this.setUser_id(checkins.getLong(checkins
                .getColumnIndex(CHECKINS_COLUMN_USER_ID)));
        this.setStatus(CheckInStatus.valueOf(checkins.getString(checkins
                .getColumnIndex(CHECKINS_COLUMN_STATUS))));
        this.setTimestamp(new Timestamp(checkins.getLong(checkins
                .getColumnIndex(CHECKINS_COLUMN_TIMESTAMP))));

        // if answers cursor is not empty
        if ((answers != null) && (answers.moveToFirst())) {
            Answer answer = new Answer();
            while (!answers.isAfterLast()) {

                this.answers.add(answer.fromCursorToPOJO(answers));
                answers.moveToNext();
            }
        }

        return this;
    }

    @Override
    public <T> int updateIt(Context context) {
        return 0;
    }

    protected CheckIn(Parcel in) {

        _id = in.readByte() == 0x00 ? null : in.readLong();
        checkin_id = in.readByte() == 0x00 ? null : in.readLong();
        user_id = in.readByte() == 0x00 ? null : in.readLong();
        status = (CheckInStatus) in.readValue(CheckInStatus.class.getClassLoader());
        if (in.readByte() == 0x01) {
            answers = new ArrayList<Answer>();
            in.readList(answers, Answer.class.getClassLoader());
        } else {
            answers = null;
        }
        long tmpTimestamp = in.readLong();
        timestamp = tmpTimestamp != -1 ? new Date(tmpTimestamp) : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (_id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(_id);
        }
        if (checkin_id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(checkin_id);
        }
        if (user_id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(user_id);
        }
        dest.writeValue(status);
        if (answers == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(answers);
        }
        dest.writeLong(timestamp != null ? timestamp.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CheckIn> CREATOR = new Parcelable.Creator<CheckIn>() {
        @Override
        public CheckIn createFromParcel(Parcel in) {
            return new CheckIn(in);
        }

        @Override
        public CheckIn[] newArray(int size) {
            return new CheckIn[size];
        }
    };
}