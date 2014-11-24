package com.imperial.heap3d.layout;

import com.graphics.RenderEngine;
import com.graphics.entities.Entity;
import com.graphics.shapes.Colour;
import com.graphics.shapes.Shape;
import com.imperial.heap3d.snapshot.IDNode;
import com.imperial.heap3d.snapshot.Node;
import com.imperial.heap3d.snapshot.StackNode;

import java.awt.*;
import java.util.*;

public class HeapGraph extends RenderEngine {

	private java.util.List<HeapGraphLevel> levels = new LinkedList<>();
	private int currentLevel = 0;
	private boolean newStack = false;
	private Collection<StackNode> stackNodes;

	public HeapGraph(Canvas canvas, Collection<StackNode> stackNodes) {
		super(canvas);
		this.stackNodes = stackNodes;
	}
	
	public HeapGraph(Collection<StackNode> stackNodes) {
		super("Heap Visualizer", 1280, 720, false);
		this.stackNodes = stackNodes;
	}

	@Override
	protected void beforeLoop() {
		super.setBackgroundColour(0.1f, 0.1f, 0.1f, 1f);
		System.out.println("Start Before Loop");
			for(StackNode stackNode : stackNodes)
			{
				if(currentLevel < levels.size())
				{
					System.out.println("Update "+currentLevel);
					updateCurrentLevel(stackNode);
				} else
				{
					System.out.println("Add at "+currentLevel);
					addLevel(stackNode);
				}

			}
		System.out.println("End Before Loop");
	}

	@Override
	protected void inLoop() {
		if(newStack){
			beforeLoop();
			newStack = false;
		}
		if(takeScreenShot){
			this.captureScreen(path, name);
			takeScreenShot = false;
		}
	}

	@Override
	protected void afterLoop() {

	}

	protected void addLevel(StackNode stackNode) {
		
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
		levelGraph.addEdge( new HeapEdge(edgeCount++), stackNode, (Node) stackNode.getValue());
		
		for (IDNode n : nodesOnThisLayer) {
			for(IDNode child : n.getChildren())
				levelGraph.addEdge(new HeapEdge(edgeCount++), n, child);
			
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

	protected void updateCurrentLevel(StackNode stackNode)
	{
		HeapGraphLevel levelGraph = levels.get(currentLevel);
		boolean stackNodeHasChanged = false;
		//stacknode has been updated
//		if(! levelGraph.getRoot().equals(stackNode))
//		{
//			stackNodeHasChanged = true;
//		}
		if(!stackNode.doesRefNode()){
			currentLevel++;
			return;
		}


		boolean nodesHaveChanged = false;
		Set<IDNode> nodesOnThisLayer = stackNode.walkHeap();
		for (IDNode n : nodesOnThisLayer)
		{
			//will return true if a new node is added
			nodesHaveChanged |= levelGraph.buildNode(n, this);
		}

		boolean edgesHaveChanged = false;
		int edgeCount = 0;
		// From stack to the first object on the heap
		edgesHaveChanged |= levelGraph.addEdge( new HeapEdge(edgeCount++), stackNode, (Node) stackNode.getValue());

		for (IDNode n : nodesOnThisLayer) {
			for(IDNode child : n.getChildren())
				if(levelGraph.addEdge(new HeapEdge(edgeCount++), n, child))
				{
					//new edge added
					edgesHaveChanged = true;
				} else
				{
					//old edge

				}

		}



		if(edgesHaveChanged|| nodesHaveChanged || stackNodeHasChanged )
		{
			System.out.println("Something changed: updating the layout");
			levelGraph.layout.layout(stackNode);
			stackNode.updatePosition();
			for (IDNode n : nodesOnThisLayer) {
				n.updatePosition();
			}
			//TODO check edges
			makeEdges(levelGraph, stackNode);
			for (IDNode n : nodesOnThisLayer) {
				makeEdges(levelGraph, n);
			}

		}


		currentLevel++;
	}
	
	private void makeEdges(HeapGraphLevel levelGraph, Node n){
		Collection<HeapEdge> outEdges = levelGraph.layout.getGraph().getOutEdges(n);
		for(HeapEdge edge : outEdges){
			Node child = levelGraph.layout.getGraph().getOpposite(n,edge);
			if(child.getGeometry() != null && n.getGeometry() != null){
				edge.connect(n,child, new Colour(1, 1, 1), this);
			}
		}
	}

	@Override
	public void addShapeTo3DSpace(Shape geometry) {
		super.addShapeTo3DSpace(geometry);
	}


	public void removeShapeFrom3DSpace(Shape shape)
	{
		super.removeShapeFrom3DSpace(shape);
	}

	public void finish(){
		super.breakOutOfLoop();
	}
	
	public void giveStackNodes(Collection<StackNode> stackNodes){
		//Don't remove geometry
		//Reset the current level
		currentLevel = 0;
		//Don't clear the levels since we need them for comparison
		//Update the new stackNodes we would like to add
		this.stackNodes = stackNodes;
		newStack = true;
	}


	private boolean takeScreenShot;
	private String path;
	private String name;
	
	public void screenShot(String path, String name ){
		//TODO this could be done a lot better
		takeScreenShot = true;
		this.path = path;
		this.name = name;
	}
}
