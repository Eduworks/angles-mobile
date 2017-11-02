/*
 Copyright 2015-2016 Eduworks Corporation and other contributing parties.

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
*/
package com.eduworks.angles;

import org.stjs.javascript.JSCollections;
import org.stjs.javascript.Map;


/**
 * Handles loading the CASS Manager settings from the settings.js file,
 * this includes the default server to show and the message to show when the user
 * refreshes the page and is logged out
 *
 * @module angles.mobile
 * @class AppSettings
 * @static
 * 
 * @author devlin.junker@eduworks.com
 */
public class AppSettings {

	public static String FIELD_MSG_RETURN = "returnLoginMessage";
	
	public static String FIELD_SERVER_URL = "defaultServerUrl";
	public static String FIELD_SERVER_NAME = "defaultServerName";
	
	public static String FIELD_SHOW_REPO_MENU = "showRepoMenu";
	public static String FIELD_SHOW_EXAMPLES_MENU = "showExamplesMenu";

	
	

	public static String defaultServerUrl = "https://angles.eduworks.org/api/";
	public static String defaultServerName = "ANGLES Server";

	public static boolean showRepoMenu = false;
	public static boolean showExamplesMenu = false;
	
	public static Map<String,String> relationTypes;
	
	static {
		relationTypes = JSCollections.$map(
			"isEnabledBy", "is Enabled by",
			"requires", "Requires",
	        "desires", "Desires",
	        "narrows", "Narrows",
	        "isRelatedTo", "is Related to",
	        "isEquivalentTo", "is Equivalent to"
		);
	}
	        
	
	/**
	 * Loads the settings from the settings file at settings/settings.js
	 * 
	 * @static
	 * @method loadSettings
	 */
	public static void loadSettings(){

	}
}
