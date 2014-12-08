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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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

@Api(value = "api/curatednews")
@Path("curatednews")
public final class RestAPIGetCuratedNews {

	private static final Logger LOGGER = Logger.getLogger(RestAPIGetCuratedNews.class
			.getName());
	
	public static final String FORMAT_JSON = "json";
	public static final String FORMAT_JSONP = "jsonp";
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get curated news", 
	protocols = "http, https",
	notes = "Get curated news entries. Can be invoked cross domain (CORS).", position = 1, 
	nickname = "Get curated news", httpMethod = "Get")	
	@ApiImplicitParams(value = { 
			@ApiImplicitParam( name = "format", paramType = "query", dataType = "java.lang.String", required = false, 
					defaultValue = "json", allowableValues = "json,jsonp",
					value = "json or jsonp" )	
	})
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "Success", response = ResponseCuratedNewsDTO.class),
	@ApiResponse(code = 500, message = "Failure")})								
	public Response getNews(@QueryParam("format") String format,
			@Context final HttpServletRequest httpServletRequest)
			throws JSONException, URISyntaxException {
		LOGGER.info("api/curatednews invoked");
		String output = "";
		
		if (format == null) format = FORMAT_JSON;
		if (format.equalsIgnoreCase(FORMAT_JSONP)) {
			format = FORMAT_JSONP;
		}
		else {
			format = FORMAT_JSON;
		}

		API api = API.getSingleton();
		Gson gson = new Gson();
		net.bluemix.newsaggregator.api.ResponseCuratedNewsDTO apiResponse = new net.bluemix.newsaggregator.api.ResponseCuratedNewsDTO();

		try {
			List<NewsEntry> newsEntries = api
					.getCuratedNewsEntriesLastMonthByCurationDate(50);
			if (newsEntries == null) return Response.status(500).build();
			List<Person> persons = api.getAllPersons();
			if (persons == null) return Response.status(500).build();
					
			List<EntryDTO> entries = new ArrayList<EntryDTO>();
			if (newsEntries != null) {
				for (int i = 0; i < newsEntries.size(); i++) {
					entries.add(new EntryDTO(newsEntries.get(i), getPersonById(
							persons, newsEntries.get(i).getPersonId())));
				}
			}
			apiResponse.setCuratedEntries(entries);
			output = gson.toJson(apiResponse);
			if (format.equalsIgnoreCase(FORMAT_JSONP)) {
				output = "jsonCallback ( { 'results': "
						+ output
						+ " } );";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).build();
		}
		
		String requestHeader = httpServletRequest.getHeader("Access-Control-Request-Headers");
		if ((requestHeader != null) && (!requestHeader.equalsIgnoreCase(""))) {
			return Response.ok(output)
					.header("Access-Control-Allow-Origin", "*")
			        .header("Access-Control-Allow-Methods", "GET")
			        .header("Access-Control-Allow-Headers", requestHeader)
			        .build();
		}
		else {
			return Response.ok(output)
					.header("Access-Control-Allow-Origin", "*")
			        .header("Access-Control-Allow-Methods", "GET")
			        .build();
		}
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