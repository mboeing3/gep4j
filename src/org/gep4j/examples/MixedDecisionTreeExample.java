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
package org.gep4j.examples;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.gep4j.GeneFactory;
import org.gep4j.INode;
import org.gep4j.INodeFactory;
import org.gep4j.KarvaEvaluator;
import org.gep4j.MutationOperator;
import org.gep4j.RecombinationOperator;
import org.gep4j.SimpleNodeFactory;
import org.gep4j.tree.IntegerDecisionNodeFactory;
import org.gep4j.tree.NominalNode;
import org.gep4j.tree.NominalTerminalFactory;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

public class MixedDecisionTreeExample {
	// total gene length
	private static final int GENE_SIZE 	= 15;
	
	// columns of table model
	private static final int OUTLOOK 	= 0;
	private static final int TEMP 		= 1;
	private static final int HUMIDITY 	= 2;
	private static final int WINDY 		= 3;
	private static final int PLAY	 	= 4;
	
	// karva expression (INode[] for this example) evaluator 
	final KarvaEvaluator karvaEvaluator = new KarvaEvaluator();	
	
	// table to hold the sample data
	private DefaultTableModel data;
	
	// variables 
	private ThreadLocal<String> outlook;
	private ThreadLocal<String> windy;
	private ThreadLocal<Integer> tempurature;
	private ThreadLocal<Integer> humidity;
	
	private void go() {
		data = new DefaultTableModel(new Object[][]{
				new Object[]{"sunny", 	85, 85,   "false", "No"},
				new Object[]{"sunny", 	80, 90,   "true",  "No"},
				new Object[]{"overcast",83, 86,   "false", "Yes"},
				new Object[]{"rainy", 	70, 96,   "false", "Yes"},
				new Object[]{"rainy",	68, 80,   "false", "Yes"},
				new Object[]{"rainy", 	65, 70,   "true",  "No"},
				new Object[]{"overcast",64, 65,   "true",  "Yes"},
				new Object[]{"sunny", 	72, 95,   "false", "No"},
				new Object[]{"sunny", 	69, 70,   "false", "Yes"},
				new Object[]{"rainy", 	75, 80,   "false", "Yes"},
				new Object[]{"sunny", 	75, 70,   "true",  "Yes"},
				new Object[]{"overcast",72, 90,   "true",  "Yes"},
				new Object[]{"overcast",81, 75,   "false", "Yes"},
				new Object[]{"rainy", 	71, 91,   "true",  "No"}
				}, 
				new Object[]{"Outlook", "Temp", "Humid", "Windy", "Play"});
		
		List<INodeFactory> factories = new ArrayList<INodeFactory>();

		outlook = new ThreadLocal<String>();
		tempurature = new ThreadLocal<Integer>();
		humidity = new ThreadLocal<Integer>();
		windy = new ThreadLocal<String>();
		
		 
		factories.add(new SimpleNodeFactory(new NominalNode("Outlook", new String[] {"sunny", "overcast", "rainy"}, outlook)));
		factories.add(new IntegerDecisionNodeFactory("tempurature", 63, 86, tempurature));
		factories.add(new IntegerDecisionNodeFactory("humidity", 64, 96, humidity));
		factories.add(new SimpleNodeFactory(new NominalNode("Windy", new String[] {"false", "true"}, windy)));
		factories.add(new NominalTerminalFactory(new Object[] {"No", "Yes"}));

		GeneFactory factory = new GeneFactory(factories, GENE_SIZE);

		List<EvolutionaryOperator<INode[]>> operators = new ArrayList<EvolutionaryOperator<INode[]>>();
		operators.add(new MutationOperator<INode[]>(factory, new Probability(0.2d)));
		operators.add(new RecombinationOperator<INode[]>(factory, new Probability(0.9d)));
		EvolutionaryOperator<INode[]> pipeline = new EvolutionPipeline<INode[]>(operators);

		FitnessEvaluator<INode[]> evaluator = new FitnessEvaluator<INode[]>() {
			@Override
			public double getFitness(INode[] candidate, List<? extends INode[]> population) {
				double error = getError(candidate);
				return error;
			}

			@Override
			public boolean isNatural() {
				return false;
			}
		};

		GenerationalEvolutionEngine<INode[]> engine = new GenerationalEvolutionEngine<INode[]>(factory, pipeline, evaluator,
				new RouletteWheelSelection(), new MersenneTwisterRNG());
		
		EvolutionObserver<INode[]> observer = new EvolutionObserver<INode[]>() {
			@Override
			public void populationUpdate(PopulationData<? extends INode[]> data) {
				INode[] bestIndividual = data.getBestCandidate();
				double error = getError(bestIndividual);
				System.out.printf("Generation %d, error = %.16f, %s\n", 
								  data.getGenerationNumber(), 
								  error, 
								  karvaEvaluator.print(bestIndividual));
			}

		};
		engine.addEvolutionObserver(observer);
		engine.evolve(500, 1, new TargetFitness(.0001, false));
	}

	private double getError(INode[] ind) {
		double error = 0;
		for (int i=0; i<data.getRowCount(); i++) {
			outlook.set(data.getValueAt(i, OUTLOOK).toString());
			tempurature.set((Integer) data.getValueAt(i, TEMP));
			humidity.set((Integer) data.getValueAt(i, HUMIDITY));
			windy.set(data.getValueAt(i, WINDY).toString());
			String result = (String) karvaEvaluator.evaluate(ind);
			error += result.equals(data.getValueAt(i, PLAY)) ? 0.0 : 1.0;
		}
		
		// TODO implement length method
		//int len= karvaEvaluator.length(ind);
		//double lenError = len / GENE_SIZE;
		double lenError = 0.0;
		return error + lenError;
	}
	
	public static void main(String[] args) {
		new MixedDecisionTreeExample().go();
	}
}
