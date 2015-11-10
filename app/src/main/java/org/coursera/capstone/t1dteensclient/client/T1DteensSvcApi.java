package org.coursera.capstone.t1dteensclient.client;

import org.coursera.capstone.t1dteensclient.entities.*;
import org.coursera.capstone.t1dteensclient.entities.enums.UserType;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

import java.util.Collection;
import java.util.List;

public interface T1DteensSvcApi {

	// USERS
	
	@GET("/users/type/{type}")
	public RequestResult getUserList(@Path("type") UserType userType);
	
	@GET("/users/{id}")
	public User getUserById(@Path("id") long id);

	@POST("/users/bycredentials")
	public RequestResult getUserByCredentials(@Body User user);
	
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

	@GET("/questions/{timestamp}")
	public List<Question> getQuestionsList(@Path("timestamp") Long timeStampInMillis);

	//OPTIONS

	@GET("/options/{timestamp}")
	public List<Option> getOptionsList(@Path("timestamp") Long timeStampInMillis);

	//RELATIONS

	@GET("/relations/{timestamp}/{userid}")
	List<Relation> getRelationsList(@Path("timestamp") Long timeOfLastSync,
									@Path("userid") Long userId);

	@POST("/relations")
	public Relation addRelation(@Body Relation relation);

	@POST("/relations/bulk")
	public List<Relation> bulkAddRelations(@Body List<Relation> relations);

}
