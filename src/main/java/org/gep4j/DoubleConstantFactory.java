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

import java.util.Random;

import org.gep4j.math.ConstantTerminal;

public class DoubleConstantFactory implements INodeFactory {
	private double min;
	private double max;
	
	public DoubleConstantFactory(double min, double max) {
		this.min = min;
		this.max = max;
	}
	
	@Override
	public INode create(Random rng) {
		double rand = rng.nextDouble() * (max - min);
		return new ConstantTerminal(min + rand);
	}

	@Override
	public int getAirity() {
		return 0;
	}

}
