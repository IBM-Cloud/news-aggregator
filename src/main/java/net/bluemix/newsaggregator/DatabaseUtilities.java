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

import java.net.MalformedURLException;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import com.google.gson.Gson;

public class DatabaseUtilities {

	private DatabaseUtilities() {
	}

	private static DatabaseUtilities singleton;
	private CouchDbConnector db;

	static public DatabaseUtilities getSingleton() {
		if (singleton == null) {
			singleton = new DatabaseUtilities();
		}
		return singleton;
	}

	public CouchDbConnector getDB() {

		if (db != null) return db;
 
		boolean configExists = false;
		String host = null;
		String username = null;
		String password = null;
		
		try {
			String db_url = System.getenv("NA_DB_HOST");
			String db_user = System.getenv("NA_DB_USERNAME");
			String db_pw = System.getenv("NA_DB_PASSWORD");
			if ((db_url != null) && (!db_url.equalsIgnoreCase(""))
					&& (db_user != null) && (!db_user.equalsIgnoreCase(""))
					&& (db_pw != null) && (!db_pw.equalsIgnoreCase(""))) {
				
				host = db_url;
				username = db_user;
				password = db_pw;

				configExists = true;
			} else {
				String VCAP_SERVICES = System.getenv("VCAP_SERVICES");		
				
				if (VCAP_SERVICES != null) {
					com.ibm.json.java.JSONObject obj = com.ibm.json.java.JSONObject
							.parse(VCAP_SERVICES);
					String dbKey = null;
					java.util.Set<String> keys = obj.keySet();
					for (String eachkey : keys) {
						if (eachkey.contains("cloudantNoSQLDB")) {
							dbKey = eachkey;
							break;
						}
					}
					if (dbKey == null) {
						return null;
					}

					com.ibm.json.java.JSONArray list = (com.ibm.json.java.JSONArray) obj
							.get(dbKey);
					obj = (com.ibm.json.java.JSONObject) list.get(0);

					obj = (com.ibm.json.java.JSONObject) obj.get("credentials");

					host = (String) obj.get("host");
					username = (String) obj.get("username");
					password = (String) obj.get("password");
					
					configExists = true;					
				} 				
			}
			if (configExists) {
				HttpClient httpClient;

				httpClient = new StdHttpClient.Builder()
						.url("https://" + host + "/bluemixinfo").username(username)
						.password(password).build();

				CouchDbInstance dbInstance = new StdCouchDbInstance(
						httpClient);

				db = dbInstance.createConnector("bluemixinfo", true);
				
				db.createDatabaseIfNotExists();
				DatabaseDesign.createDesign(db);
 
				return db;
			}
			else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
