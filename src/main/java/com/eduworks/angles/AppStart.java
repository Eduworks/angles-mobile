/*
 Copyright 2015-2016 Eduworks Corporation and other contributing parties.

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
*/
package com.eduworks.angles;

import com.eduworks.angles.controller.IdentityController;
import com.eduworks.angles.controller.ServerController;
import com.eduworks.angles.controller.StorageController;
import com.eduworks.angles.mobile.screen.WelcomeScreen;
import com.eduworks.ec.crypto.EcPpk;
import com.eduworks.ec.framework.view.manager.ScreenManager;
import com.eduworks.ec.framework.view.manager.ViewManager;
import com.eduworks.foundation.jquery.plugin.Foundation;
import org.cassproject.ebac.identity.EcIdentity;
import org.cassproject.ebac.identity.EcIdentityManager;
import org.stjs.javascript.Global;
import org.stjs.javascript.dom.Element;
import org.stjs.javascript.functions.Callback0;
import org.stjs.javascript.functions.Callback1;
import org.stjs.javascript.jquery.Event;
import org.stjs.javascript.jquery.EventHandler;
import org.stjs.javascript.jquery.GlobalJQuery;

import static com.eduworks.angles.AppController.*;
import static org.stjs.javascript.Global.window;

/**
 * Created by fray on 6/26/17.
 */
public class AppStart {
	/**
	 * Entry point of the application
	 *
	 * @param {String[]} args
	 *                   Not used at all...
	 */
	public static void main(String[] args) {
		AppController.identityController = new IdentityController();
		AppController.storageController = new StorageController();
		//AppController.loginController = new LoginController(storageController);
		AppController.serverController = new ServerController(storageController, AppSettings.defaultServerUrl, AppSettings.defaultServerName);

		AppSettings.loadSettings();

		// Give Login Controller access to the User Identity Controller
		//AppController.loginController.identity = AppController.identityController;

		//Manage Identity;
		String savedIdentity = null;
		savedIdentity = (String) AppController.storageController.getStoredValue("identity");
		if (savedIdentity != null) {
			EcIdentity ecIdentity = new EcIdentity();
			ecIdentity.ppk = EcPpk.fromPem(savedIdentity);
			ecIdentity.displayName = "My Identity";
			ecIdentity.source = serverController.getRepoInterface().selectedServer;
			EcIdentityManager.addIdentity(ecIdentity);
			AppController.identityController.select(ecIdentity.ppk.toPem());
			startApp();
		} else {
			identityController.generateIdentity(new Callback1<EcIdentity>() {
				@Override
				public void $invoke(EcIdentity ecIdentity) {
					EcIdentityManager.addIdentity(ecIdentity);
					AppController.identityController.select(ecIdentity.ppk.toPem());
					AppController.storageController.setStoredValue("identity", ecIdentity.ppk.toPem());
					startApp();
				}
			}, "My Identity");
		}

	}

	public static void startApp() {
		// Start the Web Application
		ScreenManager.setDefaultScreen(new WelcomeScreen());

		// Show App Menu on document ready
		GlobalJQuery.$(Global.window.document).ready(new EventHandler() {
			@Override
			public boolean onEvent(Event arg0, Element arg1) {
				ViewManager.showView(new WelcomeScreen(), AppController.mainContainer, new Callback0() {
					@Override
					public void $invoke() {
						((Foundation<?>) GlobalJQuery.$(window.document)).foundation();
					}
				});
				return true;
			}
		});
	}
}
