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

import javax.swing.table.TableModel;

import org.gep4j.DoubleConstantFactory;
import org.gep4j.GeneFactory;
import org.gep4j.GepFactory;
import org.gep4j.GepIndividual;
import org.gep4j.INode;
import org.gep4j.INodeFactory;
import org.gep4j.KarvaEvaluator;
import org.gep4j.MutationOperator;
import org.gep4j.RecombinationOperator;
import org.gep4j.SimpleNodeFactory;
import org.gep4j.VariableTerminal;
import org.gep4j.math.Add;
import org.gep4j.math.Divide;
import org.gep4j.math.Multiply;
import org.gep4j.math.Subtract;
import org.gep4j.tree.DoubleDecisionNodeFactory;
import org.gep4j.tree.NominalTerminalFactory;
import org.gep4j.util.FileUtil;
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

public class IrisMultigenicDecisionTreeExample {
	
	private static final int GENE_SIZE 		= 30;
	private static final int ADF_GENE_SIZE 	= 15;
	private static final int NUM_ADFS 	= 7;
	
	private static final int SEPAL_LENGTH 	= 0;
	private static final int SEPAL_WIDTH	= 1;
	private static final int PETAL_LENGTH	= 2;
	private static final int PETAL_WIDTH	= 3;
	private static final int CLASS	 		= 4;
	final KarvaEvaluator karvaEvaluator = new KarvaEvaluator();
	public GepIndividual bestIndividual = null;
	public TableModel data;
	private ThreadLocal<Double> sepalLength;
	private ThreadLocal<Double> sepalWidth;
	private ThreadLocal<Double> petalLength;
	private ThreadLocal<Double> petalWidth;
	private List<ThreadLocal<Double>> adf;
	
	private void go() {
		data =  FileUtil.loadToTableModel("sample-data/iris.csv", new Object[]{"sepalLength", "sepalWidth", "petalLength", "petalWidth", "Class"});
		
		GepFactory gepFactory = new GepFactory();
		
		List<INodeFactory> factories = new ArrayList<INodeFactory>();

		sepalLength = new ThreadLocal<Double>();
		sepalWidth = new ThreadLocal<Double>();
		petalLength = new ThreadLocal<Double>();
		petalWidth = new ThreadLocal<Double>();
		adf = new ArrayList<ThreadLocal<Double>>();
		for (int i=0; i<NUM_ADFS; i++) {
			adf.add(new ThreadLocal<Double>());
		}
		
		factories.add(new DoubleDecisionNodeFactory("sepalLength", 4.0, 8.0, sepalLength));
		factories.add(new DoubleDecisionNodeFactory("sepalWidth", 1.5, 5, sepalWidth));
		factories.add(new DoubleDecisionNodeFactory("petalLength", .5, 7.5, petalLength));
		factories.add(new DoubleDecisionNodeFactory("petalWidth", 0.0, 3.0, petalWidth));
		for (int i=0; i<NUM_ADFS; i++) {
			factories.add(new DoubleDecisionNodeFactory("adf" + i, -20.0, 20.0, adf.get(i)));
		}
		factories.add(new NominalTerminalFactory(new Object[] {"Iris-setosa", "Iris-versicolor", "Iris-virginica"}));

		GeneFactory factory = new GeneFactory(factories, GENE_SIZE);

		List<INodeFactory> adfFactories = new ArrayList<INodeFactory>();
		adfFactories.add(new SimpleNodeFactory(new Add()));
		adfFactories.add(new SimpleNodeFactory(new Multiply()));
		adfFactories.add(new SimpleNodeFactory(new Subtract()));
		adfFactories.add(new SimpleNodeFactory(new Divide()));
		adfFactories.add(new DoubleConstantFactory(-10.0, 10.0));
		adfFactories.add(new SimpleNodeFactory(new VariableTerminal("sepalLength", sepalLength)));
		adfFactories.add(new SimpleNodeFactory(new VariableTerminal("sepalWidth", sepalWidth)));
		adfFactories.add(new SimpleNodeFactory(new VariableTerminal("petalLength", petalLength)));
		adfFactories.add(new SimpleNodeFactory(new VariableTerminal("petalWidth", petalWidth)));
		GeneFactory adfFactory = new GeneFactory(adfFactories, ADF_GENE_SIZE);
		
		for (int i=0; i<NUM_ADFS; i++) {
			gepFactory.addFactory(adfFactory);
		}
		gepFactory.addFactory(factory);
		
		List<EvolutionaryOperator<GepIndividual>> operators = new ArrayList<EvolutionaryOperator<GepIndividual>>();
		operators.add(new MutationOperator<GepIndividual>(gepFactory, new Probability(0.05d)));
		operators.add(new RecombinationOperator<GepIndividual>(gepFactory, new Probability(0.2d)));
		EvolutionaryOperator<GepIndividual> pipeline = new EvolutionPipeline<GepIndividual>(operators);
 
		FitnessEvaluator<GepIndividual> evaluator = new FitnessEvaluator<GepIndividual>() {
			@Override
			public double getFitness(GepIndividual candidate, List<? extends GepIndividual> population) {
				double error = getError(candidate);
				return error;
			}

			@Override
			public boolean isNatural() {
				return false;
			}
		}; 

		GenerationalEvolutionEngine<GepIndividual> engine = new GenerationalEvolutionEngine<GepIndividual>(gepFactory, pipeline, evaluator,
				new RouletteWheelSelection(), new MersenneTwisterRNG());
		
		EvolutionObserver<GepIndividual> observer = new EvolutionObserver<GepIndividual>() {
			@Override
			public void populationUpdate(PopulationData<? extends GepIndividual> data) {
				bestIndividual = data.getBestCandidate();
				double error = getError(bestIndividual);
				System.out.printf("Generation %d, error = %.16f, %s\n", data.getGenerationNumber(), error, karvaEvaluator.print(bestIndividual));
			}

		};
		engine.addEvolutionObserver(observer);
		engine.evolve(200, 1, new TargetFitness(.0001, false));
	}

