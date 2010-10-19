package org.gep4j;


import java.util.ArrayList;
import java.util.List;

import org.gep4j.GeneFactory;
import org.gep4j.INode;
import org.gep4j.INodeFactory;
import org.gep4j.IntegerConstantFactory;
import org.gep4j.KarvaEvaluator;
import org.gep4j.MutationOperator;
import org.gep4j.RecombinationOperator;
import org.gep4j.SimpleNodeFactory;
import org.gep4j.math.Add;
import org.gep4j.math.Divide;
import org.gep4j.math.Multiply;
import org.gep4j.math.Subtract;
import org.junit.Assert;
import org.junit.Test;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

public class TestSimpleExpression {
	final KarvaEvaluator karvaEvaluator = new KarvaEvaluator();
	public INode[] bestIndividual=null;

	@Test
	public void testPI() {
		List<INodeFactory> factories = new ArrayList<INodeFactory>();

		factories.add(new SimpleNodeFactory(new Add()));
		factories.add(new SimpleNodeFactory(new Multiply()));
		factories.add(new SimpleNodeFactory(new Subtract()));
		factories.add(new SimpleNodeFactory(new Divide()));
		factories.add(new IntegerConstantFactory(-100000, 100000));

		GeneFactory factory = new GeneFactory(factories, 20);

		List<EvolutionaryOperator<INode[]>> operators = new ArrayList<EvolutionaryOperator<INode[]>>();
		operators.add(new MutationOperator<INode[]>(factory, new Probability(0.01d)));
		operators.add(new RecombinationOperator<INode[]>(factory, new Probability(0.5d)));
		EvolutionaryOperator<INode[]> pipeline = new EvolutionPipeline<INode[]>(operators);

		FitnessEvaluator<INode[]> evaluator = new FitnessEvaluator<INode[]>() {
			@Override
			public double getFitness(INode[] candidate, List<? extends INode[]> population) {
				double result = (Double) karvaEvaluator.evaluate(candidate);
				Double error = Math.abs(Math.PI - result);
				return error;
			}

			@Override
			public boolean isNatural() {
				return false;
			}
		};

		EvolutionEngine<INode[]> engine = new GenerationalEvolutionEngine<INode[]>(factory, pipeline, evaluator,
				new RouletteWheelSelection(), new MersenneTwisterRNG());
		EvolutionObserver<INode[]> observer = new EvolutionObserver<INode[]>() {
			@Override
			public void populationUpdate(PopulationData<? extends INode[]> data) {
				bestIndividual = data.getBestCandidate();
				System.out.printf("Generation %d, error = %.16f, value = %.16f, %s\n", 
								  data.getGenerationNumber(), 
								  Math.abs(Math.PI - (Double)karvaEvaluator.evaluate(bestIndividual)), 
								  (Double)karvaEvaluator.evaluate(bestIndividual), 
								  karvaEvaluator.print(bestIndividual));
			}
		};
		engine.addEvolutionObserver(observer);
		engine.evolve(1000, 1, new TargetFitness(.0001, false));
		Assert.assertTrue(Math.abs(Math.PI - (Double)karvaEvaluator.evaluate(bestIndividual)) < .0001);
//		engine.evolve(2000, 1, new GenerationCount(1000));
	}
}
