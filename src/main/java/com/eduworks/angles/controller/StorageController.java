/*
 Copyright 2015-2016 Eduworks Corporation and other contributing parties.

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
*/
package com.eduworks.angles.controller;

import org.stjs.javascript.*;

/**
 * Manages the storage object of the browser whether it be the session or 
 * local storage, and provides methods for retrieving values.
 * 
 * Also holds a list of recently viewed object ids for different classes
 * for a specific computer in the browsers storage, this can be used
 * to precache objects for use or to display the recently viewed objects 
 *
 * @module angles.mobile
 * @class SessionController
 * @constructor
 * 
 * @author devlin.junker@eduworks.com
 */
public class StorageController {
	
	Storage storageSystem;
	
	int MAX_RECENT = 3;
	
	Map<String, Array<String>> recent;
	
	public StorageController(){
		if(Global.localStorage != null)
			storageSystem = Global.localStorage;
		else if(Global.sessionStorage != null)
			storageSystem = Global.sessionStorage;
	
		String recent = (String) storageSystem.$get("cass.recent");
		
		if(recent != null && recent != ""){
			try{
				this.recent = (Map<String, Array<String>>) Global.JSON.parse(recent);
			}catch(Exception e){
				this.recent = JSCollections.$map();
			}
		}else{
			this.recent = JSCollections.$map();
		}
	}
	
	
	public Object getStoredValue(String key){
		return storageSystem.$get(key);
	}
	
	public void setStoredValue(String key, Object val){
		storageSystem.$put(key, val);
	}
	
	public Array<String> getRecent(String type){
		Array<String> list = recent.$get(type);
		
		if(list == null){
			list = JSCollections.$array();
			recent.$put(type, list);
			
			storageSystem.$put("cass.recent", Global.JSON.stringify(recent));
		}
		
		return list;
	}
	
	public void addRecent(String type, String id){
		
		Array<String> list = recent.$get(type);
		
		if(list == null){
			list = JSCollections.$array(id);
		}else if(list.indexOf(id) == -1){
			if(list.$length() < MAX_RECENT){
				list.push(id);
			}else{
				list.shift();
				list.push(id);
			}
		}
		
		recent.$put(type, list);
		
		storageSystem.$put("cass.recent", Global.JSON.stringify(recent));
	}
}
