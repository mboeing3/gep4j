package org.gep4j.math;

import org.gep4j.ANode;
import org.gep4j.DefaultConverter;
import org.gep4j.IConverter;
import org.gep4j.INode;
import org.gep4j.NodeEvaluation;

public class Divide extends ANode implements INode {
	IConverter converter;
	
	public Divide() {
		converter = new DefaultConverter();
	}
	
	@Override
	public int getAirity() {
		return 2;
	}

	@Override
	public Object evaluate(Object args[]) {
		try {
			double d1 = converter.toDouble(args[0]);
			double d2 = converter.toDouble(args[1]);
			Double ret = d1 / d2;
			return ret.isNaN() ? 0.0 : ret;
		} catch (Exception e) {
			return 0.0;
		}
	}

	@Override
	public String print(NodeEvaluation[] nodeEvals) {
		return String.format("(%s / %s)", nodeEvals[0].print(), nodeEvals[1].print());
	}	
}
