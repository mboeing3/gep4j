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

public class NominalNode implements INode {
	private Object[] options;
	private ThreadLocal value;
	private String name;
	
	public NominalNode(String name, Object[] options, ThreadLocal value) {
		this.options = options;
		this.value = value;
		this.name = name;
	}
	
	@Override
	public Object evaluate(Object[] args) {
		Object v = value.get();
		for (int i=0; i<options.length; i++) {
			if (v.equals(options[i])) {
				return args[i];
			}
		}
		throw new RuntimeException(String.format("Invalid value from %s, value = %s", name, v));
	}

	@Override
	public int getAirity() {
		return options.length;
	}

	@Override
	public String print(NodeEvaluation[] nodeEvals) {
		StringBuffer buf = new StringBuffer();
		buf.append(String.format("if (%s == %s) { \n", name, options[0].toString()));
		buf.append(String.format("    %s}\n", nodeEvals[0].print()));
		for (int i=1; i<options.length; i++) {
			buf.append(String.format("else if (%s == %s) { \n", name, options[i].toString()));
			buf.append(String.format("    %s}\n", nodeEvals[i].print()));
		}
		return buf.toString();
	}
}
