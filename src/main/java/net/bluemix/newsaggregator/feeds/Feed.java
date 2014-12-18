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

public class Feed extends CouchDbDocument {
	public final static String OBJECT_TYPE = "Feed";

	private String type;
	private String url;
	private String authorId;
	private boolean redirection = false;
	private Date lastSuccess;
	private Date lastReadEntry;
	private Date lastTry;
	private Date lastError;
	private String displayName;
	private String lastErrorMessage;

	public Feed() {	
		this.type = OBJECT_TYPE;
	}
	
	public Feed(String newsEntryId) {
		setId(newsEntryId);		
	}

	public Date getLastReadEntry() {
		return lastReadEntry;
	}

	public void setLastReadEntry(Date lastReadEntry) {
		this.lastReadEntry = lastReadEntry;
	}

	public String getType() {
		return OBJECT_TYPE;
	}

	public void setType(String type) {		
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public boolean isRedirection() {
		return redirection;
	}

	public void setRedirection(boolean redirection) {
		this.redirection = redirection;
	}

	public Date getLastSuccess() {
		return lastSuccess;
	}

	public void setLastSuccess(Date lastSuccess) {
		this.lastSuccess = lastSuccess;
	}

	public Date getLastTry() {
		return lastTry;
	}

	public void setLastTry(Date lastTry) {
		this.lastTry = lastTry;
	}

	public Date getLastError() {
		return lastError;
	}

	public void setLastError(Date lastError) {
		this.lastError = lastError;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getLastErrorMessage() {
		return lastErrorMessage;
	}

	public void setLastErrorMessage(String lastErrorMessage) {
		this.lastErrorMessage = lastErrorMessage;
	}
}
