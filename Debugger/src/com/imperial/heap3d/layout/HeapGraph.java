package com.imperial.heap3d.layout;

import com.graphics.RenderEngine;
import com.graphics.shapes.Shape;
import com.imperial.heap3d.snapshot.IDNode;
import com.imperial.heap3d.snapshot.Node;
import com.imperial.heap3d.snapshot.StackNode;
import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class HeapGraph extends RenderEngine {

	private Set<HeapGraphLevel> levels = new HashSet<HeapGraphLevel>();
	private int currentLevel = 0;
	private boolean newStack = false;
	private Set<StackNode> stackNodes;

	public HeapGraph(Canvas canvas, Set<StackNode> stackNodes) {
		super(canvas);
		this.stackNodes = stackNodes;
	}
	
	public HeapGraph(Set<StackNode> stackNodes) {
		super("Heap Visualizer", 1280, 720, false);
		this.stackNodes = stackNodes;
	}

	@Override
	protected void beforeLoop() {
		super.setBackgroundColour(0.1f, 0.1f, 0.1f, 1f);
		for(StackNode stackNode : stackNodes)
			addLevel(stackNode);
	}

	@Override
	protected void inLoop() {
		if(newStack){
			beforeLoop();
			newStack = false;
		}
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
	
	public void finish(){
		super.breakOutOfLoop();
	}
	
	public void giveStackNodes(Set<StackNode> stackNodes){
		super.clearShapesFrom3DSpace();
		currentLevel = 0;
		levels.clear();
		this.stackNodes = stackNodes;
		newStack = true;
	}
}
