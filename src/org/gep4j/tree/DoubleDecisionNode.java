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
package org.gep4j.tree;

import org.gep4j.INode;
import org.gep4j.NodeEvaluation;

public class DoubleDecisionNode implements INode {

	private String name;
	private Double threshold;
	private ThreadLocal<Double> value;
	
	public DoubleDecisionNode(String n, Double threshold, ThreadLocal<Double> value) {
		name = n;
		this.threshold = threshold;
		this.value = value;
	}
	
	@Override
	public int getAirity() {
		return 2;
	}

	@Override
	public Object evaluate(Object[] args) {
		if (value.get() <= threshold) {
			return args[0];
		} else {
			return args[1];
		}
	}

	@Override
	public String print(NodeEvaluation[] nodeEvals) {
		StringBuffer buf = new StringBuffer();
		buf.append(String.format("if (%s <= %s) { \n", name, threshold.toString()));
		buf.append(String.format("    %s\n}\n", nodeEvals[0].print()));
		buf.append(String.format("else { \n", name));
		buf.append(String.format("    %s\n}\n", nodeEvals[1].print()));
		return buf.toString();
	}

}
