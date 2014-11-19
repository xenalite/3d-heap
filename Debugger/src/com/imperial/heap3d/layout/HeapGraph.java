package com.imperial.heap3d.layout;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.graphics.RenderEngine;
import com.graphics.shapes.Shape;
import com.imperial.heap3d.snapshot.HeapNode;
import com.imperial.heap3d.snapshot.IDNode;
import com.imperial.heap3d.snapshot.Node;
import com.imperial.heap3d.snapshot.StackNode;
import com.sun.glass.ui.Window.Level;

public class HeapGraph extends RenderEngine{

	private Set<HeapNode> allNodes = new HashSet<HeapNode>();
	private Set<HeapGraphLevel> levels = new HashSet<HeapGraphLevel>();

	int currentLevel = 0;
	private Random rand;

	public HeapGraph(Set<StackNode> stackNodes) {
		super("Heap Visualizer!!!", 1280, 720, false);
		super.setBackgroundColour(0f, 0f, 0f, 1f);
		for(StackNode stackNode : stackNodes)
			addLevel(stackNode);
		super.start();
	}

	@Override
	protected void beforeLoop() {
		rand = new Random();
	}

	static int level = 0;

	@Override
	protected void inLoop() {
		
	}


	@Override
	protected void afterLoop() {

	}

	public void addLevel(StackNode stackNode) {
		
		// stackNode is root
		HeapGraphLevel levelGraph = new HeapGraphLevel(currentLevel);
		levels.add(levelGraph);

		//TODO I want to change this to 'addRoot' to be more clear
		levelGraph.buildNode(stackNode, this);
		
		if(!stackNode.doesRefNode()){
			levelGraph.layout.layout(stackNode);
			stackNode.updatePosition();
			currentLevel++;
			return;
		}
		
		Set<IDNode> nodesOnThisLayer = stackNode.walkHeap();
		
		for (IDNode n : nodesOnThisLayer) {
			levelGraph.buildNode(n, this);
		}

		int edgeCount = 0;
		
		// From stack to the first object on the heap
		levelGraph.addEdge( new HeapEdge(edgeCount++), stackNode, (Node) stackNode.getValue()); //TODO needs to be directed edge
		
		for (IDNode n : nodesOnThisLayer) {
			for(IDNode child : n.getChildren())
				levelGraph.addEdge(new HeapEdge(edgeCount++), n, child); //TODO needs to be directed edge
			
		}
	
		levelGraph.layout.layout(stackNode);
		stackNode.updatePosition();
		for (IDNode n : nodesOnThisLayer) {
			n.updatePosition();
		}

		makeEdges(levelGraph, stackNode);
		for (IDNode n : nodesOnThisLayer) {
			makeEdges(levelGraph, n);
		}

		currentLevel++;
	}
	
	
	private void makeEdges(HeapGraphLevel levelGraph, Node n){
		Collection<HeapEdge> outEdges = levelGraph.layout.getGraph().getOutEdges(n);
		for(HeapEdge edge : outEdges){

			System.out.println("helo---------------");
			Node child = levelGraph.layout.getGraph().getOpposite(n,edge);
			if(child.getGeometry() != null){
				edge.connect(n,child, levelGraph.linecolor, this);
			}
		}
	}

	@Override
	public void addShapeTo3DSpace(Shape geometry) {
		super.addShapeTo3DSpace(geometry);
	}

}
