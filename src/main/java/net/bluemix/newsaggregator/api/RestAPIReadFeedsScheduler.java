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

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.bluemix.newsaggregator.feeds.FeedManager;
import net.bluemix.newsaggregator.feeds.SchedulerData;
import net.bluemix.newsaggregator.feeds.SchedulerUtilities;

import org.apache.commons.codec.binary.Base64;
import org.apache.wink.json4j.JSONException;

import com.google.gson.Gson;

import com.ibm.twa.applab.client.WorkloadService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Api(value = "api/readfeedsscheduler")
@Path("readfeedsscheduler")
public final class RestAPIReadFeedsScheduler {

	private static final Logger LOGGER = Logger
			.getLogger(RestAPIReadFeedsScheduler.class.getName());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Read feeds on scheduled basis", protocols = "https", notes = "Read new news entries from registered feeds (and update in memory cache). Invoked by the workload scheduler. Note: You need application credentials and to use basic authentication.", position = 6, nickname = "Read feeds on scheduled basis", httpMethod = "Get")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = ResponseReadFeedsDTO.class),
			@ApiResponse(code = 500, message = "Failure"),
			@ApiResponse(code = 401, message = "Unauthorized") })
	public Response getNews(@Context final HttpServletRequest httpServletRequest)
			throws JSONException, URISyntaxException {
		LOGGER.info("api/readfeedsscheduler invoked");
		
		FeedManager fm = FeedManager.getSingleton();
		Gson gson = new Gson();
		ResponseReadFeedsDTO apiResponse = new ResponseReadFeedsDTO();

		String userName = null;
		String password = null;
		String authHeader = httpServletRequest.getHeader("Authorization");
		if (authHeader != null) {
			StringTokenizer tokenizer = new StringTokenizer(authHeader);
			if (tokenizer.hasMoreTokens()) {
				String basic = tokenizer.nextToken();

				if (basic.equalsIgnoreCase("Basic")) {
					try {
						String credentials = new String(
								Base64.decodeBase64(tokenizer.nextToken()),
								"UTF-8");
						int index = credentials.indexOf(":");
						if (index != -1) {
							userName = credentials.substring(0, index).trim();
							password = credentials.substring(index + 1).trim();
						} else {
							return Response.status(401).build();
						}
					} catch (UnsupportedEncodingException e) {
						return Response.status(401).build();
					}
				}
			}
		}
		boolean isAuthorized = false;

		SchedulerData data = SchedulerUtilities.getSingleton().getData();
		if (data == null) {
			return Response.status(500).build();
		}

		if (
				(userName != null) 
				&& (password != null)
				&& (userName.equalsIgnoreCase(data.getUserName()))
				&& (password.equalsIgnoreCase(data.getPassword()))) {
			isAuthorized = true;
		}
		
		if (isAuthorized == false) {
			LOGGER.info("api/readfeedsscheduler invoked - not authorized");
			return Response.status(401).build();
		} 
		else {
			LOGGER.info("api/readfeedsscheduler invoked - authorized");
		}

		try {
			LOGGER.info("BluemixInfo Scheduler start");
			fm.readFeeds();
			LOGGER.info("BluemixInfo Scheduler stop");
			apiResponse.setMessage("Feeds have been read");

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).build();
		}

		return Response.ok(gson.toJson(apiResponse)).build();
	}
}