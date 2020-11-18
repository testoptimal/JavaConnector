package testoptimal.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import testoptimal.api.Constants.Status;

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
public class RunResult {
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public Status status = Status.success;
	public String errorMsg;
	public long execMillis;
	public Map<String, Object> results = new java.util.HashMap<>();
	
	/**
	 * returns the list of test paths generated.
	 * @return List of TestPath objects
	 */
	public List<TestPath> getPathList () {
		@SuppressWarnings("unchecked")
		List<List<List<String>>> pathList = (List<List<List<String>>>) this.results.get("pathList");
		AtomicInteger tcId = new AtomicInteger();
		return pathList.stream().map(p -> new TestPath (p, tcId.getAndIncrement())).collect(Collectors.toList());
	}
	
	public String toString() {
		return gson.toJson(this);
	}
	
	public class TestPath {
		public String status;
		public int id;
		public List<TestStep> stepList = new java.util.ArrayList<>();
		public TestPath(List<List<String>> testCase_p, int tcId_p) {
			this.id = tcId_p;
			this.stepList = testCase_p.stream().map(s -> new TestStep (s)).collect(Collectors.toList());
			if (this.stepList.stream().filter(s -> s.status!=null && s.status.indexOf("fail")>=0).count() > 0) {
				this.status = "failed";
			}
			else this.status = "passed";
		}
		
		public String toString () {
			return gson.toJson(this);
		}
	}
	
	
	public class TestStep {
		public String stateName;
		public String transName;
		public String status;
		
		protected TestStep (List<String> traversal_p) {
			if (traversal_p.size() > 0) {
				this.stateName = traversal_p.get(0);
			}
			if (traversal_p.size() > 1) {
				this.transName = traversal_p.get(1);
			}
			if (traversal_p.size() > 2) {
				this.status = traversal_p.get(2);
			}
		}

		public String toString () {
			return gson.toJson(this);
		}
	}
}
