package com.testoptimal.plugin;

import com.testoptimal.plugin.MScriptInterface.TO_PLUGIN;

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
@TO_PLUGIN
public abstract class PluginAncestor implements MScriptInterface {
	public abstract String getPluginID();

	public abstract String getPluginDesc();

	/**
	 * INTERNAL USE ONLY.
	 * This usually involves starting up AUT (app under testing).
	 * @throws Exception exception
	 */
	@NOT_MSCRIPT_METHOD
	public abstract void start() throws Exception;
	
	/**
	 * entry to the model initial state and final state
	 */
	@NOT_MSCRIPT_METHOD
	public void enterInitialState () {
		return;
	}

	@NOT_MSCRIPT_METHOD
	public void exitFinalState () {
		return;
	}
	
	/**
	 * INTERNAL USE ONLY.
	 * closes the AUT, disconnect from the external resource e.g. session to Selenium server, closing watij IE object, etc.
	 */
	@NOT_MSCRIPT_METHOD
	public abstract void close();
	
}
