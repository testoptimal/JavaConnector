package testoptimal.api.COMB;
import com.google.gson.annotations.SerializedName;

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
public class Field {
	public static enum DataType {bool, text, intNum};

	@SerializedName(value="fieldName")
	private String name;

	private DataType dataType = DataType.text;
	
	/**
	 * if this field is derived from other field using groovy expression specified in the domain array.
	 */
	private boolean derived = false;
	
	/**
	 *  groupCode is the name of the field that this field will match to. It is used when derived = false.
	 */
	@SerializedName(value="groupCode")
	private String coupledField = "";
	
	private String[] domainList = new String[] {};
	
	public Field (String name_p, DataType dataType_p, String[] domainList_p) {
		this.name = name_p;
		this.dataType = dataType_p;
		this.domainList = domainList_p;
	}

	/**
	 * 
	 * @param name_p field name, preferably a legal java variable name
	 * @param dataType_p bool, text, intNum
	 * @param domainList_p Array list of domain values to be tested
	 * @param coupledField_p name of the field to be coupled with
	 * @param derived_p if the field is derived, meaning its value is derived from an expression specified in domainList_p
	 */
	public Field (String name_p, DataType dataType_p, String[] domainList_p, String coupledField_p, boolean derived_p) {
		this.name = name_p;
		this.coupledField = coupledField_p;
		this.dataType = dataType_p;
		this.derived = derived_p;
		this.domainList = domainList_p;
	}
}
