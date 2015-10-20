package org.coursera.capstone.t1dteensclient.client;

import org.coursera.capstone.t1dteensclient.entities.CheckIn;
import org.coursera.capstone.t1dteensclient.entities.User;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

import java.util.Collection;
import java.util.List;

public interface T1DteensSvcApi {

	public static final String DATA_PARAMETER = "data";

	public static final String ID_PARAMETER = "id";

	public static final String TOKEN_PATH = "/oauth/token";

	// The path where we expect the VideoSvc to live
	public static final String VIDEO_SVC_PATH = "/video";

	// The path where we expect the VideoSvc to live
	public static final String VIDEO_DATA_PATH = VIDEO_SVC_PATH + "/{"+ T1DteensSvcApi.ID_PARAMETER+"}/data";

	// USERS
	
	@GET("/users")
	public Collection<User> getUserList();
	
	@GET("/users" + "/{id}")
	public User getUserById(@Path("id") long id);
	
	@POST("/users")
	public RequestResult addUser(@Body User user);

	@POST("/users/{id}/subscribeto/{id2}")
	public User subscribeToUser(@Path("id") long subscriberId, @Path("id2") long subscriptionId);

	@POST("/users/{id}/unsubscribefrom/{id2}")
	public User unsubscribeFromUser(@Path("id") long subscriberId, @Path("id2") long subscriptionId);

	@GET("/users/{id}/subscriptions")
	public User getSubscriptions(@Path("id") long userId);

	@GET("/users/{id}/subscribers")
	public User getSubscribers(@Path("id") long userId);

	// CHECKINS

	@POST("/checkins")
	public CheckIn addCheckin(@Body CheckIn ci);

	@POST("/checkins/bulk")
	public List<CheckIn> bulkAddCheckins(@Body List<CheckIn> cis);

	
/*	@POST(VIDEO_SVC_PATH+"/{id}/rating/{rating}")
	public AverageVideoRating rateVideo(@Path("id") long id, @Path("rating") int rating);
	
	@GET(VIDEO_SVC_PATH+"/{id}/rating")
	public AverageVideoRating getVideoRating(@Path("id") long id);
	
	@Multipart
	@POST(VIDEO_DATA_PATH)
	public RequestResult setVideoData(@Path(ID_PARAMETER) long id, @Part(DATA_PARAMETER) TypedFile videoData);
	
	*//**
	 * This method uses Retrofit's @Streaming annotation to indicate that the
	 * method is going to access a large stream of data (e.g., the mpeg video 
	 * data on the server). The client can access this stream of data by obtaining
	 * an InputStream from the Response as shown below:
	 * 
	 * T1DteensSvcApi client = ... // use retrofit to create the client
	 * Response response = client.getData(someVideoId);
	 * InputStream videoDataStream = response.getBody().in();
	 * 
	 * @param id
	 * @return
	 *//*
	@Streaming
    @GET(VIDEO_DATA_PATH)
    Response getVideoData(@Path(ID_PARAMETER) long id);*/
	
}
