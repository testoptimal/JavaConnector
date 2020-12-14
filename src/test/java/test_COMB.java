import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import testoptimal.api.APIError;
import testoptimal.api.Constants;
import testoptimal.api.Constants.Algorithm;
import testoptimal.api.DataSetAPI;
import testoptimal.api.RunResult;
import testoptimal.api.Server;
import testoptimal.api.Server.Protocol;
import testoptimal.api.COMB.DataSet;
import testoptimal.api.COMB.DataSet.Strength;
import testoptimal.api.COMB.Field;

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
public class test_COMB {

	private Server svr;
	private DataSetAPI exec;
	private DataSet ds;
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * requires TestOptimal server running on port 8888
	 * 
	 * @throws APIError
	 */
	@Before
	public void init () {
		svr = new Server(Protocol.http, "localhost", 8888, "lin@to.com", "test");
		exec = svr.getDataSetAPI ();
		ds = new DataSet("ds1");
		ds.addField ("F1", "1,2,3".split(","));
		ds.addField ("F2", "a,b,c".split(","));
	}
	
	@Test
	public void test_save () throws APIError {
		exec.upload(this.ds);
	}

	@Test
	public void test_gen () throws APIError {
		exec.upload(this.ds);
		RunResult results = exec.generate(ds.getName(), Algorithm.pairWise);
		System.out.println("test_gen: " + this.gson.toJson(results));
	}

	@Test
	public void test_gen_with_relation () throws APIError {
		DataSet dataset = new DataSet("dataset_with_relation");
		dataset.addField ("F1", Field.DataType.intNum, "1,2,3".split(","), null, false);
		dataset.addField ("F2", "a,b,c,d".split(","));
		dataset.addField ("F3", Field.DataType.bool, "true,false".split(","), null, false);
		dataset.addField ("F4a", "cat,dog,snake,bear,shark".split(","));
		dataset.addField ("F4b", Field.DataType.intNum, "10,20,30,40,50".split(","), "F4a", false);
		dataset.addField ("F5", Field.DataType.intNum, "10,20".split(","), null, false);
		dataset.addFieldVerify ("V1", "F1 + F4b");
		dataset.addRelation("highly_Interaction", new String[] {"F1","F2","F5"}, Strength.threeWise);
		exec.upload(dataset);
		RunResult results = exec.generate(dataset.getName(), Constants.Algorithm.mixed);
		System.out.println("test_gen_relation: " + this.gson.toJson(results));
	}


	@Test
	public void test_gen_with_constraint () throws Exception {
		DataSet dataset = new DataSet("dataset_with_constraint");
		dataset.addField ("F1", Field.DataType.intNum, "1,2,3".split(","), null, false);
		dataset.addField ("F2", "a,b,c,d".split(","));
		dataset.addField ("F3", Field.DataType.bool, "true,false".split(","), null, false);
		dataset.addField ("F4a", "cat,dog,snake,bear,shark".split(","));
		dataset.addField ("F4b", Field.DataType.intNum, "10,20,30,40,50".split(","), "F4a", false);
		dataset.addField ("F5", Field.DataType.intNum, "10,20".split(","), null, false);
		dataset.addFieldVerify ("V1", "F1 + F4b");
		dataset.addConstraint ("F1 = 2", "F2 = \"a\"");
		exec.upload(dataset);
		RunResult results = exec.generate(dataset.getName(), Constants.Algorithm.pairWise);
		System.out.println("test_gen_relation: " + this.gson.toJson(results));
	}
}
