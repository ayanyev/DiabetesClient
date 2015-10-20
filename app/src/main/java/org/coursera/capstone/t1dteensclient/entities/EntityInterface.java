package org.coursera.capstone.t1dteensclient.entities;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

public interface EntityInterface {


    public ContentValues toContentValues();
    public Uri saveIt(Context context);
//    public <T> T fromCursorToPOJO(Cursor cursor, Class<T> type);
}
