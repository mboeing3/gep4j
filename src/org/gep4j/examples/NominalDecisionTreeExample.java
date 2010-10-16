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

import org.gep4j.GepFactory;
import org.gep4j.INode;
import org.gep4j.INodeFactory;
import org.gep4j.KarvaEvaluator;
import org.gep4j.MutationOperator;
import org.gep4j.RecombinationOperator;
import org.gep4j.SimpleNodeFactory;
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

public class NominalDecisionTreeExample {
	
	private static final int GENE_SIZE 	= 15;
	private static final int OUTLOOK 	= 0;
	private static final int TEMP 		= 1;
	private static final int HUMIDITY 	= 2;
	private static final int WINDY 		= 3;
	private static final int PLAY	 	= 4;
	final KarvaEvaluator karvaEvaluator = new KarvaEvaluator();
	public INode[] bestIndividual=null;
	DefaultTableModel data;
	private NominalNode outlook;
	private NominalNode tempurature;
	private NominalNode humidity;
	private NominalNode windy;
	
	private void go() {
		data = new DefaultTableModel(new Object[][]{
				new Object[]{"sunny", 	"hot",  "high",   "false", "No"},
				new Object[]{"sunny", 	"hot",  "high",   "true",  "No"},
				new Object[]{"overcast","hot",  "high",   "false", "Yes"},
				new Object[]{"rainy", 	"mild", "high",   "false", "Yes"},
				new Object[]{"rainy",	"cool", "normal", "false", "Yes"},
				new Object[]{"rainy", 	"cool", "normal", "true",  "No"},
				new Object[]{"overcast","cool", "normal", "true",  "Yes"},
				new Object[]{"sunny", 	"mild", "high",   "false", "No"},
				new Object[]{"sunny", 	"cool", "normal", "false", "Yes"},
				new Object[]{"rainy", 	"mild", "normal", "false", "Yes"},
				new Object[]{"sunny", 	"mild", "normal", "true",  "Yes"},
				new Object[]{"overcast","mild", "high",   "true",  "Yes"},
				new Object[]{"overcast","hot",  "normal", "false", "Yes"},
				new Object[]{"rainy", 	"mild", "high",   "true",  "No"}
				}, 
				new Object[]{"Outlook", "Temp", "Humid", "Windy", "Play"});
		
		List<INodeFactory> factories = new ArrayList<INodeFactory>();

		outlook = new NominalNode("Outlook", new String[] {"sunny", "overcast", "rainy"});
		tempurature = new NominalNode("Tempurature", new String[] {"hot", "mild", "cool"});
		humidity = new NominalNode("Humidity", new String[] {"high", "normal"});
		windy = new NominalNode("Windy", new String[] {"false", "true"});
		
		factories.add(new SimpleNodeFactory(outlook));
		factories.add(new SimpleNodeFactory(tempurature));
		factories.add(new SimpleNodeFactory(humidity));
		factories.add(new SimpleNodeFactory(windy));
		factories.add(new NominalTerminalFactory(new Object[] {"No", "Yes"}));

		GepFactory factory = new GepFactory(factories, GENE_SIZE);

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
				bestIndividual = data.getBestCandidate();
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
			outlook.setValue(data.getValueAt(i, OUTLOOK));
			tempurature.setValue(data.getValueAt(i, TEMP));
			humidity.setValue(data.getValueAt(i, HUMIDITY));
			windy.setValue(data.getValueAt(i, WINDY));
			String result = (String) karvaEvaluator.evaluate(ind);
			error += result.equals(data.getValueAt(i, PLAY)) ? 0.0 : 1.0;
		}
		//int len= karvaEvaluator.length(ind);
		//double lenError = len / GENE_SIZE;
		double lenError = 0.0;
		return error + lenError;
	}
	
	public static void main(String[] args) {
		new NominalDecisionTreeExample().go();
	}
}
