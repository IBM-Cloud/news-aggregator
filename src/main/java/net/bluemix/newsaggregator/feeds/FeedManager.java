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
import java.util.List;
import java.util.logging.Logger;

import net.bluemix.newsaggregator.NewsEntry;
import net.bluemix.newsaggregator.api.API;

public class FeedManager {

	private static FeedManager singleton;

	static public FeedManager getSingleton() {
		if (singleton == null) {
			singleton = new FeedManager();
		}
		return singleton;
	}

	private static final Logger LOGGER = Logger.getLogger(FeedManager.class
			.getName());

	public FeedManager() {
	}

	public void readFeeds() {
		LOGGER.info("readFeeds invoked");

		List<Feed> feeds = null;
		try {
			feeds = FeedStorage.getSingleton().getFeeds();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("readFeeds failed");
			return;
		}
		if (feeds == null) {
			LOGGER.info("readFeeds failed");
			return;
		}
		for (int i = 0; i < feeds.size(); i++) {
			Feed feed = feeds.get(i);
			Date date = new Date();
			feed.setLastTry(date);
			try {
				RSSReader reader = new RSSReader(feed);
				reader.loadFeed();

				int amount = reader.getSize();
				if (amount > 5)
					amount = 5;
				for (int index = 0; index < amount; index++) {
					NewsEntry entry = reader.getNthEntry(index);
					if (entry != null) {
						boolean save = feed.getLastSuccess() == null || entry.getPublicationDate().after(feed.getLastSuccess());
						if (save) {
							API.getSingleton().addIncomingEntry(entry, feed.getDisplayName());	
						}											
					}
				}

				feed.setLastError(null);
				feed.setLastErrorMessage("");
				feed.setLastSuccess(date);
			} catch (Exception e) {
				e.printStackTrace();
				feed.setLastError(date);
				feed.setLastErrorMessage(e.getMessage());
			}
			FeedStorage.getSingleton().updateFeed(feed);
		}
		try {
			API.getSingleton().updateCache();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		LOGGER.info("readFeeds succeeded");
	}

	public static void main(String[] args) {
		FeedManager.getSingleton().readFeeds();
	}
}
