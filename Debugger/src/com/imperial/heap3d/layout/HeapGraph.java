package com.imperial.heap3d.layout;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.graphics.RenderEngine;
import com.graphics.shapes.Colour;
import com.graphics.shapes.Shape;
import com.imperial.heap3d.events.ControlEvent;
import com.imperial.heap3d.events.EventType;
import com.imperial.heap3d.snapshot.Node;
import com.imperial.heap3d.snapshot.StackNode;
import com.imperial.heap3d.utilities.NodesComparator;

import java.awt.*;
import java.util.*;

public class HeapGraph extends RenderEngine {

	private java.util.List<HeapGraphLevel> levels = new LinkedList<>();
	private int currentLevel = 0;
	private boolean newStack = false;
	private Collection<StackNode> stackNodes;
	private EventBus _eventBus;
	protected Set<Node> allHeapNodes = new HashSet<>();

	public HeapGraph(Canvas canvas, Collection<StackNode> stackNodes, EventBus eventBus) {
		super(canvas);
		this.stackNodes = stackNodes;
		_eventBus = eventBus;
		_eventBus.register(this);
	}
	
	public HeapGraph(Collection<StackNode> stackNodes) {
		super("Heap Visualizer", 1280, 720, false);
		this.stackNodes = stackNodes;
	}

	@Subscribe
	public void handleStartEvent(ControlEvent e) {
		if(e.type == EventType.START) {
			currentLevel = 0;
			newStack = false;
			stackNodes.clear();
			levels.clear();
			super.clearShapesFrom3DSpace();
		}
	}

	@Override
	protected void beforeLoop() {
		super.setBackgroundColour(0.1f, 0.1f, 0.1f, 1f);
		System.out.println("Start Before Loop ---------------------------------------------");
		clearShapesFrom3DSpace();
		currentLevel = 0;
		levels.clear();
			for(StackNode stackNode : stackNodes)
			{
				System.out.println("Stack Node: " + stackNode.getName());
                //We are looking at an old portion of the stack
				if(currentLevel < levels.size())
				{
					try {
						updateCurrentLevel(stackNode);
					} catch (Exception e) {
						System.err.println("Caught Exception in updateCurrentLevel " + e);
						//something went wrong, update current level so that the program keeps working
						//TODO remove this eventually
						currentLevel++;
					}
				} else
				{
                    //New node!
					System.out.println(String.format("Adding new stackNode %s at %s", stackNode.getName(), currentLevel));
					try {
						addLevel(stackNode);
					} catch (Exception e) {
						System.err.println("Caught Exception in addLevel " + e);
						//something went wrong, update current level so that the program keeps working
						//TODO remove this eventually
						currentLevel++;
					}
				}

			}
		System.out.println("End Before Loop =====================================");
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
		stackNodes.size();
	}

	protected void addLevel(StackNode stackNode) {
		
		//make a new levelGraph and add it to the list of levels
		HeapGraphLevel levelGraph = new HeapGraphLevel(currentLevel);
		levels.add(levelGraph.id, levelGraph);

		//If the stacknode only has primitive values
		if(!stackNode.hasReference()){
            //build the root / stackNode
            levelGraph.buildNode(stackNode, this);
			//Update the position in the Layout World Space
			levelGraph.layout.layout(stackNode);
			//Update the root position in the 3D world space
			stackNode.updatePosition();
			currentLevel++;
		}
		else {//The stacknode has references

			//Build all the nodes(including the root), adding them to the layout world and 3D space
			//Add edges between the nodes
			buildNodes(stackNode, levelGraph);
			addEdges(stackNode, levelGraph);

			//Layout the nodes on this level with the stackNode as the root
			levelGraph.layout.layout(stackNode);
			//update the 3D positions of all the nodes
			updatePositions(stackNode);

			//Build the edges in 3D space
			//We want to do this after the nodes are created and positioned since the lines are expensive
			buildEdges(levelGraph, stackNode);
			currentLevel++;
		}
	}


	protected void updateCurrentLevel(StackNode stackNode)
	{
		NodesComparator comparator = new NodesComparator();

		//Get the node at the current level
		HeapGraphLevel levelGraph = levels.get(currentLevel);
		StackNode oldStackNode = levelGraph.getRoot();

		if(oldStackNode.equals(stackNode)) {
			//Same stacknode
			if(comparator.compare(oldStackNode, stackNode))
			{
				//no changes to the layout, simply increment the current level
				System.out.println("No changes "+currentLevel);
				currentLevel++;
				return;
			}

		}
			System.out.println("Nodes not equal, so adding a new level " + currentLevel);
			//TODO delete old stackNode
			removeLevel(levelGraph);


			addLevel(stackNode);


	}

	private void addEdges(Node n, HeapGraphLevel level) {
		for(Node child : n.getReferences()) {
			level.addEdge(new HeapEdge(), n, child);
			addEdges(child, level);
		}
	}

	private void buildNodes(Node n, HeapGraphLevel level) {
		//TODO add nodes to a set of allNodes so we can connect between levels
		if(n instanceof Node && !allHeapNodes.contains(n))
		{
			allHeapNodes.add((Node)n);
		}
		level.buildNode(n, this);
        java.util.List<Node> references = n.getReferences();
        for(Node child : references)
			buildNodes(child, level);
	}
	
	private void buildEdges(HeapGraphLevel levelGraph, Node n) {
		Collection<HeapEdge> outEdges = levelGraph.layout.getGraph().getOutEdges(n);
		for(HeapEdge edge : outEdges) {
			Node child = levelGraph.layout.getGraph().getOpposite(n,edge);
			if(child.getGeometry() != null) {
				edge.connect(n,child, new Colour(1, 1, 1), this);
			}
		}
		for(Node child : n.getReferences()) {
			buildEdges(levelGraph, child);
		}
	}

	private void updatePositions(Node n) {

		n.updatePosition();
		java.util.List<Node> references = n.getReferences();
		for(Node child : references)
			updatePositions(child);
	}

	private void removeLevel(HeapGraphLevel levelGraph)
	{
		for (Node n : levelGraph.getVertices() )
		{
			//TODO check that there are no references to it from another level?
			if(levelGraph.getInEdges(n).size() >= 0)
			{
				for (HeapEdge edge : levelGraph.getOutEdges(n))
				{
					removeShapeFrom3DSpace(edge.getLine());
				}
				removeShapeFrom3DSpace(n.getGeometry());
			}
		}
		this.levels.remove(levelGraph.id);
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
