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
package net.bluemix.newsaggregator.feeds;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import net.bluemix.newsaggregator.DatabaseUtilities;
import org.apache.wink.json4j.JSON;
import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONObject;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
// TODO add again once workload scheduler libs are on Maven
/*
import com.ibm.twa.applab.client.WorkloadService;
import com.ibm.twa.applab.client.helpers.Step;
import com.ibm.twa.applab.client.helpers.TriggerFactory;
import com.ibm.twa.applab.client.helpers.WAProcess;
import com.ibm.twa.applab.client.helpers.steps.RestfulStep;
import com.ibm.twa.applab.client.helpers.steps.restutils.RESTAction;
import com.ibm.twa.applab.client.helpers.steps.restutils.RESTAuthenticationData;
import com.ibm.twa.applab.client.helpers.steps.restutils.RESTInput;
import com.ibm.tws.simpleui.bus.Task;
*/
public class SchedulerUtilities {

	private SchedulerUtilities() {
	}

	// TODO add again once workload scheduler libs are on Maven
	/*
	private static SchedulerUtilities singleton;
	private WorkloadService ws;
	String agentName = "_CLOUD";

	static public SchedulerUtilities getSingleton() {
		if (singleton == null) {
			singleton = new SchedulerUtilities();
		}
		return singleton;
	}

	public WorkloadService getWorkloadService() {

		if (ws != null)
			return ws;

		boolean configExists = false;
		String url = null;

		try {
			String ws_url = System.getenv("NA_WS_URL");
			if ((ws_url != null) && (!ws_url.equalsIgnoreCase(""))) {
				url = ws_url;
				configExists = true;
			} else {
				String VCAP_SERVICES = System.getenv("VCAP_SERVICES");

				if (VCAP_SERVICES != null) {
					Object jsonObject = JSON.parse(VCAP_SERVICES);
					JSONObject json = (JSONObject) jsonObject;
					String key = null;
					JSONArray list = null;
					java.util.Set<String> keys = json.keySet();
					for (String eachkey : keys) {
						if (eachkey.contains("WorkloadScheduler")) {
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

					url = (String) credentials.get("url");
					configExists = true;
				}
			}
			if (configExists) {
				int index = url.indexOf("tenantId=") + 9;
				String prefix = url.substring(index, index + 2);
				agentName = prefix + agentName;
				try {
					WorkloadService.disableCertificateValidation();
					ws = new WorkloadService(url);
				} catch (Exception e) {
					return null;
				}

				return ws;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String turnOn() {
		if (ws == null) return null;
		SchedulerData data = getData();
		if (data == null) return null;
		
		if (data.isOn()) return "Already on";
		
		WAProcess process = new WAProcess("BluemixInfo", "BluemixInfo");
		RESTAction action = new RESTAction(
				"http://www.bluemix.info/api/readfeedsscheduler",
				"application/json", "text/html", RestfulStep.GET_METHOD,
				"file.text");
		RESTAuthenticationData auth = RESTAuthenticationData.fromUserPwd(
				data.getUserName(), data.getPassword());

		RESTInput input = RESTInput.fromText("");
		Step step = new RestfulStep(agentName, action, auth, input);
		process.addStep(step);

		try {
			process.addTrigger(TriggerFactory.everyDayAt(0, 0));
			process.addTrigger(TriggerFactory.everyDayAt(1, 0));
			process.addTrigger(TriggerFactory.everyDayAt(2, 0));
			process.addTrigger(TriggerFactory.everyDayAt(3, 0));
			process.addTrigger(TriggerFactory.everyDayAt(4, 0));
			process.addTrigger(TriggerFactory.everyDayAt(5, 0));
			process.addTrigger(TriggerFactory.everyDayAt(6, 0));
			process.addTrigger(TriggerFactory.everyDayAt(7, 0));
			process.addTrigger(TriggerFactory.everyDayAt(8, 0));
			process.addTrigger(TriggerFactory.everyDayAt(9, 0));
			process.addTrigger(TriggerFactory.everyDayAt(10, 0));
			process.addTrigger(TriggerFactory.everyDayAt(11, 0));
			process.addTrigger(TriggerFactory.everyDayAt(12, 0));
			process.addTrigger(TriggerFactory.everyDayAt(13, 0));
			process.addTrigger(TriggerFactory.everyDayAt(14, 0));
			process.addTrigger(TriggerFactory.everyDayAt(15, 0));
			process.addTrigger(TriggerFactory.everyDayAt(16, 0));
			process.addTrigger(TriggerFactory.everyDayAt(17, 0));
			process.addTrigger(TriggerFactory.everyDayAt(18, 0));
			process.addTrigger(TriggerFactory.everyDayAt(19, 0));
			process.addTrigger(TriggerFactory.everyDayAt(20, 0));
			process.addTrigger(TriggerFactory.everyDayAt(21, 0));
			process.addTrigger(TriggerFactory.everyDayAt(22, 0));
			process.addTrigger(TriggerFactory.everyDayAt(23, 0));

			Task task = ws.createAndEnableTask(process);
			if (task != null) {
				long processId = task.getId();
				data.setProcessId(processId);
				data.setLastChanged(new Date());
				data.setOn(true);
				
				ws.runTask(processId);

				try {
					CouchDbConnector db;
					db = DatabaseUtilities.getSingleton().getDB();
					db.update(data);
					
					return "Scheduler turned on";
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
		return null;
	}

	public String turnOff() {
		if (ws == null) return null;
		SchedulerData data = getData();
		if (data == null) return null;
		
		if (data.isOn() == false) return "Already off";
		
		try {
			ws.deleteTask(data.getProcessId());
			
			data.setLastChanged(new Date());
			data.setOn(false);
			
			CouchDbConnector db;
			db = DatabaseUtilities.getSingleton().getDB();
			db.update(data);
			
			return "Scheduler turned off";
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}

	public SchedulerData getData() {
		SchedulerData out = null;
		CouchDbConnector db;
		try {
			List<SchedulerData> list = new ArrayList<SchedulerData>();

			db = DatabaseUtilities.getSingleton().getDB();

			ViewQuery viewQuery = new ViewQuery().dbPath(db.path())
					.designDocId("_design/views").viewName("allSchedulerData")
					.includeDocs(true).cacheOk(false);
			list = db.queryView(viewQuery, SchedulerData.class);
			if (list != null) {
				if (list.size() > 0) {
					out = list.get(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (out == null) {
			try {
				db = DatabaseUtilities.getSingleton().getDB();
				UUID id = UUID.randomUUID();
				UUID userName = UUID.randomUUID();
				UUID password = UUID.randomUUID();

				SchedulerData data = new SchedulerData(id.toString(),
						userName.toString(), password.toString(), null, 0, false);

				db.create(data.getId(), data);

				out = db.get(SchedulerData.class, data.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return out;
	}

	public static void main(String[] args) {
		WorkloadService w = SchedulerUtilities.getSingleton()
				.getWorkloadService();
		SchedulerUtilities.getSingleton().turnOff();

	}
	*/
}
