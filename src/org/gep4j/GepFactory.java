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
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

public class GepFactory extends AbstractCandidateFactory<GepIndividual> implements IMutator<GepIndividual>, IRecombiner<GepIndividual> {
	private List<GeneFactory> factories;
	
	public GepFactory() {
		factories = new ArrayList<GeneFactory>();
	}
	
	@Override
	public GepIndividual generateRandomCandidate(Random rng) {
		GepIndividual ind = new GepIndividual();
		for (GeneFactory fact : factories) {
			ind.add(fact.generateRandomCandidate(rng));
		}
		return ind;
	}

	@Override
	public GepIndividual mutate(GepIndividual individual, Probability probability, Random rng) {
		GepIndividual mutant = new GepIndividual();
		for (int i=0; i<individual.getGeneCount(); i++) {
			INode[] gene = individual.getGene(i);
			GeneFactory factory = factories.get(i);
			INode[] mutated = factory.mutate(gene, probability, rng);
			mutant.add(mutated);
		}
		return mutant;
	}

	@Override
	public List<GepIndividual> recombine(List<GepIndividual> individuals, Probability probability, Random rng) {
		List<GepIndividual> ret = new ArrayList<GepIndividual>(individuals.size());
		if (individuals.size() == 0) {
			return ret;
		}
		
		// init new individulas 
		for (int i=0; i<individuals.size(); i++) {
			ret.add(new GepIndividual());
		}
		
		// get gene count
		int geneCount = individuals.get(0).getGeneCount();
	
		for (int gene=0; gene<geneCount; gene++) {
			List<INode[]> genes = new ArrayList<INode[]>(individuals.size());
			for (GepIndividual ind : individuals) {
				genes.add(ind.getGene(gene));
			}
			List<INode[]> recombined = factories.get(gene).recombine(genes, probability, rng);
			for (int i=0; i<ret.size(); i++) {
				ret.get(i).add(recombined.get(i));
			}
		}
		return ret;
	}

	public void addFactory(GeneFactory factory) {
		factories.add(factory);
	}
}
