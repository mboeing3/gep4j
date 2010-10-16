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

import java.util.List;
import java.util.Random;

import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

public class RecombinationOperator<T> implements EvolutionaryOperator<T> {
	private Probability probability;
	private IRecombiner<T> recombiner;
	
	public RecombinationOperator(IRecombiner<T> recombiner, Probability probability) {
		this.probability = probability;
		this.recombiner = recombiner;
	}

	@Override
	public List<T> apply(List<T> selectedCandidates, Random rng) {
		return recombiner.recombine(selectedCandidates, probability, rng);
	}
}
