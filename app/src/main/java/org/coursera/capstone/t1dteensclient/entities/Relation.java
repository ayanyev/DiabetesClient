package org.coursera.capstone.t1dteensclient.entities;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.coursera.capstone.t1dteensclient.entities.enums.RelationStatus;
import org.coursera.capstone.t1dteensclient.provider.ServiceContract;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static org.coursera.capstone.t1dteensclient.provider.ServiceContract.*;

public class Relation implements EntityInterface{

    @JsonIgnore
    private Long _id;
    @JsonProperty("id")
    private Long relId;
    private long subscriber;
    private long subscription;
    private RelationStatus status;
    private Date timestamp;

    public Relation() {
        this._id = null;
        this.status = RelationStatus.PENDING;
    }

    public Relation(long subscriber, long subscription) {
        this.subscriber = subscriber;
        this.subscription = subscription;
        this.status = RelationStatus.PENDING;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Long getRelId() {
        return relId;
    }

    public void setRelId(Long relId) {
        this.relId = relId;
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

/*        if (_id == 0)
            cv.putNull(RELATIONS_COLUMN_ID);
        else*/
            cv.put(RELATIONS_COLUMN_ID, _id);
        cv.put(RELATIONS_COLUMN_RELATION_ID, relId);
        cv.put(RELATIONS_COLUMN_SUBSCRIBER, subscriber);
        cv.put(RELATIONS_COLUMN_SUBSCRIPTION, subscription);
        cv.put(RELATIONS_COLUMN_STATUS, String.valueOf(status));
        cv.put(QUESTIONS_COLUMN_TIMESTAMP,
                (new Date(System.currentTimeMillis()).getTime()));

        return cv;
    }

    @Override
    public Uri saveIt(final Context context)  {

        try {
            return (new AsyncTask<ContentValues, Void, Uri>() {
                @Override
                protected Uri doInBackground(ContentValues... params) {

                    // inserts checking record to database table
                    return context.getContentResolver()
                            .insert(RELATIONS_DATA_URI, params[0]);
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

    @Override
    public int updateIt(final Context context) {

        final long localId = this.get_id();
        final String selection = RELATIONS_COLUMN_ID + " = ?";
        final String[] selectionArgs = new String[] {String.valueOf(localId)};

        try {
            return (new AsyncTask<ContentValues, Void, Integer>() {
                @Override
                protected Integer doInBackground(ContentValues... params) {

                    // inserts checking record to database table
                    return context.getContentResolver()
                            .update(RELATIONS_DATA_URI,
                                    params[0],
                                    selection,
                                    selectionArgs);
                }

                @Override
                protected void onPostExecute(Integer count) {
                    super.onPostExecute(count);

                    // notify Content Observer that data changed to start Sync Adapter
                    if (count > 0)
                        context.getContentResolver().notifyChange(
                                ContentUris.withAppendedId(RELATIONS_DATA_URI, localId), null);
                }
            }.execute(this.toContentValues())).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Relation fromCursorToPOJO(Cursor cursor, int position) {

        // fills RELATION fields from cursor

        Long id;

        if (position != -1) cursor.moveToPosition(position);

        this.set_id(cursor.getLong(cursor
                .getColumnIndex(RELATIONS_COLUMN_ID)));
        id = cursor.getLong(cursor
                .getColumnIndex(RELATIONS_COLUMN_RELATION_ID));
        this.setRelId((id == 0) ? null : id);
        this.setSubscriber(cursor.getLong(cursor
                .getColumnIndex(RELATIONS_COLUMN_SUBSCRIBER)));
        this.setSubscription(cursor.getLong(cursor
                .getColumnIndex(RELATIONS_COLUMN_SUBSCRIPTION)));
        this.setStatus(RelationStatus.valueOf(cursor.getString(cursor
                .getColumnIndex(RELATIONS_COLUMN_STATUS))));
        this.setTimestamp(new Timestamp(cursor.getLong(cursor
                .getColumnIndex(CHECKINS_COLUMN_TIMESTAMP))));

        return this;
    }
}
