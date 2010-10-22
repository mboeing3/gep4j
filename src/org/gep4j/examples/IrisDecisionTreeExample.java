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

import org.gep4j.GeneFactory;
import org.gep4j.INode;
import org.gep4j.INodeFactory;
import org.gep4j.KarvaEvaluator;
import org.gep4j.MutationOperator;
import org.gep4j.RecombinationOperator;
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

public class IrisDecisionTreeExample {
	
	private static final int GENE_SIZE 		= 30;
	
	private static final int SEPAL_LENGTH 	= 0;
	private static final int SEPAL_WIDTH	= 1;
	private static final int PETAL_LENGTH	= 2;
	private static final int PETAL_WIDTH	= 3;
	private static final int CLASS	 		= 4;
	final KarvaEvaluator karvaEvaluator = new KarvaEvaluator();
	public INode[] bestIndividual=null;
	public TableModel data;
	private ThreadLocal<Double> sepalLength;
	private ThreadLocal<Double> sepalWidth;
	private ThreadLocal<Double> petalLength;
	private ThreadLocal<Double> petalWidth;
	
	private void go() {
		data =  FileUtil.loadToTableModel("sample-data/iris.csv", new Object[]{"sepalLength", "sepalWidth", "petalLength", "petalWidth", "Class"});
		
		List<INodeFactory> factories = new ArrayList<INodeFactory>();

		sepalLength = new ThreadLocal<Double>();
		sepalWidth = new ThreadLocal<Double>();
		petalLength = new ThreadLocal<Double>();
		petalWidth = new ThreadLocal<Double>();
		
		 
		factories.add(new DoubleDecisionNodeFactory("sepalLength", 4.0, 8.0, sepalLength));
		factories.add(new DoubleDecisionNodeFactory("sepalWidth", 1.5, 5, sepalWidth));
		factories.add(new DoubleDecisionNodeFactory("petalLength", .5, 7.5, petalLength));
		factories.add(new DoubleDecisionNodeFactory("petalWidth", 0.0, 3.0, petalWidth));
		factories.add(new NominalTerminalFactory(new Object[] {"Iris-setosa", "Iris-versicolor", "Iris-virginica"}));

		GeneFactory factory = new GeneFactory(factories, GENE_SIZE);

		List<EvolutionaryOperator<INode[]>> operators = new ArrayList<EvolutionaryOperator<INode[]>>();
		operators.add(new MutationOperator<INode[]>(factory, new Probability(0.05d)));
		operators.add(new RecombinationOperator<INode[]>(factory, new Probability(0.7d)));
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
		engine.evolve(200, 1, new TargetFitness(.0001, false));
	}

	private double getError(INode[] ind) {
		double error = 0;
		for (int i=0; i<data.getRowCount(); i++) {
			sepalLength.set(Double.parseDouble(data.getValueAt(i, SEPAL_LENGTH).toString()));
			sepalWidth.set(Double.parseDouble(data.getValueAt(i, SEPAL_WIDTH).toString()));
			petalLength.set(Double.parseDouble(data.getValueAt(i, PETAL_LENGTH).toString()));
			petalWidth.set(Double.parseDouble(data.getValueAt(i, PETAL_WIDTH).toString()));
			String result = (String) karvaEvaluator.evaluate(ind);
			error += result.equals(data.getValueAt(i, CLASS)) ? 0.0 : 1.0;
		}
		//int len= karvaEvaluator.length(ind);
		//double lenError = len / GENE_SIZE;
		double lenError = 0.0;
		return error + lenError;
	}
	
	public static void main(String[] args) {
		new IrisDecisionTreeExample().go();	
	}
}

/*
Generation 52268, error = 0.0000000000000000, if (sepalWidth <= 2.236124921630355) { 
if (sepalWidth <= 4.149955072581571) { 
if (sepalWidth <= 3.6438274416436736) { 
if (petalLength <= 4.827358640736768) { 
Iris-versicolor
}
else { 
Iris-virginica
}

}
else { 
if (sepalWidth <= 1.593658043651235) { 
Iris-versicolor
}
else { 
Iris-versicolor
}

}

}
else { 
if (sepalLength <= 5.870519490165783) { 
if (sepalLength <= 5.6539515328023064) { 
Iris-virginica
}
else { 
Iris-virginica
}

}
else { 
if (sepalWidth <= 2.821721762570811) { 
Iris-setosa
}
else { 
Iris-versicolor
}

}

}

}
else { 
if (sepalLength <= 6.0956297576347405) { 
if (petalWidth <= 1.6382438460637552) { 
if (petalWidth <= 0.6876236266363729) { 
Iris-setosa
}
else { 
Iris-versicolor
}

}
else { 
if (sepalWidth <= 3.049244467496116) { 
Iris-virginica
}
else { 
Iris-versicolor
}

}

}
else { 
if (petalLength <= 5.060429860949242) { 
if (petalWidth <= 1.766884331638058) { 
Iris-versicolor
}
else { 
Iris-virginica
}

}
else { 
Iris-virginica
}

}

}

*/