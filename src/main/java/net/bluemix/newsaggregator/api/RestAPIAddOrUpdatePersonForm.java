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
import javax.ws.rs.PathParam;
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

@Api(value = "api/addorupdateauthor")
@Path("addorupdateauthor")
public final class RestAPIAddOrUpdatePersonForm {

	private static final Logger LOGGER = Logger.getLogger(RestAPIAddOrUpdatePersonForm.class
			.getName());

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ApiOperation(value = "Add or update an author", 
	protocols = "https",
	notes = "Adds a new author. If the author exists, the picture URL and the Twitter name are overwritten. Note: You have to be a curator to invoke this API.", 
	position = 2, 
	httpMethod = "Post", nickname = "Add or update an author")	
	@ApiImplicitParams(value = { 
			@ApiImplicitParam( name = "displayName", paramType = "form", dataType = "java.lang.String", required = true, 
					defaultValue = "Test User",
					value = "Display Name"),
			@ApiImplicitParam(name = "pictureUrl", paramType = "form", dataType = "java.lang.String", required = false, 
			defaultValue = "http://www.bluemix.info/head.png",
			value = "Picture URL"),
			@ApiImplicitParam(name = "twitter", paramType = "form", dataType = "java.lang.String", required = false, 
			defaultValue = "@twittername",
			value = "Twitter Name (with @)")		
	})
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "Success", response = ResponseAddOrUpdateAuthorDTO.class), 
	@ApiResponse(code = 500, message = "Failure"),
	@ApiResponse(code = 401, message = "Unauthorized") })
	public Response addPerson2(@FormParam("displayName") String displayName, 
			@FormParam("pictureUrl") String pictureUrl,
			@FormParam("twitter") String twitter,
			@Context final HttpServletRequest httpServletRequest)
			throws JSONException, URISyntaxException {
		LOGGER.info("api/addorupdateauthor invoked");

		API api = API.getSingleton();
		ResponseAddOrUpdateAuthorDTO apiResponse = new ResponseAddOrUpdateAuthorDTO();		
		
		AuthorDTO person = new AuthorDTO();
		person.setDisplayName(displayName);
		person.setPictureUrl(pictureUrl);
		person.setTwitter(twitter);		
		
		if (AuthenticationServlet.checkAuthorization(httpServletRequest) == false) {	
			return Response.status(401).build();
		}			

		try {			
			String output = api.addOrUpdatePerson(person);
			if (output == null) {
				return Response.status(500).build();
			}
			else {
				apiResponse.setAuthorId(output);					
			}
		} catch (Exception e) {
			e.printStackTrace();
				return Response.status(500).build();
		}

		Gson gson = new Gson();
		return Response.ok(gson.toJson(apiResponse)).build();
	}
}