/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.coursera.capstone.t1dteensclient.client;


import org.coursera.capstone.t1dteensclient.entities.User;
import java.util.List;

public class RequestResult {

	public enum Message {
		OK, ADDED, UPDATED, CONFLICT, DELETED,
		SERVER_ERROR, USER_ACTIVE, USER_NOT_FOUND,
		WRONG_PASSWORD, FAILED_TO_CONNECT_TO_SERVER
	}

	private Message message;
	private User user;
	private List<User> users;

	public RequestResult() {
	}

	public RequestResult(Message message) {
		this.message = message;
	}

	public RequestResult(Message message, User user) {
		super();
		this.message = message;
		this.user = user;
	}

	public RequestResult(Message message, List<User> users) {
		super();
		this.message = message;
		this.users = users;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
