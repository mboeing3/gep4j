package org.gep4j;


import java.util.Random;

import org.gep4j.INodeFactory;
import org.gep4j.IntegerConstantFactory;
import org.gep4j.math.ConstantTerminal;
import org.junit.Assert;
import org.junit.Test;


public class TestIntegerConstantFactory {
	@Test
	public void testRandom() {
		INodeFactory f = new IntegerConstantFactory(100, 110);
		Random r = new Random(System.currentTimeMillis());
		for (int i=0; i<10000; i++) {
			ConstantTerminal node = (ConstantTerminal) f.create(r);
			double d = (Double) node.evaluate(null);
			Assert.assertTrue(d >= 100 && d <= 110);
		}
	}
}
