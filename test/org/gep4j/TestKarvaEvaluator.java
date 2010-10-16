package org.gep4j;


import junit.framework.Assert;

import org.gep4j.INode;
import org.gep4j.KarvaEvaluator;
import org.gep4j.math.Add;
import org.gep4j.math.ConstantTerminal;
import org.gep4j.math.Multiply;
import org.gep4j.math.Subtract;
import org.junit.Test;


public class TestKarvaEvaluator {
	@Test
	public void testSimple() {
		INode expression[] = new INode[3];
		expression[0] = new Add();
		expression[1] = new ConstantTerminal(2.0);
		expression[2] = new ConstantTerminal(1.0);
		KarvaEvaluator eval = new KarvaEvaluator();
		
		Double result = (Double) eval.evaluate(expression);
		Assert.assertEquals(3.0, result, .0001);
		Assert.assertEquals("(2.0 + 1.0)", eval.print(expression));
	}

	@Test
	public void testSimple2() {
		INode expression[] = new INode[5];
		expression[0] = new Multiply();
		expression[1] = new ConstantTerminal(4.0);
		expression[2] = new Subtract();
		expression[3] = new ConstantTerminal(3.0);
		expression[4] = new ConstantTerminal(2.0);
		KarvaEvaluator eval = new KarvaEvaluator();
		
		Double result = (Double) eval.evaluate(expression);
		Assert.assertEquals(4.0, result, .0001);
		Assert.assertEquals("(4.0 * (3.0 - 2.0))", eval.print(expression));
	}

	@Test
	public void testSimple3() {
		INode expression[] = new INode[9];
		expression[0] = new Multiply();
		expression[1] = new ConstantTerminal(4.0);
		expression[2] = new Subtract();
		expression[3] = new ConstantTerminal(3.0);
		expression[4] = new Multiply();
		expression[5] = new Multiply();
		expression[6] = new ConstantTerminal(6);
		expression[7] = new ConstantTerminal(7);
		expression[8] = new ConstantTerminal(8);
		KarvaEvaluator eval = new KarvaEvaluator();
		
		Double result = (Double) eval.evaluate(expression);
		Assert.assertEquals(-1332.0, result, .0001);
		Assert.assertEquals("(4.0 * (3.0 - ((7.0 * 8.0) * 6.0)))", eval.print(expression));
	}

	@Test
	public void testSimple4() {
		INode expression[] = new INode[12];
		expression[0] = new Subtract();
		expression[1] = new ConstantTerminal(4.0);
		expression[2] = new Multiply();
		expression[3] = new ConstantTerminal(6.0);
		expression[4] = new Multiply();
		expression[5] = new Multiply();
		expression[6] = new ConstantTerminal(7);
		expression[7] = new ConstantTerminal(2);
		expression[8] = new Subtract();
		expression[9] = new ConstantTerminal(6);
		expression[10] = new ConstantTerminal(5);
		expression[11] = new ConstantTerminal(9);
		KarvaEvaluator eval = new KarvaEvaluator();
		
		Double result = (Double) eval.evaluate(expression);
		Assert.assertEquals(-80.0, result, .0001);
		Assert.assertEquals("(4.0 - (6.0 * ((2.0 * (6.0 - 5.0)) * 7.0)))", eval.print(expression));
	}
}
