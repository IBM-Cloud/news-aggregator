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

import org.ektorp.support.CouchDbDocument;

public class Person  extends CouchDbDocument {
	 
	private String twitter;
	private String displayName;
	private String pictureURL;
	final static String OBJECT_TYPE = "Person";
	private String type;

	public Person() {		
		this.type = OBJECT_TYPE;
	}
	
	public Person(String id, String displayName, String twitter, String pictureURL) {
		setId(id);
		this.twitter = twitter;
		this.displayName = displayName;	
		this.pictureURL = pictureURL;
		this.type = OBJECT_TYPE;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPictureURL() {
		return pictureURL;
	}

	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}
}
