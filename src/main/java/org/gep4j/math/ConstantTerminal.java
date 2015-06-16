/*
 * Copyright 2010 KAT Software LLC. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */
package org.gep4j.math;

import org.gep4j.ANode;
import org.gep4j.INode;
import org.gep4j.NodeEvaluation;

public class ConstantTerminal extends ANode implements INode {
	Double value;
	
	public ConstantTerminal(double v) {
		value = v;
	}
	
	@Override
	public int getAirity() {
		return 0;
	}

	@Override
	public Object evaluate(Object args[]) {
		return value;
	}
	

	@Override
	public String print(NodeEvaluation[] nodeEvals) {
		return value.toString();
	}
}
