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
import javax.ws.rs.FormParam;
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
import net.bluemix.newsaggregator.TwitterUtilities;

import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Api(value = "api/approvenewsentry")
@Path("approvenewsentry")
public final class RestAPIApproveNewsEntry {

	private static final Logger LOGGER = Logger.getLogger(RestAPIApproveNewsEntry.class
			.getName());

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ApiOperation(value = "Approve news entry", 
	protocols = "https",
	notes = "Approve a new news entry. Note: You have to be a curator to invoke this API.", 
	position = 4, 
	httpMethod = "Post", nickname = "Approve news entry")		
	@ApiImplicitParams(value = { 
			@ApiImplicitParam( name = "newsEntryId", paramType = "form", dataType = "java.lang.String", required = true, 
					defaultValue = "...",
					value = "News Entry Id" ),
			@ApiImplicitParam(name = "tweet", paramType = "form", dataType = "java.lang.String", required = false, 
			defaultValue = "false", allowableValues = "false,true",
			value = "true or false")
	})
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "Success", response = ResponseApproveNewsEntryDTO.class), 
	@ApiResponse(code = 500, message = "Failure"),
	@ApiResponse(code = 401, message = "Unauthorized") })
	public Response addEntry(@FormParam("newsEntryId") String newsEntryId,
			@FormParam("tweet") String tweet,
			@Context final HttpServletRequest httpServletRequest) throws JSONException,
			URISyntaxException {
		LOGGER.info("api/approvenewsentry invoked");
		
		Gson gson = new Gson();
		ResponseApproveNewsEntryDTO apiResponse = new ResponseApproveNewsEntryDTO();
		apiResponse.setNewsEntryId(newsEntryId);

		if (AuthenticationServlet.checkAuthorization(httpServletRequest) == false) {
			return Response.status(401).build();
		}		
		
		API api = API.getSingleton();
		String curator = (String)httpServletRequest.getSession().getAttribute("userDisplayName");
		
		try {			
			String output = api.approveNewsEntry(newsEntryId, curator);
			if (output == null) {
				return Response.status(500).build();				
			}
			else {
				if (tweet != null) {
					if (tweet.equalsIgnoreCase("true")) {
						String twitterOutput = TwitterUtilities.getSingleton().tweetNewsEntry(output);
						apiResponse.setTweetUrl(twitterOutput);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
				return Response.status(500).build();
		}

		return Response.ok(gson.toJson(apiResponse)).build();
	} 
}