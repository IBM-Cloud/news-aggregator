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

import java.util.Date;

import org.ektorp.support.CouchDbDocument;

public class SchedulerData extends CouchDbDocument {

	public final static String OBJECT_TYPE = "SchedulerData";
	private String type;
	private String userName;
	private String password;
	private Date lastChanged;
	private long processId;
	private boolean on;
	
	public SchedulerData() {
		this.type = OBJECT_TYPE;	
	}
	
	public SchedulerData(String schedulerDataId, String userName,
			String password, Date lastChanged, long processId, boolean on) {
		setId(schedulerDataId);	
		this.type = OBJECT_TYPE;
		this.userName = userName;
		this.password = password;
		this.processId = processId;
		this.lastChanged = lastChanged;
		this.on = on;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getLastChanged() {
		return lastChanged;
	}

	public void setLastChanged(Date lastChanged) {
		this.lastChanged = lastChanged;
	}

	public long getProcessId() {
		return processId;
	}

	public void setProcessId(long processId) {
		this.processId = processId;
	}

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}	
}
