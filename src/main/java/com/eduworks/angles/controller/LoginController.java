/*
 Copyright 2015-2016 Eduworks Corporation and other contributing parties.

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
*/
package com.eduworks.angles.controller;

import org.cassproject.ebac.identity.EcIdentity;
import org.cassproject.ebac.identity.EcIdentityManager;
import org.cassproject.ebac.identity.remote.EcRemoteIdentityManager;
import org.cassproject.ebac.identity.remote.OAuth2FileBasedRemoteIdentityManager;
import org.cassproject.ebac.identity.remote.RemoteIdentityManagerInterface;
import org.cassproject.ebac.repository.EcRepository;
import org.stjs.javascript.Array;
import org.stjs.javascript.functions.Callback0;
import org.stjs.javascript.functions.Callback1;
import org.stjs.javascript.functions.Function0;

/**
 * Manages the current user's logged in state and interfaces with the server to
 * sign in/out and create users
 *
 * @author devlin.junker@eduworks.com
 * @module angles.mobile
 * @class LoginController
 * @constructor
 */
public class LoginController {
	public RemoteIdentityManagerInterface loginServer = null;
	public IdentityController identity = null;

	public boolean refreshLoggedIn = false;
	public boolean loggedIn = false;

	StorageController storageSystem;

	/**
	 * On startup, check if the last time the user was on the page, whether or not they were signed in
	 */
	public LoginController(StorageController storage) {
		this.storageSystem = storage;

		refreshLoggedIn = (String) storageSystem.getStoredValue("cass.login") == "true" ? true : false;
	}

	/**
	 * Setter for the boolean flag of whether or not a user is loged in
	 *
	 * @param {boolean} val
	 *                  true if signed in, false if logged out
	 * @method setLoggedIn
	 * @static
	 */
	public void setLoggedIn(boolean val) {
		loggedIn = val;

		if (storageSystem != null)
			storageSystem.setStoredValue("cass.login", val);
	}

	/**
	 * Getter for boolean flag of whether or not user is logged in
	 *
	 * @return {boolean}
	 * true if signed in, false if logged out
	 * @method getLoggedin
	 * @static
	 */
	public boolean getLoggedIn() {
		return loggedIn;
	}

	/**
	 * If the last time the user was using the application, they were signed in this
	 * returns true (used to remind them to sign in again once they return)
	 *
	 * @return {boolean}
	 * true if previously signed in, false if not signed in last time, or user is here for
	 * the first time from this computer
	 * @method getPreviouslyLoggedIn
	 * @static
	 */
	public boolean getPreviouslyLoggedIn() {
		return refreshLoggedIn;
	}

	public void hello(final String network, final Callback0 success, final Callback1<String> failure) {
		final IdentityController identityManager = identity;

		final LoginController me = this;
		loginServer = new OAuth2FileBasedRemoteIdentityManager(new Callback0() {
			public void $invoke() {
				me.loginServer.setDefaultIdentityManagementServer(network);
				me.loginServer.startLogin(null, null);
				me.loginServer.fetch(new Callback1<Object>() {
					@Override
					public void $invoke(Object p1) {
						EcIdentityManager.readContacts();

						EcRepository.cache = new Object();
						me.setLoggedIn(true);

						if (EcIdentityManager.ids.$length() > 0) {
							identityManager.select(EcIdentityManager.ids.$get(0).ppk.toPem());
						}
						success.$invoke();

					}
				}, new Callback1<String>() {
					@Override
					public void $invoke(String p1) {
						failure.$invoke(p1);
					}
				});
			}
		});
	}

	/**
	 * Validates a username and password on the server and then parses the user's credentials and
	 * checks if they have an admin key. Also tells the identity manager to check for contacts in
	 * local storage after signed in.
	 *
	 * @param {String} username
	 *                 username of the user signing in
	 * @param {String} password
	 *                 password of the user signing in
	 * @param {String} success
	 *                 callback on successful login
	 * @param {String} failure
	 *                 callback on error during login
	 * @method login
	 */
	public void login(final String username, final String password, String server, final Callback0 success, final Callback1<String> failure) {
		final IdentityController identityManager = identity;

		final LoginController that = this;

		loginServer = new EcRemoteIdentityManager();
		loginServer.setDefaultIdentityManagementServer(server);
		loginServer.configureFromServer(new Callback1<Object>() {
			@Override
			public void $invoke(Object o) {
				that.loginServer.startLogin(username, password);
				that.loginServer.fetch(new Callback1<Object>() {
					@Override
					public void $invoke(Object p1) {
						EcIdentityManager.readContacts();

						EcRepository.cache = new Object();
						that.setLoggedIn(true);

						if (EcIdentityManager.ids.$length() > 0) {
							identityManager.select(EcIdentityManager.ids.$get(0).ppk.toPem());
						}
						success.$invoke();

					}
				}, new Callback1<String>() {
					@Override
					public void $invoke(String p1) {
						failure.$invoke(p1);
					}
				});
			}
		},failure);
	}

	/**
	 * Sets the flags so the user is logged out, wipes all sign in data so the user is no longer
	 * authenticated and is unidentified
	 *
	 * @method logout
	 */
	public void logout() {
		loginServer.clear();
		identity.selectedIdentity = null;
		EcRepository.cache = new Object();
		setLoggedIn(false);
		EcIdentityManager.ids = new Array<EcIdentity>();
		EcIdentityManager.clearContacts();
	}

	/**
	 * Creates a new user and saves the account details on the login server, then signs in
	 * to the new account on successful creation
	 *
	 * @param {String}            username
	 *                            username of the new account
	 * @param {String}            password
	 *                            password of the new account
	 * @param {Callback0}         success
	 *                            callback for successful creation and sign in
	 * @param {Callback1<String>} failure
	 *                            callback for error during creation
	 * @method create
	 */
	public void create(final String username, final String password, final String server, final Callback0 success, final Callback1<String> failure) {
		loginServer.startLogin(username, password);
		final LoginController me = this;
		loginServer.create(new Callback1<String>() {
			@Override
			public void $invoke(String p1) {
				me.login(username, password, server, success, failure);
			}
		}, new Callback1<String>() {
			@Override
			public void $invoke(String p1) {
				failure.$invoke(p1);
			}
		}, new Function0<String>() {
			@Override
			public String $invoke() {
				return "";
			}
		});
	}

	/**
	 * Saves the users credentials and contacts to the server
	 *
	 * @param {Callback0}         success
	 *                            callback for successful save
	 * @param {Callback1<String>} failure
	 *                            callback for error during save
	 * @method save
	 */
	public void save(final Callback0 success, final Callback1<String> failure) {
		loginServer.commit(new Callback1<String>() {
			@Override
			public void $invoke(String p1) {
				success.$invoke();
			}
		}, new Callback1<String>() {
			@Override
			public void $invoke(String p1) {
				failure.$invoke(p1);
			}
		}, new Function0<String>() {
			@Override
			public String $invoke() {
				return null;
			}
		});
	}

}
