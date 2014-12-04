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

import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import net.bluemix.newsaggregator.api.API;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.ektorp.CouchDbConnector;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterUtilities {

	private static TwitterUtilities singleton;

	static public TwitterUtilities getSingleton() {
		if (singleton == null) {
			singleton = new TwitterUtilities();
		}
		return singleton;
	}

	public TwitterUtilities() {
	}

	public String tweetNewsEntry(String newsEntryId) {
		if (newsEntryId == null) return null;
		if (newsEntryId.equalsIgnoreCase("")) return null;
		
		try {
			NewsEntry newsEntry = API.getSingleton().getCuratedNewsEntryById(newsEntryId);
			if (newsEntry == null) return null;
			
			Person person = API.getSingleton().getPersonById(newsEntry.getPersonId());
			if (person == null) return null;
			
			String title = newsEntry.getTitle();
			String link = newsEntry.getLink();
			String personName = person.getTwitter();
			if ((personName == null) || personName.equalsIgnoreCase("")) {
				personName = person.getDisplayName();				
			}
			if ((personName == null) || personName.equalsIgnoreCase("")) {
				personName = "";
			}
			else {
				personName = " via " + personName;
			}
						
			String hashtag = " #bluemix";
			
			int l = 140 - 20 - personName.length() - 1 - hashtag.length();
			if (title.length() > l) {
				title = title.substring(0, l - 1);
			}
			
			String message = title + personName + hashtag + " " + link;
			
			return tweet(message);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}				
	}	
	
	private String tweet(String message) {
		String output = null;
		if (message == null) return null;
		if (message.equalsIgnoreCase("")) return null;
		
		try {
			String consumerKey = ConfigUtilities.getSingleton().getTwitterConsumerKey();
			String consumerSecret = ConfigUtilities.getSingleton().getTwitterConsumerSecret();
			String accessToken = ConfigUtilities.getSingleton().getTwitterAccessToken();
			String accessTokenSecret = ConfigUtilities.getSingleton().getTwitterAccessTokenSecret();
			
			TwitterFactory twitterFactory = new TwitterFactory();			 
	        Twitter twitter = twitterFactory.getInstance();
	        twitter.setOAuthConsumer(consumerKey, consumerSecret);
	        twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
	 
	        StatusUpdate statusUpdate = new StatusUpdate(message);	      

	        Status status = twitter.updateStatus(statusUpdate);
	        if (status == null) return null;
	        output = "https://twitter.com/BluemixInfo/status/" + String.valueOf(status.getId());

			return output;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return output;
	}	
	
	public static void main(String[] args) {
		TwitterUtilities.getSingleton().tweetNewsEntry("356b8741-eea3-4f95-8a20-b713478663e9");
	}
}
