package testoptimal.api.COMB;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import testoptimal.api.Util;
import testoptimal.api.COMB.Field.DataType;
import testoptimal.api.FSM.Model;

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
public class DataSet {
	public static enum Strength {pairWise, threeWise, fourWise, fiveWise, sixWise};
	public static final String ROWNOFIELDNAME = "_rowno";
	
	@SerializedName(value="dsName")
	private String name;
	public String getName() {
		return this.name;
	}
	
	private String path = "client";
	
	private List<String> pluginList = new java.util.ArrayList();;
	
	private List<Field> fieldList = new java.util.ArrayList(); 
	private String genAlgorithm;
	private Strength overallStrength = Strength.pairWise;
	
	private List <Constraint> ruleList = new java.util.ArrayList();
	private List<Relation> relationList = new java.util.ArrayList();
	
	/**
	 * automation script to loop through data rows generated and perform testing using the data row.
	 */
	private String script = "";
	
	public DataSet (String name_p) {
		this.name = name_p;
	}

	/**
	 * activate additional plugin
	 * @param pluginID_p plugin ID
	 * @return this dataset object
	 */
	public DataSet addPlugin (String pluginID_p) {
		this.pluginList.add(pluginID_p);
		return this;
	}
	
	/**
	 * adds a field.
	 * @param name_p field name
	 * @param domainList_p Array list of domain values to be tested
	 * @return Field object
	 */
	public Field addField (String name_p, String[] domainList_p) {
		Field f = new Field (name_p, DataType.text, domainList_p, null, false);
		this.fieldList.add(f);
		return f;
	}

	/**
	 * adds a verify field - field of which the value is derived from other fields using the expression. For example
	 * 	 	F1 + F4, where F1 and F4 are the field names.
	 * @param name_p field name
	 * @param expr_p groovy expression
	 * @return Field object
	 */
	public Field addFieldVerify (String name_p, String expr_p) {
		Field f = new Field (name_p, DataType.text, new String[] {expr_p}, null, true);
		this.fieldList.add(f);
		return f;
	}

	/**
	 * adds a field to the dataset.
	 * @param name_p field name, preferably a legal java variable name
	 * @param dataType_p bool, text, intNum, floatNum
	 * @param domainList_p Array list of domain values to be tested
	 * @param coupledField_p name of the field to be coupled with
	 * @param derived_p if the field is derived, meaning its value is derived from an expression specified in domainList_p
	 * @return Field object
	 */
	public Field addField (String name_p, DataType dataType_p, String[] domainList_p, String coupledField_p, boolean derived_p) {
		Field f = new Field (name_p, dataType_p, domainList_p, coupledField_p, derived_p);
		this.fieldList.add(f);
		return f;
	}

	/**
	 * adds a relation to the dataset.  A Relation is used to test a set of fields with a specific interaction strength.
	 * Data set may have 0 or many relations.
	 * @param name_p relation name
	 * @param fieldNameList_p list of field names to create the relation
	 * @param strength_p interaction strength to test, pairWise, threeWise, fourWise, fiveWise, sixWise
	 * @return Relation object
	 */
	public Relation addRelation (String name_p, String [] fieldNameList_p, DataSet.Strength strength_p) {
		Relation r = new Relation(name_p, fieldNameList_p, strength_p);
		this.relationList.add(r);
		return r;
	}
	
	/**
	 * adds a constraint to the dataset.  Test cases failing any of the constraints will be removed. Use Constraints to 
	 *    filter out invalid/unwanted test cases. String literal must be enclosed with double quotes.
	 * @param ifExpr_p boolean expression, for example Age &lt; 30 &amp;&amp; Fulltime_Student &amp;&amp; Smoking = "NO"
	 * @param thenExpr_p boolean expression, for example Credit = 50 
	 * @return Rule object
	 * @throws Exception on any error
	 */
	public Constraint addConstraint (String ifExpr_p, String thenExpr_p) throws Exception {
		if (ifExpr_p.indexOf("'") >= 0 || thenExpr_p.indexOf("'") >= 0) {
			throw new Exception ("Boolean expression may not contain single quote. Enclose string literal with double quotes.");
		}
		Constraint r = new Constraint (ifExpr_p, thenExpr_p);
		this.ruleList.add(r);
		return r;
	}

	public void saveToFile (String filePath_p) throws Exception {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String modelJson = gson.toJson(this);
		Util.writeToFile(filePath_p, modelJson);
	}
	
	public static DataSet fromFile (String filePath_p) throws Exception {
		StringBuffer modelJson = Util.readFile(filePath_p);
		Gson gson = new Gson();
		DataSet ds = gson.fromJson(modelJson.toString(), DataSet.class);
		return ds;
	}

}
