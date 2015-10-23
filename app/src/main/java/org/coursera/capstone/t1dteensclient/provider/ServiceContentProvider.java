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

public class ServiceContentProvider extends ContentProvider {



    private DataBaseHelper mDbHelper;
    private static final String AUTHORITY =
                                ServiceContract.AUTHORITY;

    private Uri VIDEO_DATA_URI;
    public UriMatcher mMatcher;
    private static final int MATCH_ALL = 1;
    private static final int MATCH_ONE = 2;
    private static final int MATCH_ALL_CHECKINS = 100;
    private static final int MATCH_ONE_CHECKIN = 200;
    private static final int MATCH_ALL_ANSWERS = 300;
    private static final int MATCH_ONE_ANSWER = 400;
    private static final int MATCH_ALL_QUESTIONS = 500;
    private static final int MATCH_ONE_QUESTION = 600;
    private static final int MATCH_ALL_OPTIONS = 700;
    private static final int MATCH_ONE_OPTION = 800;


    @Override
    public boolean onCreate() {

        mDbHelper = new DataBaseHelper(getContext());
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mMatcher.addURI(AUTHORITY, ServiceContract.CHECKINS_TABLE_NAME, MATCH_ALL);
        mMatcher.addURI(AUTHORITY, ServiceContract.CHECKINS_TABLE_NAME + "/#", MATCH_ONE);
        mMatcher.addURI(AUTHORITY, ServiceContract.ANSWERS_TABLE_NAME, MATCH_ALL);
        mMatcher.addURI(AUTHORITY, ServiceContract.ANSWERS_TABLE_NAME + "/#", MATCH_ONE);
        mMatcher.addURI(AUTHORITY, ServiceContract.QUESTIONS_TABLE_NAME, MATCH_ALL);
        mMatcher.addURI(AUTHORITY, ServiceContract.QUESTIONS_TABLE_NAME + "/#", MATCH_ONE);
        mMatcher.addURI(AUTHORITY, ServiceContract.OPTIONS_TABLE_NAME, MATCH_ALL);
        mMatcher.addURI(AUTHORITY, ServiceContract.OPTIONS_TABLE_NAME + "/#", MATCH_ONE);

        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String mTable;

        switch (mMatcher.match(uri)) {

            case MATCH_ALL:
                mTable = uri.getPathSegments().get(0);
                break;
            case MATCH_ONE:
                mTable = uri.getPathSegments().get(0);
                selection = "_id = ?";
                selectionArgs[0] = uri.getPathSegments().get(1);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        return db.query(mTable,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
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
        getContext().getContentResolver().notifyChange(uri, null);

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
