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

import java.util.List;

public class Filter {
 
	private String description;
	private List<String> curatorLoginNames;
	private String displayName;
	private List<String> hashTags;
	private int position;	
	private String id;
	
	public Filter(String id, String description, String displayName, 
			int position, List<String> curatorLoginNames, List<String> hashTags) {
		this.id = id;
		this.description = description;
		this.displayName = displayName;
		this.position = position;
		this.curatorLoginNames = curatorLoginNames;
		this.hashTags = hashTags;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<String> getCuratorLoginNames() {
		return curatorLoginNames;
	}
	public void setCuratorLoginNames(List<String> curatorLoginNames) {
		this.curatorLoginNames = curatorLoginNames;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public List<String> getHashTags() {
		return hashTags;
	}
	public void setHashTags(List<String> hashTags) {
		this.hashTags = hashTags;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
}
