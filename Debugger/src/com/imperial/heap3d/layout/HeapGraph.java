package com.imperial.heap3d.layout;

import com.graphics.RenderEngine;
import com.graphics.shapes.Colour;
import com.graphics.shapes.Shape;
import com.imperial.heap3d.snapshot.Node;
import com.imperial.heap3d.snapshot.StackNode;

import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;

public class HeapGraph extends RenderEngine {

	private Collection<HeapGraphLevel> levels = new LinkedList<>();
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
		super.setBackgroundColour(0.1f, 0.1f, 0.1f, 0.1f);
		stackNodes.forEach(this::addLevel);
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

	public void addLevel(StackNode stackNode) {
		
		// stackNode is root
		HeapGraphLevel levelGraph = new HeapGraphLevel(currentLevel);
		levels.add(levelGraph);

		//TODO I want to change this to 'addRoot' to be more clear
		levelGraph.buildNode(stackNode, this);

		if(!stackNode.hasReference()){
			levelGraph.layout.layout(stackNode);
			stackNode.updatePosition();
			currentLevel++;
			return;
		}
		
		Collection<Node> nodesOnThisLayer = stackNode.getReferences();
		buildNodes(stackNode, levelGraph);
		addEdges(stackNode, levelGraph);

		// TODO -- directed edges!
		// From stack to the first object on the heap
		levelGraph.addEdge( new HeapEdge(), stackNode, (Node) stackNode.getValue());
		
		levelGraph.layout.layout(stackNode);
		stackNode.updatePosition();
		for (Node n : nodesOnThisLayer) {
			n.updatePosition();
		}

		makeEdges(levelGraph, stackNode);
		currentLevel++;
	}

	private void addEdges(Node n, HeapGraphLevel level) {
		for(Node child : n.getReferences()) {
			level.addEdge(new HeapEdge(), n, child);
			addEdges(child, level);
		}
	}

	private void buildNodes(Node n, HeapGraphLevel level) {
		level.buildNode(n, this);
		for(Node child : n.getReferences())
			buildNodes(child, level);
	}
	
	private void makeEdges(HeapGraphLevel levelGraph, Node n) {
		Collection<HeapEdge> outEdges = levelGraph.layout.getGraph().getOutEdges(n);
		for(HeapEdge edge : outEdges) {
			Node child = levelGraph.layout.getGraph().getOpposite(n,edge);
			if(child.getGeometry() != null) {
				edge.connect(n,child, new Colour(1, 1, 1), this);
			}
		}
		for(Node child : n.getReferences()) {
			makeEdges(levelGraph, child);
		}
	}

	@Override
	public void addShapeTo3DSpace(Shape geometry) {
		super.addShapeTo3DSpace(geometry);
	}
	
	public void finish(){
		super.breakOutOfLoop();
	}
	
	public void giveStackNodes(Collection<StackNode> stackNodes){
		super.clearShapesFrom3DSpace();
		currentLevel = 0;
		levels.clear();
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
