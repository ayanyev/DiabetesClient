package org.coursera.capstone.t1dteensclient.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import static org.coursera.capstone.t1dteensclient.provider.ServiceContract.*;

public class ServiceContentProvider extends ContentProvider {



    private DataBaseHelper mDbHelper;
    private static final String AUTHORITY =
                                ServiceContract.AUTHORITY;

    private Uri VIDEO_DATA_URI;
    public UriMatcher mMatcher;

    @Override
    public boolean onCreate() {

        mDbHelper = new DataBaseHelper(getContext());
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mMatcher.addURI(AUTHORITY, CHECKINS_TABLE_NAME, MATCH_ALL);
        mMatcher.addURI(AUTHORITY, CHECKINS_TABLE_NAME + "/#", MATCH_ONE);
        mMatcher.addURI(AUTHORITY, ANSWERS_TABLE_NAME, MATCH_ALL);
        mMatcher.addURI(AUTHORITY, ANSWERS_TABLE_NAME + "/#", MATCH_ONE);
        mMatcher.addURI(AUTHORITY, QUESTIONS_TABLE_NAME, MATCH_ALL);
        mMatcher.addURI(AUTHORITY, QUESTIONS_TABLE_NAME + "/#", MATCH_ONE);
        mMatcher.addURI(AUTHORITY, OPTIONS_TABLE_NAME, MATCH_ALL);
        mMatcher.addURI(AUTHORITY, OPTIONS_TABLE_NAME + "/#", MATCH_ONE);
        mMatcher.addURI(AUTHORITY, RELATIONS_TABLE_NAME, MATCH_ALL);
        mMatcher.addURI(AUTHORITY, RELATIONS_TABLE_NAME + "/distinct", MATCH_DISTINCT);
        mMatcher.addURI(AUTHORITY, RELATIONS_TABLE_NAME + "/#", MATCH_ONE);

        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String table;
        Boolean distinct = false;

        switch (mMatcher.match(uri)) {

            case MATCH_ALL:
                table = uri.getPathSegments().get(0);
                break;
            case MATCH_DISTINCT:
                table = uri.getPathSegments().get(0);
                distinct = true;
                break;
            case MATCH_ONE:
                table = uri.getPathSegments().get(0);
                selection = "_id = ?";
                selectionArgs[0] = uri.getPathSegments().get(1);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        return db.query(distinct, table, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String mTable;

        switch (mMatcher.match(uri)) {

            case MATCH_ALL:
                mTable = uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        final long newRow = db.insert(mTable, null, values);
        if (newRow > 0) {
            Uri newUri = ContentUris.withAppendedId(uri, newRow);
            return newUri;
        } else {
            throw new SQLException("Fail to add a new record into "
                    + uri);}

    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String mTable = "";

        switch (mMatcher.match(uri)) {

            case MATCH_ALL:
                mTable = uri.getLastPathSegment();
                break;
            case MATCH_ONE:
                mTable = uri.getPathSegments().get(0);
                selection = "_id = ?";
                selectionArgs[0] = uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        final int numRowsUpdated = db.update(mTable, values, selection, selectionArgs);

        return numRowsUpdated;

    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String mTable;

        switch (mMatcher.match(uri)) {

            case MATCH_ALL:
                mTable = uri.getLastPathSegment();
                break;
            case MATCH_ONE:
                mTable = "";
                selection = "_id = ?";
                selectionArgs[0] = uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        final int numRowsDeleted = db.delete(mTable, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return numRowsDeleted;
    }


}
