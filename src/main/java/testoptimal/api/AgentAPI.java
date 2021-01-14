package testoptimal.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import testoptimal.api.Constants.MbtMode;
import testoptimal.api.FSM.AgentCmdResult;
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
public class AgentAPI {
	private Server svr;
	private Gson gson = new Gson();
	private String agentID;
	private MbtSessInfo mbtSessInfo;
	private ModelRequest runReq;

	protected AgentAPI (Server svr_p) {
		this.svr = svr_p;
	}

	/**
	 * returns the agentID.
	 * 
	 * @return agent ID
	 */
	public String getAgentID () {
		return this.agentID;
	}

	/**
	 * starts model execution on TestOptimal server with default sequencer Optimal.
	 * @param modelName_p model name
	 * @return mbtSessID model execution sesson ID
	 * @throws APIError on any error
	 */
	public String execModel (String modelName_p) throws APIError {
		return this.execModel (modelName_p, MbtMode.Optimal);
	}

	/**
	 * starts model execution on TestOptimal server with specified sequencer/MBT Mode.
	 * @param modelName_p model name
	 * @param mbtMode_p Optimal, Priority, Pairwise, Random
	 * @return mbtSessID model execution sesson ID
	 * @throws APIError on any error
	 */
	public String execModel (String modelName_p, MbtMode mbtMode_p) throws APIError {
		this.runReq = new ModelRequest();
		this.runReq.options.put("path", "client");
		this.runReq.modelName = modelName_p;
		this.runReq.mbtMode = mbtMode_p.name();
		this.runReq.options.put("autoClose", false);
		String reqJson = this.gson.toJson(this.runReq);
		this.mbtSessInfo = null;
		this.agentID = null;
		this.mbtSessInfo = this.gson.fromJson(this.svr.sendPost("runtime", "model/run/async", reqJson, 200), MbtSessInfo.class);
		return this.mbtSessInfo.mbtSessID;
	}

	/**
	 * starts model execution on TestOptimal server to cover the transitions specified.
	 * @param modelName_p model name
	 * @param transList_p List of transitions to be covered.
	 * @param optimal_p true if to generate minimal number of test steps, false to cover the transitions in the order
	 *     specified in tranList_p.
	 * @return mbtSessID model execution sesson ID
	 * @throws APIError on any error
	 */
	public String execModel (String modelName_p, List<Trans> transList_p, boolean optimal_p) throws APIError {
		this.runReq = new ModelRequest();
		this.runReq.options.put("path", "client");
		this.runReq.modelName = modelName_p;
		this.runReq.mbtMode = optimal_p? "MarkedOptimal": "MarkedSerial";
		List<String> transUIDs = transList_p.stream().map(t -> t.getUid()).collect(Collectors.toList());
		this.runReq.options.put("markList", transUIDs);
		this.runReq.options.put("autoClose", false);
		String reqJson = this.gson.toJson(this.runReq);
		this.mbtSessInfo = null;
		this.agentID = null;
		this.mbtSessInfo = this.gson.fromJson(this.svr.sendPost("runtime", "model/run/async", reqJson, 200), MbtSessInfo.class);
		return this.mbtSessInfo.mbtSessID;
	}
	
	/**
	 * register agent to TestOptimal to be connected to the running model.  Model must be started with startModel(...).
	 * @param modelName_p model name
	 * @return agentID agent ID
	 * @throws APIError on any error
	 */
	public String regAgent (String modelName_p) throws APIError {
		this.agentID = UUID.randomUUID().toString().replace("-", "");
		this.svr.sendGet("agent", "register/" + this.runReq.modelName + "/" + this.agentID, null, 200);
		return this.agentID;
	}

	/**
	 * retrieves the next command from the model.
	 * @return command text. Blank/null indicates the model execution has completed.
	 * @throws APIError on any error
	 */
	public String getNextCmd () throws APIError {
		return this.getNextCmd(0);
	}

