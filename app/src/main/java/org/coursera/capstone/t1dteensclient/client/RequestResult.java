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

public class RequestResult {

	public enum UserState {
		ADDED, UPDATED, CONFLICT, DELETED, SERVER_ERROR
	}

	private UserState state;
	private User user;

	public RequestResult() {
	}

	public RequestResult(UserState state) {
		this.state = state;
	}

	public RequestResult(UserState state, User user) {
		super();
		this.state = state;
		this.user = user;
	}

	public UserState getState() {
		return state;
	}

	public void setState(UserState state) {
		this.state = state;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
