package org.gep4j.math;

import org.gep4j.INode;
import org.gep4j.NodeEvaluation;

public class VariableTerminal implements INode {
	private Double value;
	private String name;
	
	public VariableTerminal(String name) {
		this.name = name;
	}

	@Override
	public Object evaluate(Object[] args) {
		return value;
	}

	@Override
	public int getAirity() {
		return 0;
	}

	@Override
	public String print(NodeEvaluation[] nodeEvals) {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public void setValue(Double v) {
		value = v;
	}
}
