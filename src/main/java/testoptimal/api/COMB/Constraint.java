package testoptimal.api.COMB;

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
public class Constraint {
	private String ruleExprIF;
	private String ruleExprTHEN;
	
	/**
	 * Rules are expressed as IF 'ifExpr_p' THEN 'thenExpr_p' where expressions must 
	 * evaluate to true or false.  The expression supported are basic arithmetic and boolean
	 * operations. For details, refer to <a href="https://testoptimal.com/v6/wiki/doku.php?id=data_design_ide#define">DataDesigner</a>. 
	 * 
	 * @param ifExpr_p boolean expression
	 * @param thenExpr_p boolean expression
	 */
	public Constraint (String ifExpr_p, String thenExpr_p) {
		this.ruleExprIF = ifExpr_p;
		this.ruleExprTHEN = thenExpr_p;
	}
}
