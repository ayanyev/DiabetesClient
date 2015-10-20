package org.coursera.capstone.t1dteensclient.client;

import org.coursera.capstone.t1dteensclient.entities.*;
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
	
	@GET("/users/{id}")
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

	// QUESTIONS

	@GET("/questions")
	public List<Question> getQuestionsList(@Body Long timeStampInMIllis);

	//OPTIONS

	@GET("/options")
	public List<Option> getOptionsList(@Body Long timeStampInMIllis);

	//RELATIONS

	@GET("/relations")
	List<Relation> getRelationsList(Long timeStampInMillis);

	@POST("/relations")
	public Relation addRelation(@Body Relation relation);

	@POST("/relations/bulk")
	public List<Relation> bulkAddRelations(@Body List<Relation> relations);

}
