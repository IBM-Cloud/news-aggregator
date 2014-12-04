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

import org.apache.wink.json4j.JSON;
import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONObject;
import com.ibm.websphere.objectgrid.ClientClusterContext;
import com.ibm.websphere.objectgrid.ObjectGrid;
import com.ibm.websphere.objectgrid.ObjectGridManager;
import com.ibm.websphere.objectgrid.ObjectGridManagerFactory;
import com.ibm.websphere.objectgrid.Session;
import com.ibm.websphere.objectgrid.security.config.ClientSecurityConfiguration;
import com.ibm.websphere.objectgrid.security.config.ClientSecurityConfigurationFactory;
import com.ibm.websphere.objectgrid.security.plugins.builtins.UserPasswordCredentialGenerator;

public class DataCacheUtilities {

	public DataCacheUtilities() {
		// TODO Auto-generated constructor stub
	}

	private static DataCacheUtilities singleton;
	private Session session;

	static public DataCacheUtilities getSingleton() {
		if (singleton == null) {
			singleton = new DataCacheUtilities();
		}
		return singleton;
	}

	public Session getSession() {

		if (session != null)
			return session;

		boolean configExists = false;
		String userName = null;
		String password = null;
		String endpoint = null;
		String gridName = null;

		try {
			String dc_userName = System.getenv("NA_DC_USERNAME");
			String db_password = System.getenv("NA_DC_PASSWORD");
			String db_endpoint = System.getenv("NA_DC_CATALOGENDPOINT");
			String db_gridName = System.getenv("NA_DC_GRIDNAME");
			if ((dc_userName != null) && (!dc_userName.equalsIgnoreCase(""))
					&& (db_password != null)
					&& (!db_password.equalsIgnoreCase(""))
					&& (db_endpoint != null)
					&& (!db_endpoint.equalsIgnoreCase(""))
					&& (db_gridName != null)
					&& (!db_gridName.equalsIgnoreCase(""))) {

				userName = dc_userName;
				password = db_password;
				endpoint = db_endpoint;
				gridName = db_gridName;

				configExists = true;
			} else {
				String VCAP_SERVICES = System.getenv("VCAP_SERVICES");
				String serviceName = null;

				if (VCAP_SERVICES == null)
					return null;
				
				Object jsonObject = JSON.parse(VCAP_SERVICES);
				JSONObject json = (JSONObject) jsonObject;
				String key = null;
				JSONArray list = null;
				java.util.Set<String> keys = json.keySet();
				for (String eachkey : keys) {
					if (eachkey.startsWith("DataCache")) {
						key = eachkey;
						break;
					}
				}
				if (key == null) {
					return null;
				}
				list = (JSONArray) json.get(key);
				JSONObject jsonService = (JSONObject) list.get(0);
				JSONObject credentials = (JSONObject) jsonService
						.get("credentials");
				
				userName = (String) credentials.get("username");
				password = (String) credentials.get("password");
				endpoint = (String) credentials.get("catalogEndPoint");
				gridName = (String) credentials.get("gridName");

				configExists = true;
			}
			if (configExists) {
				ObjectGridManager ogm = ObjectGridManagerFactory
						.getObjectGridManager();
				ClientSecurityConfiguration csc = null;
				csc = ClientSecurityConfigurationFactory
						.getClientSecurityConfiguration();
				csc.setCredentialGenerator(new UserPasswordCredentialGenerator(
						userName, password));
				csc.setSecurityEnabled(true);

				ClientClusterContext ccc = ogm.connect(endpoint, csc, null);
				ObjectGrid clientGrid = ogm.getObjectGrid(ccc, gridName);
				session = clientGrid.getSession();
				
				return session;
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
