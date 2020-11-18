package testoptimal.api.FSM;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
public class Model {
	private String uid = Util.genUID();

	@SerializedName (value="modelName")
	private String name;

	private String typeCode = "scxml";
	
	@SerializedName (value="childrenStates")
	private List<State> stateList = new java.util.ArrayList<>();
	
	/**
	 * all models submitted from java connector must have AGENT plugin enabled for automation.
	 */
	private List<String> pluginList = Arrays.asList("AGENT".split(","));
	
	public String getName () {
		return this.name;
	}
	
	public Model (String name_p) {
		this.uid = name_p.replace(" ", "_");
		this.name = name_p;
	}

	public State addState (String name_p) {
		State state = new State (name_p);
		this.assignPosition(state);
		this.stateList.add(state);
		return state;
	}

	private void assignPosition (State state_p) {
		// assign default position so that IDE can display it
		int canvasWidth = 1500, canvasHeight = 1000, offsetLeft = 100, offsetTop = 100, offsetRight = 100, offsetBottom = 100;
		int spacingHorizontal = 300, spacingVertical = 200;
		state_p.setPosition(offsetLeft + (spacingHorizontal * this.stateList.size()) % canvasWidth, 
			offsetTop + (spacingHorizontal * this.stateList.size() / canvasWidth) * spacingVertical, 75, 50);
	}
	
	public State addStateInitial (String name_p) {
		State state = new State (name_p);
		state.setInitialState(true);
		this.assignPosition(state);
		this.stateList.add(state);
		return state;
	}

	public State addStateFinal (String name_p) {
		State state = new State (name_p);
		state.setFinalState(true);
		this.assignPosition(state);
		this.stateList.add(state);
		return state;
	}

	public State findState (String name_p) {
		Optional<State> st = this.stateList.stream().filter(s -> s.getName().equals(name_p)).findFirst();
		return st.orElseGet(null);
	}
	
	public void saveToFile (String filePath_p) throws Exception {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String modelJson = gson.toJson(this);
		Util.writeToFile(filePath_p, modelJson);
	}
	
	public static Model fromFile (String filePath_p) throws Exception {
		StringBuffer modelJson = Util.readFile(filePath_p);
		Gson gson = new Gson();
		Model model = gson.fromJson(modelJson.toString(), Model.class);
		return model;
	}
}
