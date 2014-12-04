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

import java.util.List;

import com.wordnik.swagger.annotations.ApiModelProperty;

import net.bluemix.newsaggregator.NewsEntry;
import net.bluemix.newsaggregator.Person;

public class ResponseNewsDTO {

	private List<EntryDTO> incomingEntries;
	private List<EntryDTO> topStories;
	private List<EntryDTO> curatedEntries;
	private boolean isCurrentUserCurator;
	
	public ResponseNewsDTO() {		
	}
	
	@ApiModelProperty(value = "Recent curated News Entries", required=true)
	public List<EntryDTO> getCuratedEntries() {
		return curatedEntries;
	}

	public void setCuratedEntries(List<EntryDTO> curatedEntries) {
		this.curatedEntries = curatedEntries;
	}

	@ApiModelProperty(value = "Recent incoming (not curated) news entries", required=true)
	public List<EntryDTO> getIncomingEntries() {
		return incomingEntries;
	}

	public void setIncomingEntries(List<EntryDTO> incomingEntries) {
		this.incomingEntries = incomingEntries;
	}

	@ApiModelProperty(value = "Recent Top Stories", required=true)
	public List<EntryDTO> getTopStories() {
		return topStories;
	}

	public void setTopStories(List<EntryDTO> topStories) {
		this.topStories = topStories;
	}

	@ApiModelProperty(value = "Is current User Curator", required=true)
	public boolean isCurrentUserCurator() {
		return isCurrentUserCurator;
	}
	
	public void setCurrentUserCurator(boolean isCurrentUserCurator) {
		this.isCurrentUserCurator = isCurrentUserCurator;
	}		
}
