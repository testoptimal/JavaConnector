package testoptimal.api.FSM;

import java.util.Map;

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
public class ModelRequest {
	public String modelName;
	public String mbtMode;
	
	/**
	 * For StateModel executions:
	 *   <ul>
	 *   <li>stopMinute: stop condition - stop MBT execution after the specified number of minutes</li>
	 *   <li>stopTraversal: stop condition - stop MBT execution after the specified number of trasition traversals have been reached</li>
	 *   <li>stopTransCoveragePct: stop condition - stop MBT execution when the transition coverage has reached the specified percentage</li>
	 *   <li>stopAtFinalOnly: stop execution at final states only</li>
	 *   <li>seqParams: additional sequencer parameters in the format of code=value, separate multiple settings with a semi-colon</li>
	 *   <li>seed: int to set the seed to be used by random number generator</li>
	 *   <li>initScript: array of groovy script to be executed before model execution. use this to set user variables. This overrides "Init Scripts" in model setting.</li>
	 *   <li>autoClose: true/false to set Auto Close model after model execution.</li>
	 * 	 <li>markList: array of UIDs for states and/or transitions</li>
	 *   </ul>
	 * 
	 */
	public Map<String, Object> options = new java.util.HashMap<String, Object>();
}