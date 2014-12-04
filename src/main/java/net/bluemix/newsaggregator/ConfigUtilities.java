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
package net.bluemix.newsaggregator;

public class ConfigUtilities {

	public ConfigUtilities() {
		// TODO Auto-generated constructor stub
	}

	private static ConfigUtilities singleton;

	static public ConfigUtilities getSingleton() {
		if (singleton == null) {
			singleton = new ConfigUtilities();
			singleton.initialize();
		}
		return singleton;
	}

	private String NA_SSO_REDIRECTURI;
	private String NA_SSO_CLIENT_IDENTIFIER;
	private String NA_SSO_CLIENT_SECRET;
	private String[] NA_CURATORS;
	private String NA_TW_CONSUMER_KEY;
	private String NA_TW_CONSUMER_SECRET;
	private String NA_TW_ACCESS_TOKEN;
	private String NA_TW_ACCESS_TOKEN_SECRET;
	
	public String getTwitterConsumerKey() {
		return NA_TW_CONSUMER_KEY;
	}
	
	public String getTwitterConsumerSecret() {
		return NA_TW_CONSUMER_SECRET;
	}
	
	public String getTwitterAccessToken() {
		return NA_TW_ACCESS_TOKEN;
	}
	
	public String getTwitterAccessTokenSecret() {
		return NA_TW_ACCESS_TOKEN_SECRET;
	}

	public String getSSORedirectUri() {
		return NA_SSO_REDIRECTURI;
	}

	public String getSSOClientIdentifier() {
		return NA_SSO_CLIENT_IDENTIFIER;
	}

	public String getSSOClientSecret() {
		return NA_SSO_CLIENT_SECRET;
	}

	public String[] getCurators() {
		return NA_CURATORS;
	}

	public void initialize() {

		try {
			String value = System.getenv("NA_LOCAL");
			if ((value != null) && (!value.equalsIgnoreCase(""))) {

				NA_SSO_REDIRECTURI = System.getenv("NA_SSO_REDIRECTURI");
				NA_SSO_CLIENT_IDENTIFIER = System
						.getenv("NA_SSO_CLIENT_IDENTIFIER");
				NA_SSO_CLIENT_SECRET = System.getenv("NA_SSO_CLIENT_SECRET");
				
				NA_TW_CONSUMER_KEY = System.getenv("NA_TW_CONSUMER_KEY");
				NA_TW_CONSUMER_SECRET = System.getenv("NA_TW_CONSUMER_SECRET");
				NA_TW_ACCESS_TOKEN = System.getenv("NA_TW_ACCESS_TOKEN");
				NA_TW_ACCESS_TOKEN_SECRET = System.getenv("NA_TW_ACCESS_TOKEN_SECRET");
				
				String curators = System.getenv("NA_CURATORS");
				if (curators != null) {
					NA_CURATORS = curators.split(",");
				} else {
					NA_CURATORS = new String[0];
				}

			} else {
				String VCAP_SERVICES = System.getenv("VCAP_SERVICES");

				if (VCAP_SERVICES != null) {
					com.ibm.json.java.JSONObject obj = com.ibm.json.java.JSONObject
							.parse(VCAP_SERVICES);
					String dbKey = null;
					java.util.Set<String> keys = obj.keySet();
					for (String eachkey : keys) {
						if (eachkey.contains("user-provided")) {
							dbKey = eachkey;
							break;
						}
					}
					if (dbKey == null) {
						return;
					}

					com.ibm.json.java.JSONArray list = (com.ibm.json.java.JSONArray) obj
							.get(dbKey);
					obj = (com.ibm.json.java.JSONObject) list.get(0);

					obj = (com.ibm.json.java.JSONObject) obj.get("credentials");

					NA_SSO_REDIRECTURI = (String) obj.get("NA_SSO_REDIRECTURI");
					NA_SSO_CLIENT_IDENTIFIER = (String) obj
							.get("NA_SSO_CLIENT_IDENTIFIER");
					NA_SSO_CLIENT_SECRET = (String) obj
							.get("NA_SSO_CLIENT_SECRET");
					
					String curators = (String) obj.get("NA_CURATORS");
					if (curators != null) {
						NA_CURATORS = curators.split(",");
					} else {
						NA_CURATORS = new String[0];
					}
					
					NA_TW_CONSUMER_KEY = (String) obj.get("NA_TW_CONSUMER_KEY");
					NA_TW_CONSUMER_SECRET = (String) obj.get("NA_TW_CONSUMER_SECRET");
					NA_TW_ACCESS_TOKEN = (String) obj.get("NA_TW_ACCESS_TOKEN");
					NA_TW_ACCESS_TOKEN_SECRET = (String) obj.get("NA_TW_ACCESS_TOKEN_SECRET");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
