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

import net.bluemix.newsaggregator.NewsEntry;
import net.bluemix.newsaggregator.Person;

public class EntryDTO {

	public final static String STATE_NEW = "new";
	public final static String STATE_CURATED = "curated";
	
	@ApiModelProperty(value = "Author Display Name", required=true)
	private String authorDisplayName;
	@ApiModelProperty(value = "Author Picture URL", required=true)
	private String authorPictureURL;
	@ApiModelProperty(value = "Author Twitter Name (with @)", required=false)
	private String authorTwitter;
	
	@ApiModelProperty(value = "News Entry Link", required=true)
	private String newsEntryLink;
	@ApiModelProperty(value = "News Entry First Sentences", required=true)
	private String newsEntryFirstSentences;
	@ApiModelProperty(value = "News Entry Curation Date", required=true)
	private Date newsEntryCurationDate;	
	@ApiModelProperty(value = "News Entry Publication Date", required=true)
	private Date newsEntryPublicationDate;
	@ApiModelProperty(value = "Is News Entry Top Story", required=true)
	private boolean newsEntryIsTopStory;
	@ApiModelProperty(value = "Top Story Position", required=true)
	private int newsEntryTopStoryPosition;
	@ApiModelProperty(value = "News Entry State", required=true, allowableValues = STATE_NEW + "," + STATE_CURATED)
	private String newsEntryState;
	@ApiModelProperty(value = "News Entry Curator", required=true)
	private String newsEntryCurator;
	@ApiModelProperty(value = "News Entry Title", required=true)
	private String newsEntryTitle;
	@ApiModelProperty(value = "News Entry ID", required=true)
	private String newsEntryId;
	
	public EntryDTO() {		
	}

	public EntryDTO(NewsEntry newsEntry, Person person) {
		authorDisplayName = person.getDisplayName();
		authorPictureURL = person.getPictureURL();
		authorTwitter = person.getTwitter();
		
		newsEntryLink = newsEntry.getLink();
		newsEntryFirstSentences = newsEntry.getFirstSentences();
		newsEntryCurationDate = newsEntry.getCurationDate();
		newsEntryPublicationDate = newsEntry.getPublicationDate();
		newsEntryIsTopStory = newsEntry.isTopStory();
		newsEntryTopStoryPosition = newsEntry.getTopStoryPosition();
		newsEntryState = newsEntry.getState();
		newsEntryCurator = newsEntry.getCurator();
		newsEntryTitle = newsEntry.getTitle();
		newsEntryId = newsEntry.getId();
	}
	
	public String getNewsEntryId() {
		return newsEntryId;
	}

	public void setNewsEntryId(String newsEntryId) {
		this.newsEntryId = newsEntryId;
	}

	public String getAuthorTwitter() {
		return authorTwitter;
	}
	public void setAuthorTwitter(String authorTwitter) {
		this.authorTwitter = authorTwitter;
	}
	public String getAuthorDisplayName() {
		return authorDisplayName;
	}
	public void setAuthorDisplayName(String authorDisplayName) {
		this.authorDisplayName = authorDisplayName;
	}
	public String getAuthorPictureURL() {
		return authorPictureURL;
	}
	public void setAuthorPictureURL(String authorPictureURL) {
		this.authorPictureURL = authorPictureURL;
	}
	public String getNewsEntryLink() {
		return newsEntryLink;
	}
	public void setNewsEntryLink(String newsEntryLink) {
		this.newsEntryLink = newsEntryLink;
	}
	public String getNewsEntryFirstSentences() {
		return newsEntryFirstSentences;
	}
	public void setNewsEntryFirstSentences(String newsEntryFirstSentences) {
		this.newsEntryFirstSentences = newsEntryFirstSentences;
	}
	public Date getNewsEntryCurationDate() {
		return newsEntryCurationDate;
	}
	public void setNewsEntryCurationDate(Date newsEntryCurationDate) {
		this.newsEntryCurationDate = newsEntryCurationDate;
	}
	public Date getNewsEntryPublicationDate() {
		return newsEntryPublicationDate;
	}
	public void setNewsEntryPublicationDate(Date newsEntryPublicationDate) {
		this.newsEntryPublicationDate = newsEntryPublicationDate;
	}
	public boolean isNewsEntryIsTopStory() {
		return newsEntryIsTopStory;
	}
	public void setNewsEntryIsTopStory(boolean newsEntryIsTopStory) {
		this.newsEntryIsTopStory = newsEntryIsTopStory;
	}
	public int getNewsEntryTopStoryPosition() {
		return newsEntryTopStoryPosition;
	}
	public void setNewsEntryTopStoryPosition(int newsEntryTopStoryPosition) {
		this.newsEntryTopStoryPosition = newsEntryTopStoryPosition;
	}
	public String getNewsEntryState() {
		return newsEntryState;
	}
	public void setNewsEntryState(String newsEntryState) {
		this.newsEntryState = newsEntryState;
	}
	public String getNewsEntryCurator() {
		return newsEntryCurator;
	}
	public void setNewsEntryCurator(String newsEntryCurator) {
		this.newsEntryCurator = newsEntryCurator;
	}
	public String getNewsEntryTitle() {
		return newsEntryTitle;
	}
	public void setNewsEntryTitle(String newsEntryTitle) {
		this.newsEntryTitle = newsEntryTitle;
	}
}
