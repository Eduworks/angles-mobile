/*
 Copyright 2015-2016 Eduworks Corporation and other contributing parties.

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
*/
package com.eduworks.angles.controller;

import com.eduworks.ec.crypto.EcPk;
import com.eduworks.ec.crypto.EcPpk;
import org.cassproject.ebac.identity.EcContact;
import org.cassproject.ebac.identity.EcIdentity;
import org.cassproject.ebac.identity.EcIdentityManager;
import org.cassproject.schema.general.EcRemoteLinkedData;
import org.stjs.javascript.JSObjectAdapter;
import org.stjs.javascript.functions.Callback0;
import org.stjs.javascript.functions.Callback1;

/**
 * Manages the current selected identity for the user, and interfaces 
 * the EBAC Identity Manager library to provide helper functions for 
 * ownership and key identification
 * 
 * @module angles.mobile
 * @class IdentityController
 * @constructor
 *
 * @author devlin.junker@eduworks.com
 */
public class IdentityController
{
	public EcIdentity selectedIdentity = null;

	public IdentityController(){
		EcIdentityManager.clearContacts();
	}
	
	/**
	 * Sets the currently selected identity to the ppk specified, only works if the ppk is in the 
	 * list of identities that the user owns
	 * 
	 * @method select
	 * @param {String} ppkPem
	 * 			PEM of the identity that will be set to the current identity
	 */
	public void select(String ppkPem)
	{
		EcPpk clickedPpk = EcPpk.fromPem(ppkPem);
		for (int i = 0; i < EcIdentityManager.ids.$length(); i++)
			if (EcIdentityManager.ids.$get(i).ppk.equals(clickedPpk))
				selectedIdentity = EcIdentityManager.ids.$get(i);
	}
	
	/**
	 * Clears the selected identity, so the user will be identified as public for any actions
	 * that they make going forward
	 * 
	 * @method unselect
	 */
	public void unselect(){
		selectedIdentity = null;
	}
	
	static EcContact unknownContact = new EcContact(); 
	
	/**
	 * Returns the contact that is associated with the PEM given, looks at both the user's
	 * identities and contacts to match the PEM. The Contact returned will contain the display
	 * name that the user set for the PEM
	 *
	 * @method lookup
	 * @param {String} pkPem 
	 * 			PEM of the contact to lookup
	 * @return Contact that contains the displayName and public key, if the user
	 * 			does not have a display name stored for the PEM in either their contacts or identities,
	 * 			will return the Unknown Contact which contains the key and a display name of "Unknown"
	 */
	public EcContact lookup(String pkPem)
	{
		EcPk candidatePk = EcPk.fromPem(pkPem);
		
		for(int i = 0; i < EcIdentityManager.ids.$length(); i++)
		{
			if(EcIdentityManager.ids.$get(i).ppk.toPk().equals(candidatePk))
			{
				EcContact newContact = new EcContact();
				newContact.pk = candidatePk;
				newContact.displayName = EcIdentityManager.ids.$get(i).displayName;
				return newContact;
			}
		}		
		
		for (int i = 0; i < EcIdentityManager.contacts.$length(); i++)
			if (EcIdentityManager.contacts.$get(i).pk.equals(candidatePk))
				return EcIdentityManager.contacts.$get(i);
		
		unknownContact.pk = candidatePk;
		unknownContact.displayName = "Unknown";
		return unknownContact;
	}

	/**
	 * Adds a Key to the list of user identities managed by the EcIdentityManager
	 * 
	 * @method addKey
	 * @param {String} ppk
	 * 			PEM representation of PPK Key to save to user identities
	 * @param {String} displayName 
	 * 			Name to associate with the key to be saved, to identify it
	 * @param {Callback0} success
	 * 			Callback function once the key has been added
	 */
	public void addKey(String ppk, String displayName, Callback0 success)
	{
		EcIdentity ident = new EcIdentity();
		ident.ppk = EcPpk.fromPem(ppk);
		ident.displayName = displayName;
		EcIdentityManager.addIdentity(ident);
		
		if(success != null)
			success.$invoke();
	}
	
	/**
	 * Adds a contact to the list of user's contacts managed by EcIdentityManager
	 * 
	 * @method addContact
	 * @param {String} pk
	 * 			PEM representation of PK Key to save user contact
	 * @param {String} displayName
	 * 			Name to associate with the key to be saved, to identify it
	 * @param {Callback0} success
	 * 			Callback function once the contact has been added
	 */
	public void addContact(String pk, String displayName, Callback0 success)
	{
		EcContact contact = new EcContact();
		contact.pk = EcPk.fromPem(pk);
		contact.displayName = displayName;
		EcIdentityManager.addContact(contact);
		
		if(success != null)
			success.$invoke();
	}

	/**
	 * Generates a new Encryption Key to save to the identity manager list
	 * 
	 * @method generateIdentity
	 * @param {Callback1<EcIdentity>} success
	 * 			callback, once they key has been generated and added to the identity manager
	 * @param {String} displayName
	 * 			display name for the key that is being generated to identify it
	 */
	public void generateIdentity(final Callback1<EcIdentity> success, final String displayName)
	{
		EcPpk.generateKeyAsync(new Callback1<EcPpk>()
		{
			public void $invoke(EcPpk p1)
			{
				EcIdentity ident = new EcIdentity();
				ident.ppk = p1;
				if(displayName != null && displayName != "")
					ident.displayName = displayName;
				
				EcIdentityManager.addIdentity(ident);
				
				if(success != null)
					success.$invoke(ident);
			}
		});
	}
	
	/**
	 * Helper function to determine if the logged in user owns a piece of data from the repository,
	 * useful for showing certain actions
	 * 
	 * @method owns
	 * @param {EcRemoteLiinkedData} data 
	 * 			The object to check for ownership of
	 * @return {boolean} true if owned, false if not owned by the current user
	 */
	public boolean owns(EcRemoteLinkedData data){
		if(JSObjectAdapter.$get(data, "hasOwner") != null)
			for(int i = 0; i < EcIdentityManager.ids.$length(); i++)
			{
				if(data.hasOwner(EcIdentityManager.ids.$get(i).ppk.toPk())){
					return true;
				}
			}
		return false;
	}
	
	/**
	 * Helper function to determine if the logged in user can modify a piece of data, this means 
	 * that they either own the data, or it is public
	 * 
	 * @method canEdit
	 * @param {EcRemoteLinkedData} data
	 * 			The object to check for ability to edit
	 * @return {boolean} true if editable, false if not
	 */
	public boolean canEdit(EcRemoteLinkedData data){
		if(data.owner == null || data.owner.$length() == 0)
			return true;
		for(int i = 0; i < EcIdentityManager.ids.$length(); i++)
		{
			if(data.canEdit(EcIdentityManager.ids.$get(i).ppk.toPk())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Helper function to determine if the current user is associated with the key passed in
	 * 
	 * @method isCurrentUser
	 * @param {String} pk
	 * 			PEM representation of pk to check
	 * @return {boolean} true if the current logged in user is associated with the key
	 */
	public boolean isCurrentUser(String pk){
		
		for(int i = 0; i < EcIdentityManager.ids.$length(); i++)
		{
			if(EcIdentityManager.ids.$get(i).ppk.toPk().toString() == pk){
				return true;
			}
		}
		return false;
	}
}
