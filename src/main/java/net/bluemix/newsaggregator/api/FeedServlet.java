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

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.bluemix.newsaggregator.ConfigUtilities;
import net.bluemix.newsaggregator.NewsEntry;
import net.bluemix.newsaggregator.Person;

import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;

import com.google.gson.Gson;
import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.SyndFeedOutput;

import java.io.FileWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeedServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FeedServlet() {
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		try {
			SyndFeed feed = new SyndFeedImpl();
            feed.setFeedType("rss_2.0");

            feed.setTitle("Bluemix.info");
            feed.setLink("http://www.bluemix.info");
            feed.setDescription("Bluemix.info - News Aggregator for IBM Bluemix Developers");

            List<SyndEntry> entries = new ArrayList<SyndEntry>();
            SyndEntry entry;
            SyndContent description;
            
            List<NewsEntry> newsEntries = API.getSingleton().getCuratedNewsEntriesLastMonthByCurationDate();
            if (newsEntries != null) {
            	int maxAmount = 20;
            	int amount = 20;
            	if (newsEntries.size() <= maxAmount) {
            		amount = newsEntries.size();
            	}
            	for (int i = 0; i < amount; i++) {
            		NewsEntry newsEntry = newsEntries.get(i);

		            entry = new SyndEntryImpl();
		            entry.setTitle(newsEntry.getTitle());
		            entry.setLink(newsEntry.getLink());
		            entry.setPublishedDate(newsEntry.getCurationDate());
		            description = new SyndContentImpl();
		            description.setType("text/plain");
		            description.setValue(newsEntry.getFirstSentences());
		            entry.setDescription(description);
		            
		            Person p = API.getSingleton().getPersonById(newsEntry.getPersonId());
		            if (p == null) {
		            	entry.setAuthor("Unknown");
		            }
		            else {
		            	entry.setAuthor(p.getDisplayName());	
		            }		            
		            entries.add(entry);
            	}
            }            
            feed.setEntries(entries);
            
            Writer writer = response.getWriter();
            SyndFeedOutput output = new SyndFeedOutput();
            output.output(feed, writer);
            writer.close();		
		
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
