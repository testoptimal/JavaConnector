package testoptimal.api.FSM;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

import testoptimal.api.Util;

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
public class Trans {
	public static enum TransAnchorType {left, right, top, bottom};
	
	private String uid = Util.genUID();
	
	@SerializedName (value="targetUID")
	private String targetStateName = "";
	
	private transient State targetState;
	
	@SerializedName (value="event")
	private String name;

	private int weight = 5;
	private int traverseTimes = 1;

	/**
	 * change target state
	 * @param targetState_p target state object
	 * @return this transition for chaining action
	 */
	public Trans setTargetState (State targetState_p) {
		this.targetState = targetState_p;
		this.targetStateName = targetState_p.getUid();
		return this;
	}
	
	protected Trans (String name_p, State targetState_p) {
		this.name = name_p;
		this.setTargetState(targetState_p);
	}
	
	public String getUid () {
		return this.uid;
	}
	
	public String getName() {
		return this.name;
	}
	
	/**
	 * assign transition weight. default value is 5.
	 * @param weight_p positive integer
	 * @return this transition for chaining action
	 */
	public Trans setWeight (int weight_p) {
		this.weight = weight_p;
		return this;
	}

	/**
	 * sets number of times the transition must be traversed. default value is 1.
	 * @param traverseTimes_p positive integer
	 * @return this transition for chaining action
	 */
	public Trans setTraverseTimes (int traverseTimes_p) {
		this.traverseTimes = traverseTimes_p;
		return this;
	}
}
