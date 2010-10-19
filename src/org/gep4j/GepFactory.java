package org.gep4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

public class GepFactory extends AbstractCandidateFactory<List<INode[]>> {
	private List<GeneFactory> factories;
	
	public GepFactory(List<GeneFactory> factories) {
		this.factories = factories;
	}
	
	@Override
	public List<INode[]> generateRandomCandidate(Random rng) {
		List<INode[]> cand = new ArrayList<INode[]>();
		
		for (GeneFactory fact : factories) {
			cand.add(fact.generateRandomCandidate(rng));
		}
		return cand;
	}

}
