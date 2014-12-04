/*
 * Copyright IBM Corp. 2014
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.bluemix.newsaggregator.api;

public class User {

	private String username;
	private String[] userDisplayName;
	
	public User() {
		// TODO Auto-generated constructor stub
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String[] getUserDisplayName() {
		return userDisplayName;
	}

	public void setUserDisplayName(String[] userDisplayName) {
		this.userDisplayName = userDisplayName;
	}	
	
	/* Example JSON:
	{
    "lastName": ["SMITH"],
    "username": "http:\/\/www.ibm.com\/johnsmith@somewhere.com",
    "userUniqueID": ["http:\/\/www.ibm.com\/110000Z99Z"],
    "AUTHENTICATION_LEVEL": "2",
    "email": ["johnsmith@somewhere.com"],
    "name": ["JOHN SMITH"],
    "AZN_CRED_CREATE_TIME": "2014-09-24T02:39:13Z",
    "userRealm": "www.ibm.com",
    "userDisplayName": ["johnsmith@somewhere.com"],
    "firstName": ["JOHN"],
    "requestedAuthnPolicy": "http:\/\/www.ibm.com\/idaas\/authnpolicy\/basic\/verified_email",
    "idaas.verified_email": ["johnsmith@somewhere.com"]
	}
	 */
}
