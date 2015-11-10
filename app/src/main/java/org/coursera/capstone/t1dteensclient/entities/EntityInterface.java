package org.coursera.capstone.t1dteensclient.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.concurrent.ExecutionException;

public interface EntityInterface {


    public ContentValues toContentValues();
    public Uri saveIt(Context context) throws ExecutionException, InterruptedException;
    public <T> int updateIt(Context context);
//    public <T> T fromCursorToPOJO(Cursor cursor, Class<T> type);
}
