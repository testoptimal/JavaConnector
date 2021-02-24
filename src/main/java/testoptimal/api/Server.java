package testoptimal.api;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Copyright 2020 TestOptimal LLC
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Yaxiong Lin
 *
 */
public class Server {
	public static enum Protocol {http, https};
	
	private Protocol protocol;
	private String svrHost;
	private int svrPort;
	private String username;
	private String password;
	private String svrURL;
	private String apiURL;
	
	private DataSetAPI dataSetAPI = new DataSetAPI (this);
	private ModelAPI modelAPI = new ModelAPI (this);
	private AgentAPI agentAPI = new AgentAPI (this);
	
	/**
	 * Provides a set of APIs to access TestOptimal server.
	 * @param protocol_p http or https
	 * @param svrHost_p TestOptimal server host name
	 * @param svrPort_p TestOptimal server port number
	 * @param username_p user id to login to TestOptimal server (same user id you would use to open TestOptimal IDE)
	 * @param password_p password
	 */
	public Server (Protocol protocol_p, String svrHost_p, int svrPort_p, String username_p, String password_p) {
		this.protocol = protocol_p;
		this.svrHost = svrHost_p;
		this.svrPort = svrPort_p;
		this.username = username_p;
		this.password = password_p;
		
		this.svrURL = this.protocol + "://" + this.svrHost + ":" + this.svrPort + "/";
		this.apiURL = this.svrURL + "api/v1/";
	}
	
	protected String sendGet(String facilityName_p, String cmd_p,
			String param_p, int expectedStatusCode_p) throws APIError {
		if (param_p == null)
			param_p = "";
		String url = this.apiURL + facilityName_p + "/" + cmd_p;
		if (param_p != null && !param_p.contentEquals("")) {
			url += "?" + param_p;
		}
		RequestSpecification req = RestAssured.given();
		req.auth().preemptive().basic(this.username, this.password);
		Response resp = req.get(url);
		if (expectedStatusCode_p > 0 && resp.getStatusCode()!=expectedStatusCode_p) {
			throw genApiError (resp.getStatusCode(), resp.asString(), url);
		}
		String respString = resp.asString();
		return respString;
	}

	private APIError genApiError (int status_p, String message_p, String url_p) {
		APIError err = null;
		try {
			Gson gson = new Gson();
			err = gson.fromJson(message_p, err.getClass());
		}
		catch (Exception e) {
			err = new APIError (status_p, "Error", message_p, url_p);
		}
		return err;
	}

	protected String sendPost (String facilityName_p, String cmd_p,
			String body_p, int expectedStatusCode_p) throws APIError {
		String url = this.apiURL + facilityName_p + "/" + cmd_p;
		RequestSpecification req = RestAssured.given();
		req.auth().preemptive().basic(this.username, this.password);
		req.body(body_p);
		req.contentType("application/json");
		Response resp = req.post(url);
		if (expectedStatusCode_p > 0 && resp.getStatusCode()!=expectedStatusCode_p) {
			throw genApiError (resp.getStatusCode(), resp.asString(), url);
		}
		String respString = resp.asString();
		return respString;
	}

	protected String sendPut (String facilityName_p, String cmd_p,
			String body_p, int expectedStatusCode_p) throws APIError {
		String url = this.apiURL + facilityName_p + "/" + cmd_p;
		RequestSpecification req = RestAssured.given();
		req.auth().preemptive().basic(this.username, this.password);
		req.body(body_p);
		req.contentType("application/json");
		Response resp = req.put(url);
		if (expectedStatusCode_p > 0 && resp.getStatusCode()!=expectedStatusCode_p) {
			throw genApiError (resp.getStatusCode(), resp.asString(), url);
		}
		String respString = resp.asString();
		return respString;
	}

	protected String getGraph (String modelName_p, String graphType_p, String outFilePath_p) throws APIError {
		String url = this.apiURL + "graph/" + modelName_p + "/" + graphType_p + "?execID=-1";
		RequestSpecification req = RestAssured.given();
		req.auth().preemptive().basic(this.username, this.password);
		
		Response resp = req.get(url);
		if (resp.getStatusCode()!=200) {
			throw genApiError (resp.getStatusCode(), resp.asString(), url);
		}

		try (OutputStream outStream = new FileOutputStream(outFilePath_p)) {
			byte[] image = resp.asByteArray();
	        outStream.write(image);
		}
		catch (Exception e) {
			throw new APIError(-1, "api", e.getLocalizedMessage(), url);
		}
		
		File f = new File (outFilePath_p);
		return f.getAbsolutePath();
	}
	
	public DataSetAPI getDataSetAPI () {
		return dataSetAPI;
	}

	public ModelAPI getModelAPI () {
		return modelAPI;
	}
	
	public AgentAPI getAgentAPI () {
		return agentAPI;
	}
}
