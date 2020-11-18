package testoptimal.api.FSM;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gson.annotations.SerializedName;

import testoptimal.api.Util;
import testoptimal.api.FSM.Trans.TransAnchorType;

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
public class State {
	private String uid = Util.genUID();
	
	@SerializedName (value="stateID")
	private String name;
	
	@SerializedName (value="isFinal")
	private boolean finalState = false;
	
	@SerializedName (value="isInitial")
	private boolean initialState = false;

	/**
	 * left: 651
	 * top: 189
	 * width: 83
	 * height: 47
	 */
	private Map<String, Integer> position;
	
	/**
	 * assign state position to be used to display in TestOptimal IDE.  This does not affect the graph layout.
	 * 
	 * @param left_p position left
	 * @param top_p position top
	 * @param width_p state width
	 * @param height_p state height
	 */
	public void setPosition(int left_p, int top_p, int width_p, int height_p) {
		this.position = new java.util.HashMap<>();
		this.position.put("left", left_p);
		this.position.put("top", top_p);
		this.position.put("width", width_p);
		this.position.put("height", height_p);
	}
	
	@SerializedName (value="transitions")
	private List<Trans> transList = new java.util.ArrayList();
	
	public State (String name_p) {
		this.name = name_p;

	}
	
	public String getUid() {
		return this.uid;
	}
	
	public String getName() {
		return this.name;
	}

	/**
	 * adds an outgoing transition to this state.
	 * @param name_p name of the state
	 * @param targetState_p target state object
	 * @return current state for chaining additional calls on this state.
	 */
	public State addTrans (String name_p, State targetState_p) {
		Trans trans = new Trans (name_p, targetState_p);
		this.transList.add(trans);
		return this;
	}
	
	public State setFinalState (boolean finalState_p) {
		this.finalState = finalState_p;
		return this;
	}

	public State setInitialState (boolean initialState_p) {
		this.initialState = initialState_p;
		return this;
	}

	public Trans findTrans (String name_p) {
		Optional<Trans> st = this.transList.stream().filter(t -> t.getName().equals(name_p)).findFirst();
		return st.orElseGet(null);
	}
}
