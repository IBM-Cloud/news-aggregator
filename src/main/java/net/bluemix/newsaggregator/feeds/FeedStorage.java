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
import net.bluemix.newsaggregator.NewsEntry;
import net.bluemix.newsaggregator.Person;
import net.bluemix.newsaggregator.api.API;
import net.bluemix.newsaggregator.api.AuthorDTO;
import net.bluemix.newsaggregator.api.FeedDTO;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;

public class FeedStorage {

	public FeedStorage() {
	}

	private static FeedStorage singleton;

	static public FeedStorage getSingleton() {
		if (singleton == null) {
			singleton = new FeedStorage();
		}
		return singleton;
	}

	public boolean updateFeed(Feed feed) {
		CouchDbConnector db;
		
		db = DatabaseUtilities.getSingleton().getDB();
		if (db == null)
			return false;

		db.update(feed);
		return true;
	}
	
	public String addOrUpdateFeed(FeedDTO feed) {
		String output = null;
		CouchDbConnector db;

		if (feed == null)
			return null;

		String displayName = feed.getDisplayName();
		String authorDisplayName = feed.getAuthorDisplayName();
		String url = feed.getUrl();

		if (displayName == null)
			return null;
		if (authorDisplayName == null)
			return null;
		if (url == null)
			return null;

		try {
			Person existingPerson = API.getSingleton().getPersonByDisplayName(
					authorDisplayName);
			String personId;
			if (existingPerson == null) {
				personId = API.getSingleton().createPerson(authorDisplayName);
				if (personId == null)
					return null;
			} else {
				personId = existingPerson.getId();
			}

			db = DatabaseUtilities.getSingleton().getDB();
			if (db == null)
				return null;

			Feed existingFeed = getFeedByDisplayName(displayName);
			if (existingFeed == null) {
				Feed newFeed = new Feed();
				UUID id = UUID.randomUUID();
				newFeed.setId(id.toString());
				newFeed.setAuthorId(personId);
				newFeed.setDisplayName(displayName);
				newFeed.setUrl(url);
				db.create(newFeed.getId(), newFeed);

				return newFeed.getId();
			} else {
				existingFeed.setUrl(url);
				existingFeed.setAuthorId(personId);
				db.update(existingFeed);

				return existingFeed.getId();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Feed> getFeeds() throws Exception {
		CouchDbConnector db;
		List<Feed> output = new ArrayList<Feed>();

		db = DatabaseUtilities.getSingleton().getDB();

		ViewQuery viewQuery = new ViewQuery().dbPath(db.path())
				.designDocId("_design/views").viewName("allFeeds")
				.includeDocs(true).cacheOk(false).descending(true);
		output = db.queryView(viewQuery, Feed.class);

		return output;
	}

	private Feed getFeedByDisplayName(String displayName) {
		try {
			List<Feed> allFeeds = getFeeds();
			for (int i = 0; i < allFeeds.size(); i++) {
				Feed feed = allFeeds.get(i);
				if (feed.getDisplayName().equalsIgnoreCase(displayName)) {
					return feed;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
}
