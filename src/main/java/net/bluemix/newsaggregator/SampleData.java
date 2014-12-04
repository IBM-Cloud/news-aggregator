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

import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;
import java.util.ArrayList;

public class SampleData {
 
	public static void main(String[] args) {
		initSampleData();
	}
	
	private static final Logger LOGGER = Logger.getLogger(SampleData.class
			.getName());
	
	public static void initSampleData() {
		LOGGER.info("initSampleData invoked");
		
		try {
			NewsEntry entry = null;
			Person person = null;
			Date date;
			
			date = new Date(System.currentTimeMillis() + 1000*60*60*1);
			entry = new NewsEntry("newsEntry1",
					"person1",
					"IBM Internet of Things Foundation is Ready",
					"https://developer.ibm.com/bluemix/2014/10/22/internet-of-things-foundation/",
					"A new service just went live which addresses the key challenge of bringing the world of sensors and controllers with that of big data and analytics. When combined with the IBM Bluemix platform, IBM Internet of Things Foundation provides simple, but powerful, and scalable application access to devices and their data.",
					date,
					null,
					date,
					date,
					false,
					0,
					NewsEntry.STATE_NEW,
					"Niklas Heidloff",
					"Niklas Heidloff"
					);
			DatabaseUtilities.getSingleton().getDB().create(entry.getId(), entry);
		
			date = new Date(System.currentTimeMillis() + 1000*60*60*2);
			entry = new NewsEntry("newsEntry2",
					"person2",
					"Build a Java EE app on Bluemix Using Watson and Cloudant",
					"https://developer.ibm.com/bluemix/2014/10/17/building-java-ee-app-ibm-bluemix-using-watson-cloudant/",
					"Jeff here again and something I am really excited about is Watson is now available for anyone to use in Bluemix! Today we are going to be building an example app using Java, Cloudant, and Watson.",
					date,
					null,
					date,
					date,
					false,
					0,
					NewsEntry.STATE_NEW,
					"Niklas Heidloff",
					"Niklas Heidloff"
					);
			DatabaseUtilities.getSingleton().getDB().create(entry.getId(), entry);
				
			date = new Date(System.currentTimeMillis() + 1000*60*60*3);
			entry = new NewsEntry("newsEntry3",
					"person3",
					"New BlueMix RapidApps Demo from IBM Impact",
					"http://heidloff.net/nh/home.nsf/article.xsp?id=02.05.2014124306NHEEKZ.htm",
					"As I wrote IBM announced BlueMix RapidApps, a runtime and service to allow business developers to build apps rapidly. Yesterday in the day 3 keynote Jerry Cuomo gave an impressive RapidApps demo.",
					date,
					date,
					date,
					date,
					true,
					1,
					NewsEntry.STATE_CURATED,
					"Niklas Heidloff",
					"Niklas Heidloff"
					);
			DatabaseUtilities.getSingleton().getDB().create(entry.getId(), entry);
		
			date = new Date(System.currentTimeMillis() + 1000*60*60*4);
			entry = new NewsEntry("newsEntry4",
					"person4",
					"Bluemix Session At MWLUG",
					"http://ryanjbaxter.com/2014/08/25/bluemix-session-at-mwlug/",
					"Tomorrow I will be heading out to Grand Rapid MI to attend and speak at MWLUG (Midwest Lotus User Group). If you are attending and would like to learn more about Bluemix, IBM’s new platform for deploying applications to the cloud, be sure to stop by my session at 11AM on Thursday.",
					date,
					date,
					date,
					date,
					false,
					0,
					NewsEntry.STATE_CURATED,
					"Niklas Heidloff",
					"Niklas Heidloff"
					);
			DatabaseUtilities.getSingleton().getDB().create(entry.getId(), entry);				
			
			person = new Person("person1",
					"Peter Crocker",
					"@petecrocker",
					"https://www.ibm.com/developerworks/community/profiles/photo.do?userid=1100006427");
			DatabaseUtilities.getSingleton().getDB().create(person.getId(), person);
			
			person = new Person("person2",
					"Jeff Sloyer",
					"@jsloyer",
					"https://www.ibm.com/developerworks/community/profiles/photo.do?userid=2700002XB9");
			DatabaseUtilities.getSingleton().getDB().create(person.getId(), person);
			
			person = new Person("person3",
					"Niklas Heidloff",
					"@nheidloff",
					"https://pbs.twimg.com/profile_images/527900851457638400/H6-OSw0D_400x400.jpeg");
			DatabaseUtilities.getSingleton().getDB().create(person.getId(), person);
			
			person = new Person("person4",
					"Ryan Baxter",
					"@ryanjbaxter",
					"https://pbs.twimg.com/profile_images/439103773512704000/zYzUFEMa_400x400.jpeg");
			DatabaseUtilities.getSingleton().getDB().create(person.getId(), person);
			
			LOGGER.info("initSampleData succeeded");
		}
		catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("initSampleData failed");
		}				
	}		
}
