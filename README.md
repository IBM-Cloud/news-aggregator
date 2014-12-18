news-aggregator
================================================================================

[Bluemix.info](https://www.bluemix.info/) is a news aggregator for IBM Bluemix developers and professionals covering news about everything related to IBM's platform as a service, including runtimes, services, events and much more. Bluemix.info reads information from various sources via feeds. However curators need to approve content first before it shows up on Bluemix.info. Curators can also add entries manually.  

This project contains the source code of Bluemix.info which can easily also be used as news aggregator for other types of information.

The project is a good showcase of Bluemix functionality and services: 
* [Liberty for Java runtime](https://www.ng.bluemix.net/docs/#starters/liberty/index.html#liberty)
* [Bluemix Cloudant service](https://www.ng.bluemix.net/docs/#services/Cloudant/index.html#Cloudant)
* [Bluemix single sign on service](https://www.ng.bluemix.net/docs/#services/SingleSignOn/index.html#sso_gettingstarted) 
* [Bluemix data cache service](https://www.ng.bluemix.net/docs/#services/DataCache/index.html#data_cache)
* [Bluemix session cache service](https://www.ng.bluemix.net/docs/#services/SessionCache/index.html#session_cache) 
* [User provided service](http://docs.cloudfoundry.org/devguide/services/user-provided.html)
* [Bluemix workload scheduler](https://www.ng.bluemix.net/docs/#services/WorkloadScheduler/index.html#gettingstarted)
* Plus other [open source libraries](https://github.com/IBM-Bluemix/news-aggregator/blob/master/NOTICE)

Authors: 
* Niklas Heidloff [@nheidloff](http://twitter.com/nheidloff)
* Ryan Baxter [@ryanjbaxter](https://twitter.com/ryanjbaxter)
* Stephan Wissel [@notessensei](https://twitter.com/notessensei)


Setup of Eclipse IDE, Liberty and Application
----------------------------------------------------------------------------------

*Install Eclipse, the Java Runtime and Maven*

[Install Eclipse IDE for Java Developers](http://www.eclipse.org/downloads/)

[Find out more about Eclipse and Java Runtime installation](https://wiki.eclipse.org/Eclipse/Installation)

[Install the Eclipse Maven plugin (M2Eclipse)](http://eclipse.org/m2e/)

 
*Install Liberty profile*
 
[Download and install Liberty profile](https://developer.ibm.com/wasdev/downloads/liberty-profile-using-eclipse)

[Find out more about Liberty](https://www.ng.bluemix.net/docs/#starters/liberty/index.html) 


*Get the Application Code from GitHub*

You can either download the zip file or clone the project from Eclipse. Then import as Maven project. 

* [Git url](https://github.com/IBM-Bluemix/news-aggregator.git)
* [Zip file](https://github.com/IBM-Bluemix/news-aggregator/archive/master.zip)
* [Project](https://github.com/IBM-Bluemix/news-aggregator)


*Install WebSphere eXtreme Scale*

In order to compile the code locally the project pulls in almost all dependencies via Maven. 

The only exception is the library com.ibm.ws.xs.client_1.1.jar which is needed for the data cache service.
In order to get this file you need to download and install "WebSphere eXtreme Scale for Developers Liberty Profile" and link to it in your project settings. To install this product, run the command: 
> java -jar wxs-wlp_8.6.0.5.jar

[Download and install WebSphere eXtreme Scale](https://developer.ibm.com/wasdev/downloads/#asset/addons-wxs)

Under Project Properties - Java Build Path - Libraries change the link of this file:
* com.ibm.ws.xs.client_1.1.jar (\liberty\dev\ibm-api)


*Install Client Libraries for Workload Scheduler*

[Download client libraries](https://start.wa.ibmserviceengage.com/bluemix/ClientLibraries_java.zip)

Import the three jar files into the directory src/main/webapp/WEB-INF/lib/ (create lib if it doesn't exist):
* twaclient.jar
* tdwcsimpleui_public.jar
* tdwccronparser.jar

In your project properties under "Java Build Path" add the three libraries.


*Configure the Liberty server (server.xml)*

       <server description="new server">
           <featureManager>
               <feature>jsp-2.2</feature>
               <feature>localConnector-1.0</feature>
               <feature>ssl-1.0</feature>
               <feature>appSecurity-2.0</feature>
               <feature>jaxrs-1.1</feature>
           </featureManager>
           <httpEndpoint httpPort="9080" httpsPort="9443" id="defaultHttpEndpoint"/>
           <applicationMonitor updateTrigger="mbean"/>
           <sslDefault/>    
           <keyStore id="defaultKeyStore" password="yourPassword"/>
           <webApplication id="news-aggregator" 
        	   location="news-aggregator.war" 
        	   name="news-aggregator"/>
       </server>


Setup of Bluemix Application and Services
----------------------------------------------------------------------------------

To run the application on Bluemix you need to create an application and create and bind services. 

[Sign up for Bluemix](https://apps.admin.ibmcloud.com/manage/trial/bluemix.html)

[Install the Cloud Foundry command line tool](https://github.com/cloudfoundry/cli#downloads)


Choose Liberty as runtime:
* [Liberty for Java runtime](https://www.ng.bluemix.net/docs/#starters/liberty/index.html#liberty)

Then add the following services:
* [Bluemix Cloudant service](https://www.ng.bluemix.net/docs/#services/Cloudant/index.html#Cloudant)
* [Bluemix single sign on service](https://www.ng.bluemix.net/docs/#services/SingleSignOn/index.html#sso_gettingstarted) 
* [Bluemix data cache service](https://www.ng.bluemix.net/docs/#services/DataCache/index.html#data_cache)
* [Bluemix session cache service](https://www.ng.bluemix.net/docs/#services/SessionCache/index.html#session_cache) 
* [Bluemix workload scheduler](https://www.ng.bluemix.net/docs/#services/WorkloadScheduler/index.html#gettingstarted)


*Configure the Application and Services*

When running on a server the application picks up the configuration from (most of) these services automatically. However some manual steps need to be done.

In order to use the single sign on service, you need to create a 'Single Sign On Client Configuration' in the Bluemix dashboard.
The redirect URL needs to point to '/logon' e.g. https://www.bluemix.info/logon. You need to copy the generated client id and secret.

In order to pass additional configuration to the application running on Bluemix an [user provided service](http://docs.cloudfoundry.org/devguide/services/user-provided.html) is used.

> cf cups news-aggregator-config -p "NA_SSO_REDIRECTURI, NA_SSO_CLIENT_IDENTIFIER, NA_SSO_CLIENT_SECRET, NA_CURATORS, NA_TW_ACCESS_TOKEN_SECRET, NA_TW_ACCESS_TOKEN, NA_TW_CONSUMER_SECRET, NA_TW_CONSUMER_KEY"

Single sign on service:
* NA_SSO_REDIRECTURI 'your redirect url' e.g. 'https://www.bluemix.info/logon'
* NA_SSO_CLIENT_IDENTIFIER 'your client id' e.g. 'nhMFIsHGMXuHAhaUCcZT'
* NA_SSO_CLIENT_SECRET 'your client secret' e.g. 'nhOIysMpRzQkiXzUXAfs'

List of curators:
* NA_CURATORS 'your comma separated list' e.g. 'http://www.ibm.com/niklas_heidloff@company.com,http://www.ibm.com/rjbaxter@company.com'

Twitter configuration (you need to create a [Twitter application](https://apps.twitter.com/app/new))
* NA_TW_ACCESS_TOKEN_SECRET 'your access token secret' e.g. 'nhwcbJKRTE8Lf2ZJx7wpo8CIWXNJPOykRTxNhZH22W01Y'
* NA_TW_ACCESS_TOKEN 'your acess token' e.g. 'nh81184955-IH6g6NqA5s8apQs2q6DeqFcm9pUvNii7Du47qqP'
* NA_TW_CONSUMER_SECRET 'your consumer secret' e.g. 'nhcl3Uw7bm4qNhUpZ09wuEb8R7jykia9DKTmI2yMQUbczWCXlN'
* NA_TW_CONSUMER_KEY 'your consumer key' e.g. '8sq7PX3D5pwl7vPmAooPcD7fd'


Run the Application locally
----------------------------------------------------------------------------------

The application can be run locally so that it can be tested and debugged before changes are deployed to Bluemix. 
When running locally no authentication and authorization check is done and the data cache service is not used.

To run it locally the following environment variables need to be set.

* NA_LOCAL true

Cloudant service (copy from Bluemix dashboard)
* NA_DB_HOST 'your cloudant host' e.g. '1234567890-bluemix.cloudant.com'
* NA_DB_PASSWORD 'your cloudant password' e.g. 'adfadsfa0b4d208e0e2452180e0db4132f3639bd8bbdae17355efc7eb24b68ae2ec'
* NA_DB_USERNAME 'your cloudant username' e.g. 'adfadsfdf984-bluemix'

Twitter configuration (you need to create a [Twitter application](https://apps.twitter.com/app/new))
* NA_TW_ACCESS_TOKEN_SECRET 'your access token secret' e.g. 'nhwcbJKRTE8Lf2ZJx7wpo8CIWXNJPOykRTxNhZH22W01Y'
* NA_TW_ACCESS_TOKEN 'your acess token' e.g. 'nh81184955-IH6g6NqA5s8apQs2q6DeqFcm9pUvNii7Du47qqP'
* NA_TW_CONSUMER_SECRET 'your consumer secret' e.g. 'nhcl3Uw7bm4qNhUpZ09wuEb8R7jykia9DKTmI2yMQUbczWCXlN'
* NA_TW_CONSUMER_KEY 'your consumer key' e.g. '8sq7PX3D5pwl7vPmAooPcD7fd'

After you've set these variables you can run the application:

[Local Liberty Home](https://localhost:9443/news-aggregator)

[Local Liberty API](https://localhost:9443/news-aggregator/swagger/index.html)


Run the Application on Bluemix
----------------------------------------------------------------------------------

Follow the following steps to deploy the application to Bluemix.


*Build the sample (war file)*

The sample is a Maven project. To generate the war file select the project in the Eclipse explorer and choose export war file.

Alternatively you can use the external build tool [Maven](http://maven.apache.org/download.cgi). To build the app change to the project's directory in a command line window and run ...
> mvn


*Push app to Bluemix*

In a command prompt run the following commands the same directory that contains the war file.

> cf api https://api.ng.bluemix.net

> cf login

You need to use your IBM id and password (Bluemix credentials)
 
> cf push [yourappname] -p [news-aggregator.war] -m 512M

* [yourappname] - this is the name of your Bluemix app. needs to be unique
* [news-aggregator.war] - name of the exported/built war file

Alternatively you can use the [Cloud Foundry Maven Plugin](https://github.com/cloudfoundry/cf-java-client/tree/master/cloudfoundry-maven-plugin) to build and push in one step. In this case you can deploy your application simply via ...
> mvn -P deploy

*Run the Application on Bluemix*

Invoke the following URLs:
* Home: http://[yourappname].mybluemix.net
* API: https://[yourappname].mybluemix.net/swagger/index.html
* Curation: https://[yourappname].mybluemix.net/logon