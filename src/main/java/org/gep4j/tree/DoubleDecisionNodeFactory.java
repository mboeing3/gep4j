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

import java.util.Random;

import org.gep4j.INode;
import org.gep4j.INodeFactory;

public class DoubleDecisionNodeFactory implements INodeFactory {
	private double min;
	private double max;
	private ThreadLocal<Double> value;
	private String name;
	
	public DoubleDecisionNodeFactory(String name, double min, double max, ThreadLocal<Double> value) {
		this.min = min;
		this.max = max;
		this.value = value;
		this.name = name;
	}
	
	@Override
	public INode create(Random rng) {
		double rand = rng.nextDouble() * (max - min);
		return new DoubleDecisionNode(name, min + rand, value);
	}

	@Override
	public int getAirity() {
		return 2;
	}
}
