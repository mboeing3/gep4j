package org.gep4j;

public class VariableTerminal implements INode {
	private String name;
	private ThreadLocal<Double> value;
	
	public VariableTerminal() {
		value = new ThreadLocal<Double>();
	}
	
	public VariableTerminal(String name) {
		this();
		this.name = name;
	}
	
	public Double getValue() {
		return value.get();
	}

	public void setValue(Double value) {
		this.value.set(value);
	}

	@Override
	public Object evaluate(Object[] args) {
		return value.get();
	}

	@Override
	public int getAirity() {
		return 0;
	}

	@Override
	public String print(NodeEvaluation[] nodeEvals) {
		return name;
	}
}
