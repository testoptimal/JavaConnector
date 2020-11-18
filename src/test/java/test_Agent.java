import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import testoptimal.api.APIError;
import testoptimal.api.AgentAPI;
import testoptimal.api.ModelAPI;
import testoptimal.api.RunResult;
import testoptimal.api.Server;
import testoptimal.api.Server.Protocol;

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
public class test_Agent {
	private AgentAPI agentAPI;
	private ModelAPI modelAPI;
	private String modelName;

	@Before
	public void init () throws APIError {
		Server svr = new Server(Protocol.http, "localhost", 8888, "lin@to.com", "test");
		modelName = "DEMO_RemoteTrigger";
		agentAPI = svr.getAgentAPI ();
		modelAPI = svr.getModelAPI();
	}
	
	@Test
	public void test_all () throws APIError {
		this.agentAPI.startModel(modelName);
		this.agentAPI.regAgent(modelName);
		String cmd = this.agentAPI.getNextCmd();
		while (cmd != null && !cmd.equals("")) {
			System.out.println("Next Cmd: " + cmd);
			this.agentAPI.setResult(Math.random()>0.25, "Echo " + cmd);
			cmd = this.agentAPI.getNextCmd();
		}
		System.out.println ("Agent is done");
		this.agentAPI.stopModel();
		RunResult result = this.agentAPI.getModelStats(modelName);
		System.out.println("results: " + result.getPathList());
		modelAPI.getGraphMSC(modelName, "msc.png");
	}	
}
