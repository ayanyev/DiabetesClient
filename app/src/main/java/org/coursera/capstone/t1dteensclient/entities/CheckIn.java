package org.coursera.capstone.t1dteensclient.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import org.coursera.capstone.t1dteensclient.entities.enums.CheckInStatus;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckIn implements EntityInterface{

    private Long checkin_id = null;
    private Long user_id;
    private CheckInStatus status;
    private List<Answer> answers = new ArrayList<>();
    private Date timestamp;

    public CheckIn() {
        this.checkin_id = null;
    }

    public CheckIn(long user_id) {
        this.user_id = user_id;
        this.checkin_id = null;
    }

    public Long getId() {
        return checkin_id;
    }

    public void setId(Long id) {
        this.checkin_id = id;
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

    @Override
    public ContentValues toContentValues(){

        ContentValues cv = new ContentValues();
        cv.put(ServiceContract.CHECKINS_COLUMN_CHECKING_ID, checkin_id);
        cv.put(ServiceContract.CHECKINS_COLUMN_USER_ID, user_id);
        cv.put(ServiceContract.CHECKINS_COLUMN_STATUS, String.valueOf(status));

        Date ct = new Date(System.currentTimeMillis());
        Log.d("Chekin - timestamp : ", ct.toString());
        cv.put(ServiceContract.CHECKINS_COLUMN_TIMESTAMP, ct.getTime());

        return cv;
    }

    @Override
    public Uri saveIt(Context context) {

        // inserts checking record to table CHECKINS
        Uri uri = context.getContentResolver()
                .insert(ServiceContract.CHECKINS_DATA_URI, this.toContentValues());

        // inserts answers records to table ANSWERS
        if (answers.size() > 0) {

            ContentValues cvs[] = new ContentValues[answers.size()];
            int i = 0;
            for (Answer answer : answers) {
                answer.setCheckInId(Long.valueOf(uri.getLastPathSegment()));
                cvs[i] = answer.toContentValues();
                i++;
            }
            context.getContentResolver().bulkInsert(ServiceContract.ANSWERS_DATA_URI, cvs);
        }
        // notify Content Observer that data changed
        context.getContentResolver().notifyChange(uri, null);
        return uri;
    }

    public CheckIn fromCursorToPOJO(Cursor checkins, int position, Cursor answers) {

        // fills CHECKIN fields from cursor

        Long id;

        if (position != -1) checkins.moveToPosition(position);

        id = checkins.getLong(checkins
                .getColumnIndex(ServiceContract.CHECKINS_COLUMN_CHECKING_ID));
        this.setId(id == 0 ? null : id);

        this.setUser_id(checkins.getLong(checkins
                .getColumnIndex(ServiceContract.CHECKINS_COLUMN_USER_ID)));
        this.setStatus(CheckInStatus.valueOf(checkins.getString(checkins
                .getColumnIndex(ServiceContract.CHECKINS_COLUMN_STATUS))));
        this.setTimestamp(new Timestamp(checkins.getLong(checkins
                .getColumnIndex(ServiceContract.CHECKINS_COLUMN_TIMESTAMP))));

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
}
