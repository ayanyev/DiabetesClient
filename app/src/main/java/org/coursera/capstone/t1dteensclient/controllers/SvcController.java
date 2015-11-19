package org.coursera.capstone.t1dteensclient.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.coursera.capstone.t1dteensclient.Constants;
import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.client.RequestResult;
import org.coursera.capstone.t1dteensclient.client.SecuredRestBuilder;
import org.coursera.capstone.t1dteensclient.client.SvcApi;
import org.coursera.capstone.t1dteensclient.client.HttpsClient;
import org.coursera.capstone.t1dteensclient.entities.*;
import org.coursera.capstone.t1dteensclient.entities.enums.UserType;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.converter.JacksonConverter;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SvcController {

    private Context mContext;
    private SvcApi mServiceApi;
    private SharedPreferences prefs;

    public SvcController(Context mContext) {

        this.mContext = mContext;
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        String username = Utils.getCurrentUserName(mContext);
        String password = Utils.getCurrentPassword(mContext);

        JacksonConverter converter = new JacksonConverter(new ObjectMapper());

        mServiceApi = new SecuredRestBuilder()
                .setLoginEndpoint(Constants.SERVER_URL + Constants.TOKEN_PATH)
                .setUsername(username)
                .setPassword(password)
                .setClientId(Constants.CLIENT_ID)
                // TODO change for safe Http Client
                .setClient(new OkClient(HttpsClient.getUnsafeOkHttpClient()))
                .setEndpoint(Constants.SERVER_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(converter)
                .build()
                .create(SvcApi.class);
    }

    public RequestResult register(final User user) throws RetrofitError{

        return mServiceApi.addUser(user);
    }

    public User getUserById(long id){
        try {
            return (new AsyncTask<Long, Void, User>() {
                @Override
                protected User doInBackground(Long... params) {
                    return mServiceApi.getUserById(params[0]);
                }
            }.execute(id)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public RequestResult getUserByCredentials(User user){
        try {
            return (new AsyncTask<User, Void, RequestResult>() {
                @Override
                protected RequestResult doInBackground(User...params) {

                    try {
                        return mServiceApi.getUserByCredentials(params[0]);
                    } catch (RetrofitError retrofitError) {
                        return new RequestResult(RequestResult.Message.FAILED_TO_CONNECT_TO_SERVER);
                    }
                }
            }.execute(user)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CheckIn addCheckin(CheckIn checkin){

        return mServiceApi.addCheckin(checkin);
    }

    public List<CheckIn> bulkAddCheckins(List<CheckIn> checkin){

        return mServiceApi.bulkAddCheckins(checkin);
    }

    public List<CheckIn> getCheckins(long userId, long timeStampInMillis){

        return mServiceApi.getCheckins(userId, timeStampInMillis);
    }

    public List<Question> getUpdatedQuestionsList(Long timeStampInMIllis){

        return mServiceApi.getQuestionsList(timeStampInMIllis);
    }

    public List<Option> getUpdatedOptionsList(Long timeStampInMIllis){

        return mServiceApi.getOptionsList(timeStampInMIllis);
    }

    public List<Relation> getUpdatedRelationsList(Long timeOfLastSync) {

        return mServiceApi.getRelationsList(timeOfLastSync, Utils.getCurrentUserId(mContext));
    }

    public Relation addRelation(Relation relation){

        return mServiceApi.addRelation(relation);
    }

    public List<Relation> bulkAddRelations(List<Relation> relations){

        return mServiceApi.bulkAddRelations(relations);
    }

    public RequestResult getAllUsers(UserType userType) {

        return mServiceApi.getUserList(userType);
    }
}
