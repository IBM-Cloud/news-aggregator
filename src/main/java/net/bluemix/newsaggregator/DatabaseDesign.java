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

import java.util.logging.Logger;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.DesignDocument;

public class DatabaseDesign {

	private static final Logger LOGGER = Logger.getLogger(DatabaseDesign.class
			.getName());
	
	public static void main(String[] args) {
		LOGGER.info("initDatabaseDesign invoked");
		
		try {
			CouchDbConnector db = DatabaseUtilities.getSingleton().getDB();
			
			createDesign(db);

			LOGGER.info("initDatabaseDesign succeeded");
		}
		catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("initDatabaseDesign failed");
		}
	}

	public static void createDesign(CouchDbConnector db) {
		if (!db.contains("_design/views")) {
			DesignDocument dd = new DesignDocument("_design/views");
			
			String mapNewEntries = "function(doc) {\n    if (doc.type == \"NewsEntry\") {\n  emit(doc._id, 1);\n    }\n}";
			String mapCuratedEntriesByCurationDate = "function(doc) {\n  if (doc.type == \"NewsEntry\") {\n      if (doc.state == \"curated\") {\n        emit(doc.curationDate, doc);\n      }\n  }\n}";
			String mapIncomingEntriesByCreation = "function(doc) {\r\n  if (doc.type == \"NewsEntry\") {\r\n      if (doc.state == \"new\") {\r\n        emit(doc.creationDate, doc);\r\n      }\r\n  }\r\n}";
			String mapPersons = "function(doc) {\r\nif (doc.type == \"Person\") {\r\n  emit(doc._id, 1);\r\n    }\r\n}";
			String mapFeeds = "function(doc) {\r\nif (doc.type == \"Feed\") {\r\n  emit(doc._id, 1);\r\n    }\r\n}";
			
			DesignDocument.View view = new DesignDocument.View(mapNewEntries);
			dd.addView("allNewsEntries", view);
		
			view = new DesignDocument.View(mapCuratedEntriesByCurationDate);
			dd.addView("curatedNewsEntriesByCurationDate", view);
			
			view = new DesignDocument.View(mapIncomingEntriesByCreation);
			dd.addView("incomingNewsEntriesByCreationDate", view);
			
			view = new DesignDocument.View(mapPersons);
			dd.addView("allPersons", view);
			
			view = new DesignDocument.View(mapFeeds);
			dd.addView("allFeeds", view);
				
			db.create(dd);
		}
	}
}
