import org.junit.Before;
import org.junit.Test;

import testoptimal.api.APIError;
import testoptimal.api.Constants.MbtMode;
import testoptimal.api.Constants.Status;
import testoptimal.api.ModelAPI;
import testoptimal.api.RunResult;
import testoptimal.api.Server;
import testoptimal.api.Server.Protocol;
import testoptimal.api.FSM.Model;
import testoptimal.api.FSM.State;

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
public class test_Graph {

	private ModelAPI exec;
	private Model model;

	@Before
	public void init () throws APIError {
		Server svr = new Server(Protocol.http, "localhost", 8888, "lin@to.com", "test");
		exec = svr.getModelAPI ();
		
		model = new Model("ClientModel_1");
		State start = model.addStateInitial("Start");
		State s2 = model.addState("State 2");
		State s3 = model.addState("State 3");
		State s4 = model.addState("State 4");
		State s5 = model.addState("State 5");
		State end = model.addStateFinal("End");
		start.addTrans("trans_12", s2).addTrans("trans_13", s3);
		s2.addTrans("trans_23", s3).addTrans("trans_24", s4).addTrans("trans_2end", end);
		s3.addTrans("trans_34", s4).addTrans("trans_32", s2).addTrans("trans_35", s5).addTrans("trans_3end", end);		
		s4.addTrans("trans_42", s2).addTrans("trans_43", s3).addTrans("trans_45", s5).addTrans("trans_4end", end);
		s5.addTrans("trans_53", s3).addTrans("trans_54", s4).addTrans("trans_5end", end);
		ModelAPI modelAPI = svr.getModelAPI();
		modelAPI.upload(model);
		RunResult result = modelAPI.genPaths (model.getName(), MbtMode.Optimal, 10000);
		if (result.status==Status.error) {
			throw new APIError (-1, result.status.name(), result.errorMsg, "model genPaths");
		}
	}
	
	@Test
	public void test_graph_model () throws APIError {
		String f = exec.getGraphModel(model.getName(), "testModel.png");
		System.out.println("saved model graph to " + f);
	}

	@Test
	public void test_graph_coverage () throws APIError {
		String f = exec.getGraphCoverage(model.getName(), "testCoveragel.png");
		System.out.println("saved coverage graph to " + f);
	}
	
	@Test
	public void test_graph_sequence () throws APIError {
		String f = exec.getGraphSequence(model.getName(), "testSequence.png");
		System.out.println("saved sequence/traversal graph to " + f);
	}

	@Test
	public void test_graph_msc () throws APIError {
		String f = exec.getGraphMSC(model.getName(), "testMSC.png");
		System.out.println("saved MSC graph to " + f);
	}
}
