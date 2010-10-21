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
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

public class GepFactory extends AbstractCandidateFactory<GepIndividual> implements IMutator<GepIndividual>, IRecombiner<GepIndividual> {
	private List<GeneFactory> factories;
	
	@Override
	public GepIndividual generateRandomCandidate(Random rng) {
		return null;
//		List<INode[]> cand = new ArrayList<INode[]>();
//		
//		for (GeneFactory fact : factories) {
//			cand.add(fact.generateRandomCandidate(rng));
//		}
//		return cand;
	}

	@Override
	public GepIndividual mutate(GepIndividual individual, Probability probability, Random rng) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GepIndividual> recombine(List<GepIndividual> individual, Probability probability, Random rng) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addFactory(GeneFactory adfFactory) {
		// TODO Auto-generated method stub
		
	}
}
