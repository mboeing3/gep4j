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
