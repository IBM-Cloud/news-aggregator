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

import java.util.Date;
import org.ektorp.support.CouchDbDocument;

public class NewsEntry extends CouchDbDocument {

	public final static String STATE_NEW = "new";
	public final static String STATE_CURATED = "curated";
	public final static String OBJECT_TYPE = "NewsEntry";

	private String personId;
	private String link;
	private String firstSentences;
	private Date curationDate;	
	private Date publicationDate;
	private Date creationDate;
	private boolean isTopStory;
	private int topStoryPosition;
	private String state;
	private String curator;
	private String lastEditor;
	private Date lastModified;
	private String title;
	private String type;

	public NewsEntry() {	
		this.type = OBJECT_TYPE;
	}
	
	public NewsEntry(String newsEntryId, String personId, 
			String title, String link, String firstSentences, 
			Date publicationDate, Date curationDate, Date creationDate, Date lastModified,
			 boolean isTopStory, int topStoryPosition, 
			 String state, String curator, String lastEditor) {
		setId(newsEntryId);		
		this.title = title;
		this.personId = personId;
		this.link = link;
		this.firstSentences = firstSentences;
		this.curationDate = curationDate;
		this.publicationDate = publicationDate;
		this.isTopStory = isTopStory;
		this.topStoryPosition = topStoryPosition;
		this.creationDate = creationDate;
		this.state = state;
		this.curator = curator;
		this.lastEditor = lastEditor;
		this.lastModified = lastModified;
		this.type = this.type = OBJECT_TYPE;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
		
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
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

	public Date getCurationDate() {
		return curationDate;
	}

	public void setCurationDate(Date curationDate) {
		this.curationDate = curationDate;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public boolean isTopStory() {
		return isTopStory;
	}

	public void setTopStory(boolean isTopStory) {
		this.isTopStory = isTopStory;
	}

	public int getTopStoryPosition() {
		return topStoryPosition;
	}

	public void setTopStoryPosition(int topStoryPosition) {
		this.topStoryPosition = topStoryPosition;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCurator() {
		return curator;
	}

	public void setCurator(String curator) {
		this.curator = curator;
	}

	public String getLastEditor() {
		return lastEditor;
	}

	public void setLastEditor(String lastEditor) {
		this.lastEditor = lastEditor;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
}

