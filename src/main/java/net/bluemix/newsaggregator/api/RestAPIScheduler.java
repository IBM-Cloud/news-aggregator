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

import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.bluemix.newsaggregator.feeds.SchedulerUtilities;

import org.apache.wink.json4j.JSONException;

import com.google.gson.Gson;
// TODO add again once workload scheduler libs are on Maven 
//import com.ibm.twa.applab.client.WorkloadService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Api(value = "api/scheduler")
@Path("scheduler")
public final class RestAPIScheduler {

	private static final Logger LOGGER = Logger
			.getLogger(RestAPIScheduler.class.getName());

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Turn scheduler on or off", protocols = "https", 
	notes = "Turn scheduler on or off. Note: You need to be curator.", 
	position = 6, nickname = "Turn scheduler on or off", httpMethod = "Post")
	@ApiImplicitParams(value = { 			
			@ApiImplicitParam(name = "state", paramType = "form", dataType = "java.lang.String", required = true, 
			defaultValue = "off", allowableValues = "on,off",
			value = "on or off")
	})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = ResponseSchedulerDTO.class),
			@ApiResponse(code = 500, message = "Failure"),
			@ApiResponse(code = 401, message = "Unauthorized") })
	public Response getNews(@FormParam("state") String state,
			@Context final HttpServletRequest httpServletRequest)
			throws JSONException, URISyntaxException {
		LOGGER.info("api/scheduler invoked");

		Gson gson = new Gson();
		ResponseSchedulerDTO apiResponse = new ResponseSchedulerDTO();

		if (AuthenticationServlet.checkAuthorization(httpServletRequest) == false) {
			return Response.status(401).build();
		}	

		try {
			boolean on = false;
			String output = null;
			if (state != null) {
				if (state.equalsIgnoreCase("on")) on = true;
			}
			
			// TODO add again once workload scheduler libs are on Maven
			/*
			WorkloadService w = SchedulerUtilities.getSingleton()
					.getWorkloadService();
			if (w == null) {
				return Response.status(500).build();
			}
			if (on == true) {
				output = SchedulerUtilities.getSingleton().turnOn();	
			}
			else {
				output = SchedulerUtilities.getSingleton().turnOff();
			}
			*/
			if (output == null) {
				apiResponse.setMessage("Scheduler state couldn't be changed");	
			}
			else {
				apiResponse.setMessage(output);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).build();
		}

		return Response.ok(gson.toJson(apiResponse)).build();
	}
}