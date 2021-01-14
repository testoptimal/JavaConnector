package testoptimal.api;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import testoptimal.api.Constants.GraphType;
import testoptimal.api.Constants.MbtMode;
import testoptimal.api.FSM.Model;
import testoptimal.api.FSM.ModelRequest;
import testoptimal.api.FSM.Trans;

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
public class ModelAPI {
	private Server svr;
	private Gson gson = new Gson();

	protected ModelAPI (Server svr_p) {
		this.svr = svr_p;
	}
	
	/**
	 * uploads a model to TestOptimal server.
	 * @param model_p model name
	 * @throws APIError on any error
	 */
	public void upload (Model model_p) throws APIError {
		String modelJson = this.gson.toJson(model_p);
		this.svr.sendPost("client", "model/upload", modelJson, 200);
	}

	/**
	 * generate test paths (test cases) for a model (uploaded) using the default Optimal sequencer.
	 * @param modelName_p name of the model
	 * @return RunResult that contains the test paths and test steps.
	 * @throws APIError on any error
	 */
	public RunResult genPaths (String modelName_p) throws APIError {
		return this.genPaths (modelName_p, MbtMode.Optimal, 0);
	}

	/**
	 * generate test paths (test cases) for a model (uploaded).
	 * @param modelName_p name of the model
	 * @param mbtMode_p choose one of the model sequencers: Optimal, Priority, Pairwise, Random.
	 * @return RunResult that contains the test paths and test steps.
	 * @throws APIError on any error
	 */
	public RunResult genPaths (String modelName_p, MbtMode mbtMode_p) throws APIError {
		return this.genPaths (modelName_p, mbtMode_p, 0);
	}
	
	/**
	 * generate test paths (test cases) for a model (uploaded).
	 * @param modelName_p name of the model
	 * @param mbtMode_p choose one of the model sequencers: Optimal, Priority, Pairwise, Random.
	 * @param timeoutMillis_p # of milliseconds to wait for the generation to complete
	 * @return RunResult that contains the test paths and test steps.
	 * @throws APIError on any error
	 */
	public RunResult genPaths (String modelName_p, MbtMode mbtMode_p, int timeoutMillis_p) throws APIError {
		ModelRequest req = new ModelRequest();
		if (timeoutMillis_p>0) {
			req.options.put("timeoutMillis", timeoutMillis_p);
		}
		req.mbtMode = mbtMode_p.name();
		req.modelName = modelName_p;
		String reqJson = this.gson.toJson(req);
		String retJson = this.svr.sendPost("client", "model/gen", reqJson, 200);
		RunResult result = gson.fromJson(retJson, RunResult.class);
		return result;
	}

	/**
	 * 
	 * generate test paths (test cases) to cover a subset of transitions in a model.
	 * @param modelName_p name of the model (uploaded)
	 * @param transList_p list of transitions to be covered
	 * @param optimal_p true to generate minimal # of test steps to cover all transitions in transList_p, 
	 * 		false to generate test path by covering the transitions in transList_p in the order they are
	 * 		listed.
	 * @return RunResult that contains the test paths and test steps.
	 * @throws APIError on any error
	 */
	public RunResult genPathsPartial (String modelName_p, List<Trans> transList_p, boolean optimal_p) throws APIError {
		return this.genPathsPartial (modelName_p, transList_p, optimal_p, 0);
	}

	/**
	 * 
	 * generate test paths (test cases) to cover a subset of transitions in a model.
	 * @param modelName_p name of the model (uploaded)
	 * @param transList_p list of transitions to be covered
	 * @param optimal_p true to generate minimal # of test steps to cover all transitions in transList_p, 
	 * 		false to generate test path by covering the transitions in transList_p in the order they are
	 * 		listed.
	 * @param timeoutMillis_p # of milliseconds to wait for the generation to complete. 0 for no timeout
	 * @return RunResult that contains the test paths and test steps.
	 * @throws APIError on any error
	 */
	public RunResult genPathsPartial (String modelName_p, List<Trans> transList_p, boolean optimal_p, int timeoutMillis_p) throws APIError {
		ModelRequest req = new ModelRequest();
		if (timeoutMillis_p > 0) {
			req.options.put("timeoutMillis", timeoutMillis_p);
		}
		req.mbtMode = optimal_p? "MarkedOptimal": "MarkedSerial";
		List<String> transUIDs = transList_p.stream().map(t -> t.getUid()).collect(Collectors.toList());
		req.options.put("markList", transUIDs);
		req.modelName = modelName_p;
		String reqJson = this.gson.toJson(req);
		String retJson = this.svr.sendPost("client", "model/gen", reqJson, 200);
		RunResult result = gson.fromJson(retJson, RunResult.class);
		return result;
	}

	/**
	 * closes the model (to release memory). Do not use this method to close model execution started with AgentAPI.
	 * @param modelName_p model name
	 * @throws APIError on any error
	 */
	public void closeModel (String modelName_p) throws APIError {
		this.svr.sendGet("model", modelName_p + "/close", null, 200);
		this.svr.getAgentAPI().modelClosed();
	}
	
	/**
	 * download model graph.
	 * @param modelName_p model name
	 * @param outFilePath_p where to save the graph
	 * @return absolute file path to the graph saved
	 * @throws APIError on any error
	 */
	public String getGraphModel (String modelName_p, String outFilePath_p) throws APIError {
		return this.svr.getGraph (modelName_p, GraphType.model.name(), outFilePath_p);
	}

	/**
	 * download coverage graph for current model execution.
	 * @param modelName_p model name
	 * @param outFilePath_p where to save the graph
	 * @return absolute file path to the graph saved
	 * @throws APIError on any error
	 */
	public String getGraphCoverage (String modelName_p, String outFilePath_p) throws APIError {
		return this.svr.getGraph (modelName_p, GraphType.coverage.name(), outFilePath_p);
	}

	/**
	 * download sequence/traversal graph for current model execution.
	 * @param modelName_p model name
	 * @param outFilePath_p where to save the graph
	 * @return absolute file path to the graph saved
	 * @throws APIError on any error
	 */
	public String getGraphSequence (String modelName_p, String outFilePath_p) throws APIError {
		return this.svr.getGraph (modelName_p, GraphType.sequence.name(), outFilePath_p);
	}

	/**
	 * download MSC graph for current model execution.
	 * @param modelName_p model name
	 * @param outFilePath_p where to save the graph
	 * @return absolute file path to the graph saved
	 * @throws APIError on any error
	 */
	public String getGraphMSC (String modelName_p, String outFilePath_p) throws APIError {
		return this.svr.getGraph (modelName_p, GraphType.msc.name(), outFilePath_p);
	}

}
