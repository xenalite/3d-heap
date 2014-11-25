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
				System.out.println("Stack Node: " + stackNode.getName());
				if(currentLevel < levels.size())
				{
					System.out.println("Update "+currentLevel);
					try
					{
						updateCurrentLevel(stackNode);
					} catch	(Exception e)
					{
						System.out.println("Caught Exception in updateCurrentLevel"+e.getMessage());
						currentLevel++;
					}
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
		
		//make a new levelGraph and add it to the list of levels
		HeapGraphLevel levelGraph = new HeapGraphLevel(currentLevel);
		levels.add(levelGraph);

		//TODO I want to change this to 'addRoot' to be more clear
		//Build the root of the levelGraph (add's it to 3D scene and layout)
		//TODO add node to a set of all nodes so we can connect between levels
		levelGraph.buildNode(stackNode, this);

		//If the stacknode only has primitive values
		if(!stackNode.hasReference()){
			//Update the position in the Layout World Space
			levelGraph.layout.layout(stackNode);
			//Update the root position in the 3D world space
			stackNode.updatePosition();
			currentLevel++;
		}
		else {//The stacknode has references

			//Get the references and build all the nodes, adding them to the layout world and 3D space
			//Add edges between the nodes
			Collection<Node> nodesOnThisLayer = stackNode.getReferences();
			buildNodes(stackNode, levelGraph);
			addEdges(stackNode, levelGraph);

			// Add the edge from stack to the first object on the heap
			levelGraph.addEdge(new HeapEdge(), stackNode, (Node) stackNode.getValue());

			//Layout the nodes on this level with the stackNode as the root
			levelGraph.layout.layout(stackNode);
			//update the 3D positions of all the nodes
			//TODO this could be replaces with a single foreach over the layout's vertices provided the nodes have all been correctly added to the graph?
			stackNode.updatePosition();
			for (Node n : nodesOnThisLayer) {
				n.updatePosition();
			}

			//Build the edges in 3D space
			//We want to do this after the nodes are created and positioned since the lines are expensive
			buildEdges(levelGraph, stackNode);
			currentLevel++;
		}
	}


	protected void updateCurrentLevel(StackNode stackNode)
	{
		HeapGraphLevel levelGraph = levels.get(currentLevel);
		boolean stackNodeHasChanged = false;
		//TODO check changes to rootnode
		//If the stacknode only has primitive values
		if(!stackNode.hasReference()){
			//Update the position in the Layout World Space
			levelGraph.layout.layout(stackNode);
			//Update the root position in the 3D world space
			stackNode.updatePosition();
			currentLevel++;
			return;
		}

		boolean nodesHaveChanged = false;
		Collection<Node> nodesOnThisLayer = stackNode.getReferences();

		//Delete the cubes that no longer exist
		for (Node n : levelGraph.getVertices())
		{
			if(!(n instanceof StackNode) && !nodesOnThisLayer.contains(n))
			{
				//TODO check that there are no references to it from another level?
				if(levelGraph.getInEdges(n).size() == 0)
				{
					for (HeapEdge edge : levelGraph.getOutEdges(n))
					{
						removeShapeFrom3DSpace(edge.getLine());
					}
					removeShapeFrom3DSpace(n.getGeometry());
					levelGraph.removeVertex(n);
				}
			}
		}

		//Try to build new nodes
		for (Node n : nodesOnThisLayer)
		{
			//will return true if a new node is added
			nodesHaveChanged |= levelGraph.buildNode(n, this);
		}

		boolean edgesHaveChanged = false;
		// From stack to the first object on the heap
		if(stackNode.hasReference()) {
			//TODO should probably check if the type of the stacknode changed?
			edgesHaveChanged |= levelGraph.addEdge(new HeapEdge(), stackNode, (Node) stackNode.getValue());
		}

		for (Node n : nodesOnThisLayer) {
			for(Node child : n.getReferences())
				if(levelGraph.addEdge(new HeapEdge(), n, child))
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
			for (Node n : nodesOnThisLayer) {
				//TODO probably should do this using graph.getVertices
				n.updatePosition();
			}
			//TODO check edges
			buildEdges(levelGraph, stackNode);
			for (Node n : nodesOnThisLayer) {
				buildEdges(levelGraph, n);
			}

		}


		currentLevel++;
	}

	private void addEdges(Node n, HeapGraphLevel level) {
		for(Node child : n.getReferences()) {
			level.addEdge(new HeapEdge(), n, child);
			addEdges(child, level);
		}
	}

	private void buildNodes(Node n, HeapGraphLevel level) {
		//TODO add nodes to a set of allNodes so we can connect between levels
		level.buildNode(n, this);
		for(Node child : n.getReferences())
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
