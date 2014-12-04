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

import com.wordnik.swagger.annotations.ApiModelProperty;

public class NewsEntryDTO {

	@ApiModelProperty(value = "Author Display Name", required=true)
	String authorDisplayName = null;
	@ApiModelProperty(value = "Title", required=true)
	String title = null;
	@ApiModelProperty(value = "URL", required=true)
	String link = null;
	@ApiModelProperty(value = "First Sentences", required=true)
	String firstSentences = null;
	@ApiModelProperty(value = "Whether to tweet", required=false)
	String tweet = null;
	@ApiModelProperty(value = "Whether entry is top story", required=false)
	String topStory = null;
	@ApiModelProperty(value = "Top Story Position 1 - 10", required=false)
	String topStoryPosition = null;
	
	public NewsEntryDTO() {
		// TODO Auto-generated constructor stub
	}

	public String getTweet() {
		return tweet;
	}
	
	public void setTweet(String tweet) {
		this.tweet = tweet;
	}
	
	public String getAuthorDisplayName() {
		return authorDisplayName;
	}

	public void setAuthorDisplayName(String authorDisplayName) {
		this.authorDisplayName = authorDisplayName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getFirstSentences() {
		return firstSentences;
	}

	public void setFirstSentences(String firstSentences) {
		this.firstSentences = firstSentences;
	}

	public String getTopStory() {
		return topStory;
	}

	public void setTopStory(String topStory) {
		this.topStory = topStory;
	}

	public String getTopStoryPosition() {
		return topStoryPosition;
	}

	public void setTopStoryPosition(String topStoryPosition) {
		this.topStoryPosition = topStoryPosition;
	}	
}
