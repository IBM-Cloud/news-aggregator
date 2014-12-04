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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.bluemix.newsaggregator.DataCacheUtilities;
import net.bluemix.newsaggregator.DatabaseUtilities;
import net.bluemix.newsaggregator.NewsEntry;
import net.bluemix.newsaggregator.Person;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;

import com.ibm.websphere.objectgrid.*;
import com.ibm.websphere.objectgrid.security.config.ClientSecurityConfiguration;
import com.ibm.websphere.objectgrid.security.config.ClientSecurityConfigurationFactory;
import com.ibm.websphere.objectgrid.security.plugins.builtins.UserPasswordCredentialGenerator;

public class API {

	private API() {
	}

	private static API singleton;

	static public API getSingleton() {
		if (singleton == null) {
			singleton = new API();
		}
		return singleton;
	}

	private boolean initialized = false;
	private List<NewsEntry> curatedEntriesLastMonthByCurationDate;
	private List<NewsEntry> incomingEntriesLastMonthByCreationDate;
	private List<Person> allPersons;

	public void updateCache() throws Exception {
		incomingEntriesLastMonthByCreationDate = null;
		curatedEntriesLastMonthByCurationDate = null;
		allPersons = null;

		curatedEntriesLastMonthByCurationDate = getCuratedNewsEntriesLastMonthByCurationDateFromDatabase();
		incomingEntriesLastMonthByCreationDate = getIncomingNewsEntriesLastMonthByCreationDateFromDatabase();
		allPersons = getAllPersonsFromDatabase();

		String value = System.getenv("NA_LOCAL");
		if ((value != null) && (!value.equalsIgnoreCase(""))) {
			// do nothing (data cache service not supported when running
			// locally)
		} else {
			Session session = DataCacheUtilities.getSingleton().getSession();
			if (session != null) {
				try {
					ObjectMap map = session.getMap("namap.NONE.P");
					if (map != null) {
						map.upsert("curatedEntriesLastMonthByCurationDate",
								curatedEntriesLastMonthByCurationDate);
						map.upsert("incomingEntriesLastMonthByCreationDate",
								incomingEntriesLastMonthByCreationDate);
						map.upsert("allPersons", allPersons);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void initialize() {
		if (!initialized) {
			try {
				String value = System.getenv("NA_LOCAL");
				if ((value != null) && (!value.equalsIgnoreCase(""))) {
					updateCache();
				} else {
					Session session = DataCacheUtilities.getSingleton()
							.getSession();
					Object curatedEntriesLastMonthByCurationDateObject = null;
					if (session != null) {
						try {
							ObjectMap map = session.getMap("namap.NONE.P");
							if (map != null) {
								curatedEntriesLastMonthByCurationDateObject = map
										.get("curatedEntriesLastMonthByCurationDate");
								if (curatedEntriesLastMonthByCurationDateObject != null) {
									curatedEntriesLastMonthByCurationDate = (List<NewsEntry>) curatedEntriesLastMonthByCurationDateObject;
								}
								Object incomingEntriesLastMonthByCreationDateObject = map
										.get("incomingEntriesLastMonthByCreationDate");
								if (incomingEntriesLastMonthByCreationDateObject != null) {
									incomingEntriesLastMonthByCreationDate = (List<NewsEntry>) incomingEntriesLastMonthByCreationDateObject;
								}
								Object allPersonsObject = map.get("allPersons");
								if (allPersonsObject != null) {
									allPersons = (List<Person>) allPersonsObject;
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					if (curatedEntriesLastMonthByCurationDate == null) {
						updateCache();
					} else {
						if (curatedEntriesLastMonthByCurationDate.size() == 0) {
							updateCache();
						}
					}
				}
				initialized = true;
			} catch (Exception e) {
				initialized = false;
			}
		}
	}

	private List<NewsEntry> getFromCacheCuratedEntriesLastMonthByCurationDate() {
		String value = System.getenv("NA_LOCAL");
		if ((value != null) && (!value.equalsIgnoreCase(""))) {
			return curatedEntriesLastMonthByCurationDate;
		} else {
			Session session = DataCacheUtilities.getSingleton().getSession();
			Object curatedEntriesLastMonthByCurationDateObject = null;
			if (session != null) {
				try {
					ObjectMap map = session.getMap("namap.NONE.P");
					if (map != null) {
						curatedEntriesLastMonthByCurationDateObject = map
								.get("curatedEntriesLastMonthByCurationDate");
						if (curatedEntriesLastMonthByCurationDateObject != null) {
							curatedEntriesLastMonthByCurationDate = (List<NewsEntry>) curatedEntriesLastMonthByCurationDateObject;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return curatedEntriesLastMonthByCurationDate;
	}

	private List<NewsEntry> getFromCacheIncomingEntriesLastMonthByCreationDate() {
		String value = System.getenv("NA_LOCAL");
		if ((value != null) && (!value.equalsIgnoreCase(""))) {
			return incomingEntriesLastMonthByCreationDate;
		} else {
			Session session = DataCacheUtilities.getSingleton().getSession();
			Object curatedEntriesLastMonthByCurationDateObject = null;
			if (session != null) {
				try {
					ObjectMap map = session.getMap("namap.NONE.P");
					if (map != null) {
						Object incomingEntriesLastMonthByCreationDateObject = map
								.get("incomingEntriesLastMonthByCreationDate");
						if (incomingEntriesLastMonthByCreationDateObject != null) {
							incomingEntriesLastMonthByCreationDate = (List<NewsEntry>) incomingEntriesLastMonthByCreationDateObject;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return incomingEntriesLastMonthByCreationDate;
	}

	private List<Person> getFromCacheAllPersons() {
		String value = System.getenv("NA_LOCAL");
		if ((value != null) && (!value.equalsIgnoreCase(""))) {
			return allPersons;
		} else {
			Session session = DataCacheUtilities.getSingleton().getSession();
			Object curatedEntriesLastMonthByCurationDateObject = null;
			if (session != null) {
				try {
					ObjectMap map = session.getMap("namap.NONE.P");
					if (map != null) {
						Object allPersonsObject = map.get("allPersons");
						if (allPersonsObject != null) {
							allPersons = (List<Person>) allPersonsObject;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return allPersons;
	}

	public List<NewsEntry> getAllNewsEntries() {
		CouchDbConnector db;

		try {
			db = DatabaseUtilities.getSingleton().getDB();
			if (db == null)
				return new ArrayList<NewsEntry>();

			ViewQuery viewQuery = new ViewQuery().dbPath(db.path())
					.designDocId("_design/views").viewName("allNewsEntries")
					.includeDocs(true).cacheOk(false);
			return db.queryView(viewQuery, NewsEntry.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private List<NewsEntry> getIncomingNewsEntriesLastMonthByCreationDateFromDatabase()
			throws Exception {
		CouchDbConnector db;
		List<NewsEntry> output = new ArrayList<NewsEntry>();

		Date date = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24
				* 30);

		db = DatabaseUtilities.getSingleton().getDB();

		ViewQuery viewQuery = new ViewQuery().dbPath(db.path())
				.designDocId("_design/views")
				.viewName("incomingNewsEntriesByCreationDate")
				.includeDocs(true).cacheOk(false).descending(true)
				.startKey(date);
		output = db.queryView(viewQuery, NewsEntry.class);

		return output;
	}

	public List<NewsEntry> getIncomingNewsEntriesLastMonthByCreationDate() {

		initialize();
		if (!initialized)
			return null;

		return getFromCacheIncomingEntriesLastMonthByCreationDate();
	}

	public List<NewsEntry> getIncomingNewsEntriesLastMonthByCreationDate(
			int amount) {

		initialize();
		if (!initialized)
			return null;

		List<NewsEntry> entries = getFromCacheIncomingEntriesLastMonthByCreationDate();
		List<NewsEntry> output = new ArrayList<NewsEntry>();
		if (amount > entries.size()) {
			amount = entries.size();
		}
		for (int i = 0; i < amount; i++) {
			output.add(entries.get(i));
		}
		return output;
	}

	private List<NewsEntry> getCuratedNewsEntriesLastMonthByCurationDateFromDatabase()
			throws Exception {
		CouchDbConnector db;
		List<NewsEntry> output = new ArrayList<NewsEntry>();

		Date date = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24
				* 30);

		db = DatabaseUtilities.getSingleton().getDB();

		ViewQuery viewQuery = new ViewQuery().dbPath(db.path())
				.designDocId("_design/views")
				.viewName("curatedNewsEntriesByCurationDate").includeDocs(true)
				.cacheOk(false).descending(true).startKey(date);
		output = db.queryView(viewQuery, NewsEntry.class);

		return output;
	}

	public List<NewsEntry> getCuratedNewsEntriesLastMonthByCurationDate() {

		initialize();
		if (!initialized)
			return null;

		return getFromCacheCuratedEntriesLastMonthByCurationDate();
	}

	public List<NewsEntry> getCuratedNewsEntriesLastMonthByCurationDate(
			int amount) {

		initialize();
		if (!initialized)
			return null;

		List<NewsEntry> entries = getFromCacheCuratedEntriesLastMonthByCurationDate();
		List<NewsEntry> output = new ArrayList<NewsEntry>();

		if (amount > entries.size()) {
			amount = entries.size();
		}
		for (int i = 0; i < amount; i++) {
			output.add(entries.get(i));
		}

		return output;
	}

	public List<NewsEntry> getTopStories() {

		initialize();
		if (!initialized)
			return null;

		List<NewsEntry> entries = getFromCacheCuratedEntriesLastMonthByCurationDate();
		List<NewsEntry> output = new ArrayList<NewsEntry>();
		for (int i = 0; i < entries.size(); i++) {
			if (entries.get(i).isTopStory()) {
				output.add(entries.get(i));
			}
		}
		return output;
	}

	private List<Person> getAllPersonsFromDatabase() throws Exception {
		CouchDbConnector db;
		List<Person> output = new ArrayList<Person>();

		db = DatabaseUtilities.getSingleton().getDB();

		ViewQuery viewQuery = new ViewQuery().dbPath(db.path())
				.designDocId("_design/views").viewName("allPersons")
				.includeDocs(true).cacheOk(false);
		return db.queryView(viewQuery, Person.class);
	}

	public List<Person> getAllPersons() {

		initialize();
		if (!initialized)
			return null;

		return getFromCacheAllPersons();
	}

	public String addOrUpdatePerson(AuthorDTO person) {

		if (person == null)
			return null;

		String displayName = person.getDisplayName();
		String twitter = person.getTwitter();
		String pictureUrl = person.getPictureUrl();

		CouchDbConnector db;
		if (displayName == null)
			return null;
		if (displayName.equals(""))
			return null;
		boolean useDefaultPic = false;

		if (pictureUrl == null) {
			useDefaultPic = true;
		} else {
			if (pictureUrl.equals(""))
				useDefaultPic = true;
			if (!pictureUrl.contains("://"))
				useDefaultPic = true;
		}
		if (useDefaultPic)
			pictureUrl = "head.png";

		try {
			db = DatabaseUtilities.getSingleton().getDB();
			if (db == null)
				return null;

			Person existingPerson = getPersonByDisplayName(displayName);
			if (existingPerson == null) {
				net.bluemix.newsaggregator.Person persistedPerson;
				persistedPerson = new net.bluemix.newsaggregator.Person();
				persistedPerson.setDisplayName(displayName);
				persistedPerson.setTwitter(twitter);
				persistedPerson.setPictureURL(pictureUrl);
				UUID id = UUID.randomUUID();
				persistedPerson.setId(id.toString());

				db.create(persistedPerson.getId(), persistedPerson);
				updateCache();

				return persistedPerson.getId();
			} else {
				existingPerson.setTwitter(twitter);
				existingPerson.setPictureURL(pictureUrl);

				db.update(existingPerson);
				updateCache();
				return existingPerson.getId();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String createPerson(String displayName) {

		if (displayName == null) return null;
		if (displayName.equalsIgnoreCase("")) return null;
		
		String twitter = "";
		String pictureUrl = "head.png";

		try {
			CouchDbConnector db;
			db = DatabaseUtilities.getSingleton().getDB();
			if (db == null)
				return null;

			Person person = new Person();
			person.setDisplayName(displayName);
			person.setTwitter(twitter);
			person.setPictureURL(pictureUrl);
			UUID id = UUID.randomUUID();
			person.setId(id.toString());

			db.create(person.getId(), person);
			updateCache();

			return person.getId();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Person getPersonByDisplayName(String displayName) {
		Person output = null;

		initialize();
		if (!initialized)
			return null;

		List<Person> cache = getFromCacheAllPersons();
		for (int i = 0; i < cache.size(); i++) {
			if (cache.get(i).getDisplayName() != null) {
				if (cache.get(i).getDisplayName()
						.equalsIgnoreCase(displayName)) {
					output = cache.get(i);
				}
			}
		}

		return output;
	}

	public Person getPersonById(String iD) {
		Person output = null;

		initialize();
		if (!initialized)
			return null;

		List<Person> cache = getFromCacheAllPersons();
		for (int i = 0; i < cache.size(); i++) {
			if (cache.get(i).getId()
					.equalsIgnoreCase(iD)) {
				output = cache.get(i);
			}
		}

		return output;
	}

	public NewsEntry getCuratedNewsEntryById(String iD) {
		NewsEntry output = null;

		initialize();
		if (!initialized)
			return null;
		
		List<NewsEntry> cache = getFromCacheCuratedEntriesLastMonthByCurationDate();
		for (int i = 0; i < cache.size(); i++) {
			if (cache.get(i).getId()
					.equalsIgnoreCase(iD)) {
				output = cache.get(i);
			}
		}

		return output;
	}
	
	public NewsEntry getIncomingNewsEntryById(String iD) {
		NewsEntry output = null;

		initialize();
		if (!initialized)
			return null;
		
		List<NewsEntry> cache = getFromCacheIncomingEntriesLastMonthByCreationDate();
		for (int i = 0; i < cache.size(); i++) {
			if (cache.get(i).getId()
					.equalsIgnoreCase(iD)) {
				output = cache.get(i);
			}
		}

		return output;
	}
	
	private NewsEntry getCuratedNewsEntryByUrl(String url) {
		NewsEntry output = null;

		initialize();
		if (!initialized)
			return null;
		
		List<NewsEntry> cache = getFromCacheCuratedEntriesLastMonthByCurationDate();
		for (int i = 0; i < cache.size(); i++) {
			if (cache.get(i).getLink()
					.equalsIgnoreCase(url)) {
				output = cache.get(i);
			}
		}

		return output;
	}

	public String addOrUpdateEntry(NewsEntryDTO newsEntry, String curator) {

		String personDisplayName = newsEntry.getAuthorDisplayName();
		String title = newsEntry.getTitle();
		String link = newsEntry.getLink();
		String firstSentences = newsEntry.getFirstSentences();

		CouchDbConnector db;
		db = DatabaseUtilities.getSingleton().getDB();
		if (db == null)
			return null;

		if (personDisplayName == null)
			return null;
		if (personDisplayName.equals(""))
			return null;
		if (title == null)
			return null;
		if (title.equals(""))
			return null;
		if (link == null)
			return null;
		if (link.equals(""))
			return null;
		if (!link.contains("://"))
			return null;
		if (firstSentences == null)
			return null;
		if (firstSentences.equals(""))
			return null;
		if (curator == null)
			curator = "Development Environment";
		if (curator.equals(""))
			curator = "Development Environment";

		try {
			Person existingPerson = getPersonByDisplayName(personDisplayName);
			String personId;
			if (existingPerson == null) {
				personId = createPerson(personDisplayName);
				if (personId == null)
					return null;
			} else {
				personId = existingPerson.getId();
			}
			
			NewsEntry cachedEntry = getCuratedNewsEntryByUrl(link);
			if (cachedEntry == null) {
				UUID id = UUID.randomUUID();
	
				NewsEntry entry = null;
				Date date;
				date = new Date();						
				
				entry = new NewsEntry(id.toString(), personId, title, link,
						firstSentences, date, date, date, date, isTopStory(newsEntry), getTopStoryPositionAsInt(newsEntry),
						NewsEntry.STATE_CURATED, curator, curator);
				db.create(entry.getId(), entry);
				updateCache();
				return entry.getId();
			}
			else {
				Date date;
				date = new Date();
				cachedEntry.setTitle(title);
				cachedEntry.setLink(link);
				cachedEntry.setFirstSentences(firstSentences);
				cachedEntry.setCurationDate(date);
				cachedEntry.setLastModified(date);
				cachedEntry.setCurator(curator);
				cachedEntry.setLastEditor(curator);
				cachedEntry.setTopStory(isTopStory(newsEntry));
				cachedEntry.setTopStoryPosition(getTopStoryPositionAsInt(newsEntry));
				db.update(cachedEntry);
				updateCache();
				return cachedEntry.getId();
			}			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String updateEntry(String newsEntryId, NewsEntryDTO newsEntry, String curator) {

		if (newsEntryId == null) return null;
		if (newsEntryId.equalsIgnoreCase("")) return null;
		
		String personDisplayName = newsEntry.getAuthorDisplayName();
		String title = newsEntry.getTitle();
		String link = newsEntry.getLink();
		String firstSentences = newsEntry.getFirstSentences();

		CouchDbConnector db;
		db = DatabaseUtilities.getSingleton().getDB();
		if (db == null)
			return null;

		if (curator == null)
			curator = "Development Environment";
		if (curator.equals(""))
			curator = "Development Environment";

		try {
			Person existingPerson = getPersonByDisplayName(personDisplayName);
			String personId;
			if (existingPerson == null) {
				personId = createPerson(personDisplayName);
				if (personId == null)
					return null;
			} else {
				personId = existingPerson.getId();
			}
			
			NewsEntry cachedEntry = getCuratedNewsEntryById(newsEntryId);
			if (cachedEntry == null) {
				cachedEntry = getIncomingNewsEntryById(newsEntryId);
			}
			if (cachedEntry == null) {
				return null;
			}
			else {
				Date date;
				date = new Date();
				
				if (title != null) {
					if (!title.equalsIgnoreCase("")) {
						cachedEntry.setTitle(title);		
					}
				}
				
				if (link != null) {
					if (!link.equalsIgnoreCase("")) {
						if (link.contains("://")) {
							cachedEntry.setLink(link);
						}
					}
				}
				
				if (firstSentences != null) {
					if (!firstSentences.equalsIgnoreCase("")) {
						cachedEntry.setFirstSentences(firstSentences);		
					}
				}				
				
				if (newsEntry.getAuthorDisplayName() != null) {
					if (!newsEntry.getAuthorDisplayName().equalsIgnoreCase("")) {
						cachedEntry.setPersonId(personId);
					}
				}
				
				cachedEntry.setLastModified(date);
				cachedEntry.setCurator(curator);
				cachedEntry.setLastEditor(curator);
				
				cachedEntry.setTopStory(isTopStory(newsEntry));
				cachedEntry.setTopStoryPosition(getTopStoryPositionAsInt(newsEntry));
				
				db.update(cachedEntry);
				updateCache();
				
				return cachedEntry.getId();
			}			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String addIncomingEntry(NewsEntry newsEntry, String feedName) {

		if (newsEntry == null) return null;		

		CouchDbConnector db;
		db = DatabaseUtilities.getSingleton().getDB();
		if (db == null)
			return null;

		if (newsEntry.getTitle() == null)
			return null;
		if (newsEntry.getTitle().equals(""))
			return null;
		if (newsEntry.getLink() == null)
			return null;
		if (newsEntry.getLink().equals(""))
			return null;
		if (!newsEntry.getLink().contains("://"))
			return null;
		if (newsEntry.getFirstSentences() == null)
			return null;
		if (newsEntry.getFirstSentences().equals(""))
			return null;	
		String personId = newsEntry.getPersonId();
		if (personId == null)
			return null;
		if (personId.equals(""))
			return null;		

		try {
			Person existingPerson = getPersonById(personId);
			if (existingPerson == null) {
				personId = createPerson(feedName);
				if (personId == null)
					return null;
			} else {
				personId = existingPerson.getId();
			}
			
			NewsEntry cachedEntry = getCuratedNewsEntryByUrl(newsEntry.getLink());
			if (cachedEntry == null) {					
				db.create(newsEntry.getId(), newsEntry);
				return newsEntry.getId();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	public String deleteNewsEntry(String newsEntryId) {

		if (newsEntryId == null) return null;
		if (newsEntryId.equalsIgnoreCase("")) return null;
		
		try {
			CouchDbConnector db;
			db = DatabaseUtilities.getSingleton().getDB();
			if (db == null)
				return null;

			NewsEntry newsEntry = db.get(NewsEntry.class, newsEntryId);
			if (newsEntry == null) return null;
			
			db.delete(newsEntry);
			updateCache();

			return newsEntryId;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String approveNewsEntry(String newsEntryId, String curator) {

		if (newsEntryId == null) return null;
		if (newsEntryId.equalsIgnoreCase("")) return null;
		
		try {
			CouchDbConnector db;
			db = DatabaseUtilities.getSingleton().getDB();
			if (db == null)
				return null;

			NewsEntry newsEntry = db.get(NewsEntry.class, newsEntryId);
			if (newsEntry == null) return null;
			
			if (curator == null)
				curator = "Development Environment";
			if (curator.equals(""))
				curator = "Development Environment";
			
			Date date = new Date();
			newsEntry.setCurationDate(date);
			newsEntry.setCurator(curator);
			newsEntry.setLastEditor(curator);
			newsEntry.setLastModified(date);
			newsEntry.setState(NewsEntry.STATE_CURATED);
			
			db.update(newsEntry);
			updateCache();

			return newsEntryId;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private boolean isTopStory(NewsEntryDTO entry) {
		boolean isTopStory = false;
		if (entry.getTopStory() != null) {
			if (entry.getTopStory().equalsIgnoreCase("true")) {
				isTopStory = true;
			}
		}		
		return isTopStory;
	}
	
	private int getTopStoryPositionAsInt(NewsEntryDTO entry) {
		int output = 0;
		if (entry.getTopStoryPosition() != null) {
			if (!entry.getTopStoryPosition().equalsIgnoreCase("")) {
				try {
					output = Integer.parseInt(entry.getTopStoryPosition());
				}
				catch (Exception e) {							
				}
			}
		}		
		return output;
	}
}
