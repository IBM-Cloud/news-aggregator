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

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class FeedDTO {

	public FeedDTO() {
		// TODO Auto-generated constructor stub
	}

	@ApiModelProperty(value = "Feed URL", required=true)
	private String url;
	@ApiModelProperty(value = "Author Display Name", required=true)
	private String authorDisplayName;
	@ApiModelProperty(value = "Feed Display Name", required=true)
	private String displayName;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAuthorDisplayName() {
		return authorDisplayName;
	}
	public void setAuthorDisplayName(String authorDisplayName) {
		this.authorDisplayName = authorDisplayName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}	
}
