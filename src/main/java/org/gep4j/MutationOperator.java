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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

public class MutationOperator<T> implements EvolutionaryOperator<T> {
	private Probability probability;
	private IMutator<T> mutator;
	
	public MutationOperator(IMutator<T> mutator, Probability probability) {
		this.probability = probability;
		this.mutator = mutator;
	}

	@Override
	public List<T> apply(List<T> selectedCandidates, Random rng) {
		List<T> ret = new ArrayList<T>();
		probability.nextEvent(rng);
		for (T individual : selectedCandidates) {
			ret.add(mutator.mutate(individual, probability, rng));
		}
		return ret;
	}
}
