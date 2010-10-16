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

public class GepFactory extends AbstractCandidateFactory<INode[]> implements IMutator<INode[]>, IRecombiner<INode[]> {
	private List<INodeFactory> heads;
	private List<INodeFactory> tails;
	private int headLength;
	private int tailLength;
	
	private int length;
	
	public GepFactory(List<INodeFactory> factories, int length) {		
		int maxAirity = 0;
		heads = new ArrayList<INodeFactory>();
		tails = new ArrayList<INodeFactory>();
		for (INodeFactory f : factories) {
			int airity = f.getAirity(); 
			maxAirity = Math.max(maxAirity, airity);
			heads.add(f);
			if (airity == 0) {
				tails.add(f);
			}
		}
		
		headLength = 0;
		do {
			headLength++;
			tailLength = headLength * (maxAirity - 1) + 1;
		} while (headLength + tailLength <= length);
		
		headLength--;
		tailLength = length - headLength;
		this.length = length;
	}
	
	@Override
	public INode[] generateRandomCandidate(Random rng) {
		INode[] cand = new INode[headLength + tailLength];
		int i=0;
		for (; i<headLength; i++) {
			cand[i] = createHead(rng);
		}
		
		int length = headLength + tailLength;
		
		for (; i<length; i++) {
			cand[i] = createTail(rng);
		}
		
		return cand;
	}

	private INode createHead(Random rng) {
		int factoryIndex = rng.nextInt(heads.size());
		return heads.get(factoryIndex).create(rng);
	}

	private INode createTail(Random rng) {
		int factoryIndex = rng.nextInt(tails.size());
		return tails.get(factoryIndex).create(rng);
	}

	@Override
	public INode[] mutate(INode[] individual, Probability probability, Random rng) {
		INode n[] = new INode[individual.length];
		
		int i=0;
		for (; i<headLength; i++) {
			if (probability.nextEvent(rng)) {
				n[i] = createHead(rng);
			} else {
				n[i] = individual[i];
			}
		}
		
		for (; i<length; i++) {
			if (probability.nextEvent(rng)) {
				n[i] = createTail(rng);
			} else {
				n[i] = individual[i];
			}
		}
		
		return n;
	}

	@Override
	public List<INode[]> recombine(List<INode[]> individual, Probability probability, Random rng) {
		List<INode[]> n = shuffle(individual, rng);
		for (int i=0; i<n.size()-1; i += 2) {
			if (probability.nextEvent(rng)) {
				recombine(n, i, rng);
			} 
		}
		return n;
	}

	private void recombine(List<INode[]> n, int i, Random rng) {
		INode[] mother = n.get(i);
		INode[] father = n.get(i+1);
		INode[] son = new INode[mother.length];
		INode[] daughter = new INode[mother.length];
		
		int point = rng.nextInt(mother.length);
		
		System.arraycopy(mother, 0, son, 0, point);
		System.arraycopy(father, point, son, point, length-point);

		System.arraycopy(father, 0, daughter, 0, point);
		System.arraycopy(mother, point, daughter, point, length-point);
		
		n.set(i, son);
		n.set(i+1, daughter);
	}

	private List<INode[]> shuffle(List<INode[]> nodes, Random rng) {
		List<INode[]> shuffled = new ArrayList<INode[]>(nodes);
		for (int i=0; i<shuffled.size(); i++) {
			int target = rng.nextInt(shuffled.size());
			INode[] temp = shuffled.get(i);
			shuffled.set(i, shuffled.get(target));
			shuffled.set(target, temp);
		}
		return shuffled;
	}
}
