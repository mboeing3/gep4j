package org.gep4j.tree;

import java.util.Random;

import org.gep4j.INode;
import org.gep4j.INodeFactory;

public class IntegerDecisionNodeFactory implements INodeFactory {
	private int min;
	private int max;
	private ThreadLocal<Integer> value;
	private String name;
	
	public IntegerDecisionNodeFactory(String name, int min, int max, ThreadLocal<Integer> value) {
		this.min = min;
		this.max = max;
		this.value = value;
		this.name = name;
	}
	
	@Override
	public INode create(Random rng) {
		int rand = rng.nextInt(max - min + 1);
		return new IntegerDecisionNode(name, min + rand, value);
	}

	@Override
	public int getAirity() {
		return 2;
	}

}
