import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import testoptimal.api.APIError;
import testoptimal.api.Constants.MbtMode;
import testoptimal.api.ModelAPI;
import testoptimal.api.RunResult;
import testoptimal.api.Server;
import testoptimal.api.Server.Protocol;
import testoptimal.api.FSM.Model;
import testoptimal.api.FSM.State;
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
public class test_Model {
	private ModelAPI exec;
	private Model model;
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * requires TestOptimal server running on port 8888
	 * 
	 * @throws APIError
	 */
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
		exec.upload(model);
	}
	
	@Test
	public void test_generate_default () throws APIError {
		RunResult result = exec.genPaths (model.getName());
		exec.closeModel(model.getName());
		System.out.println("test_generate (default, optimal): " + result.getPathList());
	}

	@Test
	public void test_close () throws APIError {
		exec.closeModel(model.getName());
		System.out.println("test_close");
	}

	@Test
	public void test_generate_optimal () throws APIError {
		RunResult result = exec.genPaths (model.getName(), MbtMode.Optimal);
		exec.closeModel(model.getName());
		System.out.println("test_generate_optimal: " + result.getPathList());
	}

	@Test
	public void test_generate_priority () throws APIError {
		RunResult result = exec.genPaths (model.getName(), MbtMode.Priority);
		exec.closeModel(model.getName());
		System.out.println("test_generate_priority: " + result.getPathList());
	}
	
	@Test
	public void test_generate_pairwise () throws APIError {
		RunResult result = exec.genPaths (model.getName(), MbtMode.Pairwise);
		exec.closeModel(model.getName());
		System.out.println("test_generate_pairwise: " + result.getPathList());
	}

	@Test
	public void test_generate_random () throws APIError {
		RunResult result = exec.genPaths (model.getName(), MbtMode.Random);
		exec.closeModel(model.getName());
		System.out.println("test_generate_random: " + result.getPathList());
	}
	
	@Test
	public void test_generate_partial_optimal () throws APIError {
		List<Trans> someTrans = new java.util.ArrayList<>();
		someTrans.add(this.model.findState("Start").findTrans("trans_12"));
		someTrans.add(this.model.findState("State 4").findTrans("trans_43"));
		
		RunResult result = exec.genPathsPartial (model.getName(), someTrans, true);
		exec.closeModel(model.getName());
		System.out.println("test_generate_partial_optimal: " + result.getPathList());
	}

	@Test
	public void test_generate_partial_serial () throws APIError {
		List<Trans> someTrans = new java.util.ArrayList<>();
		someTrans.add(this.model.findState("State 4").findTrans("trans_43"));
		
		RunResult result = exec.genPathsPartial (model.getName(), someTrans, false);
		exec.closeModel(model.getName());
		System.out.println("test_generate_partial_optimal: " + result.getPathList());
	}
}
