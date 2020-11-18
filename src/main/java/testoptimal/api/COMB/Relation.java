package testoptimal.api.COMB;
import java.util.Arrays;
import java.util.List;
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
public class Relation {
	/**
	 * user assigned name, must be unique
	 */
	@SerializedName(value="relName")
	private String name = "";
	
	private DataSet.Strength relationStrength = DataSet.Strength.pairWise;
	
	private List<String> fieldNameList;
	
	public Relation (String name_p, String [] fieldNameList_p, DataSet.Strength strength_p) {
		this.name = name_p;
		this.relationStrength = strength_p;
		this.fieldNameList = Arrays.asList(fieldNameList_p);
	}
}
