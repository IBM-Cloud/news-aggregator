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

import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;

import com.google.gson.Gson;


public class AuthenticationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AuthenticationServlet() {
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String SSO_CLIENT_ID = ConfigUtilities.getSingleton().getSSOClientIdentifier();
		String SSO_CLIENT_SECRET = ConfigUtilities.getSingleton().getSSOClientSecret();
		String SSO_REDIRECT = ConfigUtilities.getSingleton().getSSORedirectUri();
		//String SSO_REDIRECT = "https://localhost:9443/news-aggregator/logon";

		try {
			String code = request.getParameter("code");

			if (code != null && code.length() > 0) {
				String state = request.getParameter("state");
				String stateSession = (String)request.getSession().getAttribute("state");
				if (state == null && state.length() < 1) {
					response.sendRedirect("curation.html?userName=NotAuthenticated");
				}
				if (!state.equalsIgnoreCase(stateSession))
					response.sendRedirect("curation.html?userName=NotAuthenticated");

				String accessToken = getAccessTokenFromCodeResponse(
						SSO_CLIENT_ID, SSO_CLIENT_SECRET, SSO_REDIRECT, code);

				String userDisplayName = "";
				String userName = "";

				if (accessToken != null) {
					User user = getProfileWithAccessToken(accessToken);
					userName = user.getUsername();
					request.getSession().setAttribute("userName", userName);

					userDisplayName = user.getUserDisplayName()[0];
					request.getSession().setAttribute("userDisplayName",
								userDisplayName);
				}

				response.sendRedirect("swagger/index.html");
			} else {
				String state = UUID.randomUUID().toString();
				request.getSession().setAttribute("state", state);
				String authorizeURL = "https://idaas.ng.bluemix.net/sps/oauth20sp/oauth20/authorize"
						+ "?client_id="
						+ SSO_CLIENT_ID
						+ "&response_type=code&scope=profile"
						+ "&redirect_uri="
						+ SSO_REDIRECT
						+ "&state="
						+ state
						+ "&requestedAuthnPolicy=http://www.ibm.com/idaas/authnpolicy/basic";

				response.sendRedirect(authorizeURL);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	static public void configureSSL() {
		// note that it's not adviced to use this in a production application
		// you should overwrite the X509TrustManager to use a cacerts file (list of trusted signers) 
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL_TLS");

			sslContext.init(null, new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs,
						String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs,
						String authType) {
				}
			} }, new SecureRandom());

			Executor.unregisterScheme("https");
			SSLSocketFactory sslSocketFactory = new SSLSocketFactory(
					sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			Executor.registerScheme(new Scheme("https", 443, sslSocketFactory));

			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
					.getSocketFactory());
		
		} catch (KeyManagementException
				| NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	private String getAccessTokenFromCodeResponse(String id, String secret,
			String redirect, String code) {
		String output = null;
		try {
			configureSSL();			

			org.apache.http.client.fluent.Request req = Request
					.Post("https://idaas.ng.bluemix.net/sps/oauth20sp/oauth20/token");
			String body = "client_secret=" + secret
					+ "&grant_type=authorization_code" + "&redirect_uri="
					+ redirect + "&code=" + code + "&client_id=" + id;

			req.bodyString(body,
					ContentType.create("application/x-www-form-urlencoded"));

			org.apache.http.client.fluent.Response res = req.execute();

			output = res.returnContent().asString();
			
			output = output.substring(output.indexOf("access_token") + 15, output.indexOf("access_token") + 35);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}

	private User getProfileWithAccessToken(String accessToken) {
		User result = null;
		String output = null;
		
		try {
			configureSSL();			

			org.apache.http.client.fluent.Request req = Request
					.Get("https://idaas.ng.bluemix.net/idaas/resources/profile.jsp");
			
			req.addHeader("Authorization", "bearer " + accessToken);

			org.apache.http.client.fluent.Response res = req.execute();

			output = res.returnContent().asString();
			
			Gson gson = new Gson();
			result = gson.fromJson(output, User.class);   				
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	static public boolean checkAuthorization(HttpServletRequest httpServletRequest) {

		try {
			String value = System.getenv("NA_LOCAL");
            if ((value != null) && (!value.equalsIgnoreCase(""))) return true;				
			
			String userName = (String)httpServletRequest.getSession().getAttribute("userName");
			if (userName == null) return false;
			if (userName.equalsIgnoreCase("")) return false;
			
			String[] curators = ConfigUtilities.getSingleton().getCurators();
			if (curators == null) return false;
			
			for (int i = 0; i < curators.length; i++) {
				if (curators[i].equalsIgnoreCase(userName)) return true;
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}
