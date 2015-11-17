package org.coursera.capstone.t1dteensclient.sync;

import android.accounts.Account;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import org.coursera.capstone.t1dteensclient.Constants;
import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.client.RequestResult;
import org.coursera.capstone.t1dteensclient.controllers.SvcController;
import org.coursera.capstone.t1dteensclient.entities.CheckIn;
import org.coursera.capstone.t1dteensclient.entities.Option;
import org.coursera.capstone.t1dteensclient.entities.Question;
import org.coursera.capstone.t1dteensclient.entities.Relation;
import org.coursera.capstone.t1dteensclient.entities.User;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;

import static org.coursera.capstone.t1dteensclient.provider.ServiceContract.*;

public class SyncAdapter extends AbstractThreadedSyncAdapter {


    ContentResolver mContentResolver;
    SvcController mController;
    SharedPreferences prefs;
    UriMatcher mMatcher;
    Uri mUri;
    Context mContext;

    private final String TAG = getClass().getSimpleName();

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        init(context);
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        init(context);
    }

    private void init(Context context) {

        mContentResolver = context.getContentResolver();
        mController = new SvcController(context.getApplicationContext());
        mContext = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mMatcher.addURI(AUTHORITY, CHECKINS_TABLE_NAME, MATCH_ALL_CHECKINS);
        mMatcher.addURI(AUTHORITY, CHECKINS_TABLE_NAME + "/#", MATCH_ONE_CHECKIN);
        mMatcher.addURI(AUTHORITY, QUESTIONS_TABLE_NAME, MATCH_ALL_QUESTIONS);
        mMatcher.addURI(AUTHORITY, RELATIONS_TABLE_NAME, MATCH_ALL_REATIONS);
        mMatcher.addURI(AUTHORITY, RELATIONS_TABLE_NAME + "/#", MATCH_ONE_RELATION);
        mMatcher.addURI(AUTHORITY, USER_DETAILS, MATCH_ONE_USER);
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              SyncResult syncResult) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mUri = Uri.parse(extras.getString("uri"));

        switch (mMatcher.match(mUri)) {

            case MATCH_ALL_CHECKINS:
                syncAllCheckins(provider);
                break;
            case MATCH_ONE_CHECKIN:
                syncOneCheckin(provider);
                break;
            case MATCH_ALL_QUESTIONS:
                syncQuestions(provider);
                break;
            case MATCH_ALL_REATIONS:
                syncAllRelations(provider);
                break;
            case MATCH_ONE_RELATION:
                syncOneRelation(provider);
                break;
            case MATCH_ONE_USER:
                syncUserDetails(mContext);
            default:
                break;
        }
    }

    private void syncUserDetails(Context context) {

        User currentUser = Utils.createCurrentUser(context);
        try {
            RequestResult result = mController.register(currentUser);
            if (result.getMessage() == RequestResult.Message.UPDATED)
                Log.d(TAG, "User details sync done");
        } catch (RetrofitError retrofitError) {
            retrofitError.printStackTrace();
            Log.d(TAG, "User details sync failed");
        }
    }

    private void syncAllRelations(ContentProviderClient provider) {

        Uri uri = RELATIONS_DATA_URI;
        ContentValues cv;
        String selection;
        String[] selectionArgs;
        Long timeOfLastSync = prefs.getLong(Constants.LAST_TIME_RELATIONS_SYNCED, 0);

        try {

            // gets list of Relations from local database
            selection = "timestamp > ?";
            selectionArgs = new String[]{String.valueOf(timeOfLastSync)};
            Cursor relationsFromLocalDB = provider.query(uri, null, selection, selectionArgs, null);

            // put relations from local DB to server if there is any
            if (relationsFromLocalDB != null) {
                // creates list of POJOs from cursor
                List<Relation> relations = new ArrayList<>();

                if (relationsFromLocalDB.moveToFirst()) {

                    while (!relationsFromLocalDB.isAfterLast()) {

                        relations.add((new Relation()).fromCursorToPOJO(relationsFromLocalDB,
                                relationsFromLocalDB.getPosition()));
                        relationsFromLocalDB.moveToNext();
                    }
                    relationsFromLocalDB.close();
                }
                // puts Relations from local database to server
                relations = mController.bulkAddRelations(relations);

/*                // updates relation_id
                for (Relation rel : relations) {

                    cv = rel.toContentValues();
                    selection = "timestamp = ?";
                    selectionArgs = new String[]{String.valueOf(rel.getTimestamp().getTime())};
                    provider.update(uri, cv, selection, selectionArgs);
                }*/
            }

            // gets list of Relations from server
            List<Relation> relationsFromServer = mController.getUpdatedRelationsList((long) 0);

            provider.delete(uri, null, null);

            // puts Relations from server to local database
            for (Relation relation : relationsFromServer) {

                cv = relation.toContentValues();
                selection = "relation_id = ?";
                selectionArgs = new String[]{String.valueOf(relation.getRelId())};

//                if (provider.update(uri, cv, selection, selectionArgs) == 0)
                    provider.insert(uri, cv);
            }

            // sets last time of sync if sync was successfull
            prefs.edit().putLong(Constants.LAST_TIME_RELATIONS_SYNCED, System.currentTimeMillis()).commit();

            Log.d(TAG, "All_Relations sync done");

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "All_Relations sync failed");
        }
    }

    private void syncOneRelation(ContentProviderClient provider) {

        Uri uri = RELATIONS_DATA_URI;
        ContentValues cv;
        String selection;
        String[] selectionArgs;
        Relation relation = null;

        try {

            // gets one item cursor
            selection = "_ID = ?";
            selectionArgs = new String[]{String.valueOf(this.mUri.getLastPathSegment())};
            Cursor cursor = provider.query(uri,
                                            null,
                                            selection,
                                            selectionArgs,
                                            null);

            if (cursor != null && cursor.moveToFirst()) {

                relation = mController.addRelation((new Relation()).fromCursorToPOJO(cursor,
                                                                                    cursor.getPosition()));
                cursor.close();
            }
/*
            // posts Relation from local database to server
            relation = mController.addRelation(relation);*/

            // updates relation_id
            cv = relation.toContentValues();

            selection = "timestamp = ?";
            selectionArgs = new String[]{String.valueOf(relation.getTimestamp().getTime())};
//            provider.update(uri, cv, selection, selectionArgs);
            provider.update(mUri, cv, selection, selectionArgs);

            Log.d(TAG, "One_Relation sync done");

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "One_Relation sync failed");
        }
    }

    private void syncQuestions(ContentProviderClient provider) {

        Uri uri;
        ContentValues cv;
        String selection;
        String[] selectionArgs;
        Long timeOfLastSync = prefs.getLong(Constants.LAST_TIME_QUESTIONS_SYNCED, 0);

        try {

            List<Question> questions = mController.getUpdatedQuestionsList(timeOfLastSync);
            for (Question question : questions) {

                uri = QUESTIONS_DATA_URI;
                cv = question.toContentValues();
                selection = "question_id = ?";
                selectionArgs = new String[]{String.valueOf(question.getQuestion_id())};

                if (provider.update(uri, cv, selection, selectionArgs) == 0)
                                        provider.insert(uri, cv);
            }

            List<Option> options = mController.getUpdatedOptionsList(timeOfLastSync);
            for (Option option : options) {

                uri = OPTIONS_DATA_URI;
                cv = option.toContentValues();
                selection = "option_id = ?";
                selectionArgs = new String[]{String.valueOf(option.getOptionId())};

                if (provider.update(uri, cv, selection, selectionArgs) == 0)
                    provider.insert(uri, cv);
            }

            prefs.edit().putLong(Constants.LAST_TIME_QUESTIONS_SYNCED, System.currentTimeMillis());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncOneCheckin(ContentProviderClient provider) {

        Uri uri;
        ContentValues cv;
        String selection;
        String[] selectionArgs;
        CheckIn checkin = null;

        uri = CHECKINS_DATA_URI;
        selection = "_ID = ?";
        selectionArgs = new String[]{this.mUri.getLastPathSegment()};

        try {
            // checkins not yet synced query
            Cursor cursor =
                    provider.query(uri,
                            null,
                            selection,
                            selectionArgs,
                            null);

            // if cursor not empty
            if (cursor != null && cursor.moveToFirst()) {
                // get ID of checkin in local db
                int checkin_id = cursor.getInt(cursor
                        .getColumnIndex(CHECKINS_COLUMN_ID));

                // query for answers which belong to checkin
                uri = ANSWERS_DATA_URI;
                selection = "checkin_id = ?";
                selectionArgs = new String[]{String.valueOf(checkin_id)};

                Cursor cursor2 =
                        provider.query(uri,
                                null,
                                selection,
                                selectionArgs,
                                null);

                if (cursor2 != null) {
                    checkin = (new CheckIn()).fromCursorToPOJO(cursor, cursor.getPosition(), cursor2);
                    cursor2.close();
                }
                cursor.close();
            }

            // result of remote request with checkin_id assigned
            checkin = mController.addCheckin(checkin);

            // updates checkin_id in local db
            uri = CHECKINS_DATA_URI;
            selection = "timestamp = ?";
            cv = checkin.toContentValues();
            selectionArgs = new String[]{String.valueOf(checkin.getTimestamp().getTime())};

            int result = provider.update(uri, cv, selection, selectionArgs);

        } catch (RemoteException e) {
            e.printStackTrace();
            Log.d(TAG, "One Checkin sync failed");
        }

        Log.d(TAG, "One Checkin sync done");
    }

    private void syncAllCheckins(ContentProviderClient provider) {

        Uri uri;
        ContentValues cv;
        String selection;
        String[] selectionArgs;
        Long timeOfLastSync = prefs.getLong(Constants.LAST_TIME_CHECKINS_SYNCED, 0);

        uri = CHECKINS_DATA_URI;
        selection = "timestamp > ?";
        selectionArgs = new String[]{String.valueOf(timeOfLastSync)};

        // list of checkins to be sent to server
        List<CheckIn> checkins = new ArrayList<>();

        try {
            // checkins not yet synced query
            Cursor cursor =
                    provider.query(uri,
                            null,
                            selection,
                            selectionArgs,
                            null);

            if (cursor != null && cursor.moveToFirst()) {

                 while (!cursor.isAfterLast()) {

                    // get ID of checkin in local db
                    int checkin_id = cursor.getInt(cursor
                            .getColumnIndex(CHECKINS_COLUMN_ID));

                    // query for answers which belong to checkin
                    uri = ANSWERS_DATA_URI;
                    selection = "checkin_id = ?";
                    selectionArgs = new String[]{String.valueOf(checkin_id)};

                    Cursor cursor2 =
                            provider.query(uri,
                                    null,
                                    selection,
                                    selectionArgs,
                                    null);

                    checkins.add((new CheckIn()).fromCursorToPOJO(cursor, cursor.getPosition(), cursor2));

                    cursor2.close();
                    cursor.moveToNext();
                }
                cursor.close();
            }

            // result of remote request with checkin_ids assigned
            checkins = mController.bulkAddCheckins(checkins);

            // updates checkin_id in local db
            uri = CHECKINS_DATA_URI;
            selection = "timestamp = ?";

            for (CheckIn ci : checkins){

                cv = ci.toContentValues();
                selectionArgs = new String[]{String.valueOf(ci.getTimestamp().getTime())};

                provider.update(uri, cv, selection, selectionArgs);
            }

            prefs.edit().putLong(Constants.LAST_TIME_CHECKINS_SYNCED, System.currentTimeMillis()).commit();

            Log.d(TAG, "All Checkins sync done");

        } catch (RemoteException e) {
            e.printStackTrace();
            Log.d(TAG, "All Checkins sync failed");
        }
    }
}
