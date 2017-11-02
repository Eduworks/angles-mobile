/*
 Copyright 2015-2016 Eduworks Corporation and other contributing parties.

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
*/
package com.eduworks.angles;

import com.eduworks.angles.controller.IdentityController;
import com.eduworks.angles.controller.LoginController;
import com.eduworks.angles.controller.ServerController;
import com.eduworks.angles.controller.StorageController;

/**
 * Main entry point of the application. Figures out the settings and
 * starts the EC UI Framework at the appropriate screen.
 *
 * @module angles.mobile
 * @class AppController
 * @static
 * 
 * @author devlin.junker@eduworks.com
 */
public class AppController {
	
	public static String mainContainer = "#page";
	
	/**
	 * Manages the server connection by storing and configuring 
	 * the CASS instance endpoint for the rest of the application
	 * and managing the interfaces to it.  
	 * 
	 * @property serverController
	 * @static
	 * @type ServerController
	 */
	public static ServerController serverController;
	
	/**
	 * Manages the current user's identities and contacts through the
	 * KBAC libraries. 
	 * 
	 * @property identityController
	 * @static
	 * @type IdentityController
	 */
	public static IdentityController identityController;
	
	/**
	 * Handles the login/logout and admin communications with the server.
	 * 
	 * @property loginController
	 * @static
	 * @type LoginController
	 */
	public static LoginController loginController;
	
	/**
	 * Handles the browser storage
	 * 
	 * @property sessionController
	 * @static
	 * @type SessionController
	 */
	public static StorageController storageController;


}
