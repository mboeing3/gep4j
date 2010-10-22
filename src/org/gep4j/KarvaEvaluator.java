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
package org.gep4j;

public class KarvaEvaluator {
	public Object evaluate(INode[] nodes) {
		NodeEvaluation root = new NodeEvaluation();
		
		root.node = nodes[0];
		processLevel(new NodeEvaluation[] {root}, nodes, 1);
		return root.evaluate();
	}
	
	public String print(INode[] nodes) {
		NodeEvaluation root = new NodeEvaluation();
		
		root.node = nodes[0];
		processLevel(new NodeEvaluation[] {root}, nodes, 1);
		return root.node.print(root.nodeEvals);
	}
	
	// TODO: finish
	public int length(INode[] nodes) {
		NodeEvaluation root = new NodeEvaluation();
		
		root.node = nodes[0];
		processLevel(new NodeEvaluation[] {root}, nodes, 1);
		int length = getLength(root);
		return length;
	}
	
	private int getLength(NodeEvaluation e) {
		int len = 1;
		
		return 0;
	}

	private void processLevel(NodeEvaluation parent[], INode[] nodes, int currentNode) {
		int length = 0;
		
		for (NodeEvaluation n : parent) {
			length += n.node.getAirity();
		}
		
		if (length == 0) {
			return;
		}
		
		NodeEvaluation nodeEvals[] = new NodeEvaluation[length];

		int currentNodeEval = 0;
		for (NodeEvaluation n : parent) {
			NodeEvaluation children[] = new NodeEvaluation[n.node.getAirity()];
			for (int i=0; i<children.length; i++) {
				NodeEvaluation nodeEval = new NodeEvaluation();
				nodeEval.node = nodes[currentNode++];
				nodeEvals[currentNodeEval++] = nodeEval;
				children[i] = nodeEval;
			}
			n.nodeEvals = children;
		}
		
		processLevel(nodeEvals, nodes, currentNode);
	}

	public String print(GepIndividual ind) {
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<ind.getGeneCount(); i++) {
			buf.append(String.format("\n\n***** Gene %d *****\n\n", i));
			buf.append(print(ind.getGene(i)));
		}
		return buf.toString();
	}
}