	/**
	 * 
	 * retrieves the next command from the model.
	 * @param timeoutMillis_p number of milliseconds the agent should wait for the next command from TestOptimal server.
	 * @return command text. Blank/null indicates the model execution has completed.
	 * @throws APIError on any error
	 */
	public String getNextCmd (long timeoutMillis_p) throws APIError {
		this.checkModelRunning("nextCmd");
		String resultJson = this.svr.sendGet("agent", "nextCmd/" + this.agentID + (timeoutMillis_p>0?"?timeoutMillis=" + timeoutMillis_p:""), null, 200);
		if (resultJson.contentEquals("")) return null;
		RemoteCmd cmd = this.gson.fromJson(resultJson, RemoteCmd.class);
		return (cmd.cmd==null || cmd.cmd.equals(""))?null: cmd.cmd;
	}

	/**
	 * sends the result back to the model to be recorded.
	 * @param isSuccess_p true to record the current test step as success/passed. false to record as failed.
	 * @param result_p text result
	 * @return response from svr for debugging
	 * @throws APIError on any error
	 */
	public String setResult (boolean isSuccess_p, String result_p) throws APIError {
		return this.setResult(isSuccess_p, result_p, null, null, null);
	}

	/**
	 * 
	 * @param isSuccess_p true or false
	 * @param result_p passed or failed message
	 * @param reqTag_p requiremet tag/code
	 * @return response from svr for debugging
	 * @throws APIError on any error
	 */
	public String setResult (boolean isSuccess_p, String result_p, String reqTag_p) throws APIError {
		return this.setResult(isSuccess_p, result_p, reqTag_p, null, null);
	}

	/**
	 * 
	 * @param isSuccess_p true or false
	 * @param result_p passed or failed message
	 * @param reqTag_p requiremet tag/code
	 * @param assertID_p identifier to be assigned to the check - either passed or failed.
	 * @return response from svr for debugging
	 * @throws APIError on any error
	 */
	public String setResult (boolean isSuccess_p, String result_p, String reqTag_p, String assertID_p) throws APIError {
		return this.setResult(isSuccess_p, result_p, reqTag_p, assertID_p, null);
	}

	/**
	 * 
	 * @param isSuccess_p true or false
	 * @param result_p passed or failed message
	 * @param reqTag_p requiremet tag/code
	 * @param assertID_p identifier to be assigned to the check - either passed or failed.
	 * @param moreAttrs_p additional attributes in Map&lt;String, Object&gt; to be passed to the model.
	 * @return response from svr for debugging
	 * @throws APIError on any error
	 */
	public String setResult (boolean isSuccess_p, String result_p, String reqTag_p, String assertID_p, Map<String,Object> moreAttrs_p) throws APIError {
		this.checkModelRunning("setResult");
		AgentCmdResult rslt = new AgentCmdResult ();
		rslt.status = isSuccess_p;
		rslt.result = result_p;
		rslt.assertID = assertID_p;
		rslt.reqTag = reqTag_p;
		rslt.moreAttrs = moreAttrs_p;
		String rsltJson = gson.toJson(rslt);
		return this.svr.sendPost("agent", "setResult/" + this.agentID, rsltJson, 200);
	}

	/**
	 * to interrupt model execution.
	 * @throws APIError on any error
	 */
	public void stopModelExec () throws APIError {
		this.checkModelRunning("stopModel");
		this.svr.sendGet("runtime", "session/" + this.mbtSessInfo.mbtSessID + "/stop", null, 200);
	}

	/**
	 * to close the model (release memory).
	 * @param modelName_p model name
	 * @throws APIError on any error
	 */
	public void closeModel (String modelName_p) throws APIError {
		this.svr.sendGet("model", modelName_p + "/close", null, 200);
		this.mbtSessInfo = null;
		this.agentID = null;
	}
		
	/**
	 * retrieves model execution stats for a model execution
	 * @param modelName_p model name
	 * @return RunResult containing the model execcution stats
	 * @throws APIError on any error
	 */
	public RunResult getModelStats (String modelName_p) throws APIError {
		String retJson = this.svr.sendGet("client", "model/" + modelName_p, null, 200);
		RunResult result = gson.fromJson(retJson, RunResult.class);
		return result;
	}

	protected void modelClosed () {
		this.mbtSessInfo = null;
		this.agentID = null;
	}

	private void checkModelRunning (String path_p) throws APIError {
		if (this.mbtSessInfo==null) {
			throw new APIError (-1, "api", "model not running", path_p);
		}
	}

	public class MbtSessInfo {
		public String statsURL;
		public String mbtSessID;
		public String status;
	}
	
	private class RemoteCmd {
		String cmd;
	}
}
