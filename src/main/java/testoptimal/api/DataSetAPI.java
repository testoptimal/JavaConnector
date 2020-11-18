package testoptimal.api;

import com.google.gson.Gson;

import testoptimal.api.COMB.DataSet;
import testoptimal.api.COMB.DataSetReq;

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
public class DataSetAPI {
	private Server svr;
	private Gson gson = new Gson();

	protected DataSetAPI (Server svr_p) {
		this.svr = svr_p;
	}	

	/**
	 * uploads a dataset to TestOptimal server.
	 * @param ds_p DataSet object
	 * @throws APIError on any error
	 */
	public void upload (DataSet ds_p) throws APIError {
		String dsJson = this.gson.toJson(ds_p);
		this.svr.sendPut("datadesign", "dataset/" + ds_p.getName(), dsJson, 200);
	}

	/**
	 * generate data table for a data set (uploaded).
	 * @param dsName_p data set name
	 * @param alg_p algorithm to be used to generate data table: pairWise, threeWise, fourWise, fiveWise, sixWise, mixed
	 * @return RunResult containing the generated test cases.
	 * @throws APIError on any error
	 */
	public RunResult generate (String dsName_p, Constants.Algorithm alg_p) throws APIError {
		DataSetReq req = new DataSetReq ();
		req.dsName = dsName_p;
		req.path = "client";
		req.algorithm = alg_p;
		String reqJson = this.gson.toJson(req);
		String retJson = this.svr.sendPost("client", "dataset/gen", reqJson, 200);
		RunResult result = gson.fromJson(retJson, RunResult.class);
		return result;
	}

}
