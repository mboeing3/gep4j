package org.gep4j;

import java.util.ArrayList;
import java.util.List;

public class GepIndividual {
	List<INode[]> genes;
	
	public GepIndividual() {
		genes = new ArrayList<INode[]>();
	}
	
	public INode[] getGene(int index) {
		return genes.get(index);
	}

	public int getGeneCount() {
		return genes.size();
	}

	public void add(INode[] gene) {
		genes.add(gene);
	}

}
