/*
 * Copyright IBM Corp. 2014
 * Copyright PHL Consult 2012, 2013
 * Copyright Developi Information Systems 2012, 2013
 * Copyright Elguji Software Inc 2012, 2013
 * Copyright Jesse Gallagher 2012, 2013
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Source;

import net.bluemix.newsaggregator.NewsEntry;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RSSReader {

	private final int HTTP_TIMEOUT = 15000;
	private final int STORY_ABSTRACT_MAXLENGTH = 400;
	private final String HTTP_USERAGENT = "Bluemix.info";
	private final String MODIFIED_BY = "Bluemix.info";
	private SyndFeed feed;
	private int size = 0;
	private boolean loaded = false;
	private Feed persistedFeed;

	static {
		final TrustManager[] TRUST_ALL_CERTS = new TrustManager[] { new X509TrustManager() {
			@Override
			public void checkClientTrusted(
					final X509Certificate[] certificates, final String authType)
					throws CertificateException {
			}

			@Override
			public void checkServerTrusted(
					final X509Certificate[] certificates, final String authType)
					throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		} };

		try {
			final SSLContext sslContext = SSLContext.getInstance("SSL");

			sslContext.init(null, TRUST_ALL_CERTS, new SecureRandom());

			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
					.getSocketFactory());
		} catch (final Exception exception) {
			exception.printStackTrace();
		}
	}

	public RSSReader(Feed persistedFeed) {
		this.persistedFeed = persistedFeed;
	}

	public boolean loadFeed() throws Exception {
		if (persistedFeed == null)
			return false;

		URL feedUrl = new URL(persistedFeed.getUrl());

		HttpURLConnection httpSource = (HttpURLConnection) feedUrl
				.openConnection();

		httpSource.setConnectTimeout(HTTP_TIMEOUT);
		httpSource.setReadTimeout(HTTP_TIMEOUT);
		httpSource.addRequestProperty("User-Agent", HTTP_USERAGENT);

		SyndFeedInput input = new SyndFeedInput();
		XmlReader reader = new XmlReader(httpSource);

		this.feed = input.build(reader);
		this.size = feed.getEntries().size();
		this.loaded = true;

		return true;
	}

	public int getSize() {
		return size;
	}

	public NewsEntry getNthEntry(int index) {
		NewsEntry newsEntry = null;

		if (!loaded)
			return null;

		if (index < 0 || index >= size) {
			return null;
		}

		SyndEntry entry = (SyndEntry) feed.getEntries().get(index);
		if (entry == null)
			return null;

		String title = entry.getTitle();
		if (title == null)
			return null;
		title = title.trim();
		if (title.equalsIgnoreCase(""))
			return null;

		String content = getFeedContent(entry);
		if (content == null)
			return null;
		String abstractContent = cleanHTML(content);
		if (abstractContent == null)
			return null;

		if (abstractContent.length() >= STORY_ABSTRACT_MAXLENGTH) {
			abstractContent = abstractContent.substring(0,
					STORY_ABSTRACT_MAXLENGTH - 1) + "[...]";
		}
		abstractContent = abstractContent.trim();

		String link = entry.getLink();
		if (link == null)
			return null;
		if (link.equalsIgnoreCase(""))
			return null;

		Date publicationDate = new Date();
		if (entry.getPublishedDate() != null) {
			publicationDate = entry.getPublishedDate();
		} else if (entry.getUpdatedDate() != null) {
			publicationDate = entry.getUpdatedDate();
		}

		newsEntry = new NewsEntry();
		UUID id = UUID.randomUUID();
		Date date;
		date = new Date();
		newsEntry = new NewsEntry(id.toString(), persistedFeed.getAuthorId(),
				title, link, abstractContent, publicationDate, null, date,
				date, false, 0, NewsEntry.STATE_NEW, null, MODIFIED_BY);

		return newsEntry;
	}

	private String getFeedContent(SyndEntry entry) {
		StringBuffer output = new StringBuffer();
		if (entry.getDescription() == null) {
			Iterator<SyndContent> contentIterator = entry.getContents()
					.iterator();

			while (contentIterator.hasNext()) {
				SyndContent content = contentIterator.next();
				output.append(content.getValue());
			}
		} else {
			SyndContent content = entry.getDescription();
			output.append(content.getValue());
		}
		return output.toString();
	}

	private String cleanHTML(String input) {
		Renderer renderer = new Source(input).getRenderer();

		renderer.setMaxLineLength(Integer.MAX_VALUE);
		renderer.setIncludeHyperlinkURLs(false);

		return renderer.toString();
	}
}