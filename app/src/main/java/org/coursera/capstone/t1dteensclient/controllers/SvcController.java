package org.coursera.capstone.t1dteensclient.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.coursera.capstone.t1dteensclient.Constants;
import org.coursera.capstone.t1dteensclient.client.RequestResult;
import org.coursera.capstone.t1dteensclient.client.SecuredRestBuilder;
import org.coursera.capstone.t1dteensclient.client.T1DteensSvcApi;
import org.coursera.capstone.t1dteensclient.client.UnsafeHttpsClient;
import org.coursera.capstone.t1dteensclient.entities.CheckIn;
import org.coursera.capstone.t1dteensclient.entities.User;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.JacksonConverter;

import java.util.List;

public class SvcController {

    private Context mContext;
    private T1DteensSvcApi mServiceApi;
    private SharedPreferences prefs;

    public SvcController(Context mContext) {

        this.mContext = mContext;
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        String username = prefs.getString("username", "guest");
        String password = prefs.getString("password", "guest");

        JacksonConverter converter = new JacksonConverter(new ObjectMapper());

        mServiceApi = new SecuredRestBuilder()
                .setLoginEndpoint(Constants.SERVER_URL + Constants.TOKEN_PATH)
                .setUsername(username)
                .setPassword(password)
                .setClientId(Constants.CLIENT_ID)
                .setClient(new OkClient(UnsafeHttpsClient.getUnsafeOkHttpClient()))
                .setEndpoint(Constants.SERVER_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(converter)
                .build()
                .create(T1DteensSvcApi.class);
    }

    public RequestResult register(final User user){

        return mServiceApi.addUser(user);
    }

    public CheckIn addCheckin(CheckIn checkin){

        return mServiceApi.addCheckin(checkin);
    }

    public List<CheckIn> bulkAddCheckins(List<CheckIn> checkin){

        return mServiceApi.bulkAddCheckins(checkin);
    }
}