	private double getError(GepIndividual candidate) {
		double error = 0;
		for (int i=0; i<data.getRowCount(); i++) {
			sepalLength.set(Double.parseDouble(data.getValueAt(i, SEPAL_LENGTH).toString()));
			sepalWidth.set(Double.parseDouble(data.getValueAt(i, SEPAL_WIDTH).toString()));
			petalLength.set(Double.parseDouble(data.getValueAt(i, PETAL_LENGTH).toString()));
			petalWidth.set(Double.parseDouble(data.getValueAt(i, PETAL_WIDTH).toString()));
			int gene=0;
			for (; gene<NUM_ADFS; gene++) {
				INode node[] = candidate.getGene(gene);
				Double adfResult = (Double) karvaEvaluator.evaluate(node);
				adf.get(gene).set(adfResult);
			}
			INode node[] = candidate.getGene(gene);
			String result = (String) karvaEvaluator.evaluate(node);
			error += result.equals(data.getValueAt(i, CLASS)) ? 0.0 : 1.0;
		}
		//int len= karvaEvaluator.length(ind);
		//double lenError = len / GENE_SIZE;
		double lenError = 0.0;
		return error + lenError;
	}
	
	public static void main(String[] args) {
		new IrisMultigenicDecisionTreeExample().go();	
	}
}

/*

Generation 3925, error = 0.0000000000000000, 

***** Gene 0 *****

(sepalWidth / (5.915108402517955 - sepalLength))

***** Gene 1 *****

petalLength

***** Gene 2 *****

7.372398243776317

***** Gene 3 *****

petalLength

***** Gene 4 *****

petalLength

***** Gene 5 *****

sepalWidth

***** Gene 6 *****

petalLength

***** Gene 7 *****

if (petalWidth <= 0.701865452488579) { 
    Iris-setosa
}
else { 
    if (petalLength <= 4.917331679055637) { 
    if (adf3 <= -7.456744139767025) { 
    if (petalLength <= 5.394523564660524) { 
    if (sepalLength <= 6.122999486731862) { 
    Iris-setosa
}
else { 
    Iris-virginica
}

}
else { 
    if (petalWidth <= 0.3939951195473942) { 
    Iris-versicolor
}
else { 
    Iris-setosa
}

}

}
else { 
    if (petalWidth <= 1.6724143238053464) { 
    if (adf0 <= 3.308855906280197) { 
    Iris-versicolor
}
else { 
    Iris-versicolor
}

}
else { 
    if (sepalWidth <= 3.151741736582512) { 
    Iris-virginica
}
else { 
    Iris-versicolor
}

}

}

}
else { 
    if (petalLength <= 5.023713291318587) { 
    if (sepalWidth <= 2.543488071434724) { 
    Iris-virginica
}
else { 
    Iris-versicolor
}

}
else { 
    if (adf0 <= -16.483517646835434) { 
    Iris-versicolor
}
else { 
    Iris-virginica
}

}

}

}

*/