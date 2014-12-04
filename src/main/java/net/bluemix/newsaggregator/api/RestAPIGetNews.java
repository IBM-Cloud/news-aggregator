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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.bluemix.newsaggregator.ConfigUtilities;
import net.bluemix.newsaggregator.NewsEntry;
import net.bluemix.newsaggregator.Person;

import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Api(value = "api/news")
@Path("news")
public final class RestAPIGetNews {

	private static final Logger LOGGER = Logger.getLogger(RestAPIGetNews.class
			.getName());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get news", 
	protocols = "http, https",
	notes = "Get curated and incoming news entries and top stories", position = 1, 
	nickname = "Get news", httpMethod = "Get")		
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "Success", response = ResponseNewsDTO.class),
	@ApiResponse(code = 500, message = "Failure")})								
	public Response getNews(@Context final HttpServletRequest httpServletRequest)
			throws JSONException, URISyntaxException {
		LOGGER.info("api/news invoked");

		API api = API.getSingleton();
		Gson gson = new Gson();
		net.bluemix.newsaggregator.api.ResponseNewsDTO apiResponse = new net.bluemix.newsaggregator.api.ResponseNewsDTO();

		try {
			List<NewsEntry> newsEntries = api
					.getCuratedNewsEntriesLastMonthByCurationDate(50);
			if (newsEntries == null) return Response.status(500).build();
			List<NewsEntry> incomingEntries = api
					.getIncomingNewsEntriesLastMonthByCreationDate(50);
			if (incomingEntries == null) return Response.status(500).build();
			List<Person> persons = api.getAllPersons();
			if (persons == null) return Response.status(500).build();
			List<NewsEntry> topStories = api.getTopStories();
			if (topStories == null) return Response.status(500).build();

			if (AuthenticationServlet.checkAuthorization(httpServletRequest) == false) {
				apiResponse.setCurrentUserCurator(false);
			}
			else {
				apiResponse.setCurrentUserCurator(true);
			}
					
			List<EntryDTO> entries = new ArrayList<EntryDTO>();
			if (newsEntries != null) {
				for (int i = 0; i < newsEntries.size(); i++) {
					entries.add(new EntryDTO(newsEntries.get(i), getPersonById(
							persons, newsEntries.get(i).getPersonId())));
				}
			}

			List<EntryDTO> topStoriesEntries = new ArrayList<EntryDTO>();
			if (topStories != null) {
				for (int i = 0; i < topStories.size(); i++) {
					topStoriesEntries.add(new EntryDTO(topStories.get(i),
							getPersonById(persons, topStories.get(i)
									.getPersonId())));
				}
			}

			List<EntryDTO> newEntries = new ArrayList<EntryDTO>();
			if (incomingEntries != null) {
				for (int i = 0; i < incomingEntries.size(); i++) {
					newEntries.add(new EntryDTO(incomingEntries.get(i),
							getPersonById(persons, incomingEntries.get(i)
									.getPersonId())));
				}
			}

			apiResponse.setCuratedEntries(entries);
			apiResponse.setIncomingEntries(newEntries);
			apiResponse.setTopStories(topStoriesEntries);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).build();
		}

		return Response.ok(gson.toJson(apiResponse)).build();
	}
	
	private Person getPersonById(List<Person> persons, String id) {
		if (persons != null) {
			for (int i = 0; i < persons.size(); i++) {
				if (persons.get(i).getId().equalsIgnoreCase(id)) {
					return persons.get(i);
				}
			}
		}
		
		Person output = new Person();
		output.setDisplayName("Unknown");
		output.setPictureURL("head.png");
		output.setTwitter("");
		return output;
	}	
}