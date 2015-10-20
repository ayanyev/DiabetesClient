package org.coursera.capstone.t1dteensclient.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import org.coursera.capstone.t1dteensclient.entities.enums.RelationStatus;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;

import java.sql.Timestamp;
import java.util.Date;

public class Relation implements EntityInterface{

    private long relId;
    private long subscriber;
    private long subscription;
    private RelationStatus status;
    private Date timestamp;

    public Relation() {
    }

    public Relation(long subscriber, long subscription) {
        this.subscriber = subscriber;
        this.subscription = subscription;
        this.status = RelationStatus.PENDING;
    }

    public long getSubscription() {
        return subscription;
    }

    public void setSubscription(long subscription) {
        this.subscription = subscription;
    }

    public long getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(long subscriber) {
        this.subscriber = subscriber;
    }

    public long getId() {
        return relId;
    }

    public void setId(long relId) {
        this.relId = relId;
    }

    public RelationStatus getStatus() {
        return status;
    }

    public void setStatus(RelationStatus status) {
        this.status = status;
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

        cv.put(ServiceContract.RELATIONS_COLUMN_RELATION_ID, relId);
        cv.put(ServiceContract.RELATIONS_COLUMN_SUBSCRIBER, subscriber);
        cv.put(ServiceContract.RELATIONS_COLUMN_SUBSCRIPTION, subscription);
        cv.put(ServiceContract.RELATIONS_COLUMN_STATUS, String.valueOf(status));
        cv.put(ServiceContract.QUESTIONS_COLUMN_TIMESTAMP,
                (new Date(System.currentTimeMillis()).getTime()));

        return cv;
    }

    @Override
    public Uri saveIt(Context context) {
        return null;
    }

    public Relation fromCursorToPOJO(Cursor cursor, int position) {

        // fills RELATION fields from cursor

        Long id;

        cursor.moveToPosition(position);

        id = cursor.getLong(cursor
                .getColumnIndex(ServiceContract.RELATIONS_COLUMN_RELATION_ID));
        this.setId((id == 0) ? null : id);
        this.setSubscriber(cursor.getLong(cursor
                .getColumnIndex(ServiceContract.RELATIONS_COLUMN_SUBSCRIBER)));
        this.setSubscription(cursor.getLong(cursor
                .getColumnIndex(ServiceContract.RELATIONS_COLUMN_SUBSCRIPTION)));
        this.setStatus(RelationStatus.valueOf(cursor.getString(cursor
                .getColumnIndex(ServiceContract.RELATIONS_COLUMN_STATUS))));
        this.setTimestamp(new Timestamp(cursor.getLong(cursor
                .getColumnIndex(ServiceContract.CHECKINS_COLUMN_TIMESTAMP))));

        return this;
    }
}
