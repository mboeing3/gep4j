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
package org.gep4j;

public class VariableTerminal implements INode {
	private String name;
	private ThreadLocal<Double> value;
	
	public VariableTerminal(String name, ThreadLocal<Double> value) {
		this.name = name;
		this.value = value;
	}
	
	@Override
	public Object evaluate(Object[] args) {
		return value.get();
	}

	@Override
	public int getAirity() {
		return 0;
	}

	@Override
	public String print(NodeEvaluation[] nodeEvals) {
		return name;
	}
}
