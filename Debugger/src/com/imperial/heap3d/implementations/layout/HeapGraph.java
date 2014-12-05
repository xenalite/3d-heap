package com.imperial.heap3d.implementations.layout;

import com.google.common.eventbus.EventBus;
import com.graphics.shapes.Colour;
import com.graphics.shapes.Shape;
import com.heap3d.layout.GraphImpl;
import com.imperial.heap3d.implementations.events.ProcessEvent;
import com.imperial.heap3d.implementations.events.ProcessEventType;
import com.imperial.heap3d.implementations.layout.animation.Animation;
import com.imperial.heap3d.implementations.layout.animation.IAnimation;
import com.imperial.heap3d.implementations.layout.animation.NullAnimation;
import com.imperial.heap3d.implementations.layout.animation.SelectedAnimation;
import com.imperial.heap3d.implementations.snapshot.Node;
import com.imperial.heap3d.implementations.snapshot.StackNode;
import com.imperial.heap3d.utilities.NodesComparator;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class HeapGraph {

    private List<HeapGraphLevel> levels = new LinkedList<>();
    private int currentLevel = 0;

    private Collection<StackNode> _stackNodes = new ArrayList<>();
    private Collection<StackNode> _nodesToBe = null;
    private EventBus _eventBus;
    protected Map<Node, Node> allHeapNodes = new HashMap<>();
    protected GraphImpl<Node, HeapEdge> interLevelGraph = new GraphImpl<>();

    private IAnimation animation = new NullAnimation();
    private SelectedAnimation selectedAnimation;

    private final IRenderEngine _renderEngine;
    private final Lock LOCK = new ReentrantReadWriteLock().writeLock();

    public HeapGraph(IRenderEngine renderEngine, EventBus eventBus) {
        _renderEngine = renderEngine;
        _eventBus = eventBus;
        _eventBus.register(this);
    }

    private void resetStack() {
        System.out.println("Start Before Loop ---------------------------------------------");
        currentLevel = 0;
        for (StackNode stackNode : _stackNodes) {
            System.out.println("Stack Node: " + stackNode.getName());
            //We are looking at an old portion of the stack
            if (currentLevel < levels.size()) {
                try {
                    updateCurrentLevel(stackNode);
                } catch (Exception e) {
                    System.err.println("Caught Exception in updateCurrentLevel " + e);
                    //something went wrong, update current level so that the program keeps working
                    //TODO remove this eventually
                    currentLevel++;
                }
            } else {
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

        if (_stackNodes.size() < levels.size()) {
            int diff = levels.size() - _stackNodes.size();
            System.out.println("Diff " + diff);
            for (int i = levels.size() - 1; i >= currentLevel; i--) {
                //TODO change levels to a stack or deque or hashmap
                //needs to be done in reverse, since remove level uses id to remove
                HeapGraphLevel level = levels.get(i);
                assert (level.id == i);
                removeLevel(level);
            }
        }


        System.out.println("End Before Loop =====================================");

    }

    public void inLoop() {
        if (animation.executeStepAndCheckIfDone()) {
            try {
                buildEdges();
            } catch (Exception ignored) {
            }
            animation = new NullAnimation();
        }

        boolean newNodes = receiveNodes();
        if(newNodes) {
            _eventBus.post(_stackNodes);
            System.out.println("New Stack");
            Set<Node> oldHeapNodes = allHeapNodes.values().stream().collect(Collectors.toSet());
            resetStack();
            animation.finalise();
            animation = new Animation(oldHeapNodes, allHeapNodes.values().stream().collect(Collectors.toSet()));
        }

        Shape selected = _renderEngine.getSelectedShape();

        if (selectedAnimation != null && selected != null) {
            selectedAnimation.step();
        } else if (selectedAnimation != null) {
            selectedAnimation.finish();
            selectedAnimation = null;
        }

        if (selected != null && selected != lastSelectedShape) {
            lastSelectedShape = selected;

            for (Node node : allHeapNodes.values()) {
                if (node.getGeometry() == selected) {
                    String prims = "Node Selected! Primitives : " + node.getPrimitives();
                    System.out.println(prims);
                    //TODO send control event

                    if (selectedAnimation != null) {
                        selectedAnimation.finish();
                        selectedAnimation = null;
                    }

                    selectedAnimation = new SelectedAnimation(node);

                    String message = String.format("Selected Node: %s \nchildren: %s \nprimitives: %s", node.getName(), node.getReferences(), node.getPrimitives());
                    _eventBus.post(new ProcessEvent(ProcessEventType.SELECT, message));

                    break;
                }
            }
        }
    }

    private Shape lastSelectedShape = null;

    protected void addLevel(StackNode stackNode) {

        //make a new levelGraph and add it to the list of levels
        HeapGraphLevel levelGraph = new HeapGraphLevel(currentLevel);
        levels.add(levelGraph.id, levelGraph);

        //If the stacknode only has primitive values
        if (!stackNode.hasReference()) {
            //build the root / stackNode
            levelGraph.buildNode(stackNode, _renderEngine);
            //Update the position in the Layout World Space
            levelGraph.layout.layout(stackNode);
            //Update the root position in the 3D world space
            stackNode.updatePosition();
            currentLevel++;
        } else {//The stacknode has references

            //Build all the nodes(including the root), adding them to the layout world and 3D space
            //Add edges between the nodes
            buildGraph(stackNode, levelGraph);

            //Layout the nodes on this level with the stackNode as the root
            levelGraph.layout.layout(stackNode);
            //update the 3D positions of all the nodes
            for (Node n : levelGraph.getVertices()) {
                n.updatePosition();
            }
            currentLevel++;
        }

    }


    protected void updateCurrentLevel(StackNode stackNode) {
        NodesComparator comparator = new NodesComparator();

        //Get the node at the current level
        HeapGraphLevel levelGraph = levels.get(currentLevel);
        StackNode oldStackNode = levelGraph.getRoot();

        if (oldStackNode.equals(stackNode)) {
            //Same stacknode
            if (comparator.compare(oldStackNode, stackNode)) {
                //no changes to the layout, simply increment the current level
                System.out.println("No changes " + currentLevel);
                currentLevel++;
            } else {
                //same node, but different children
                System.out.println("Need to rebuild the graph!!!");

                //If the stacknode only has primitive values
                if (!stackNode.hasReference()) {
                    //no change to layout since primitives exist inside the object
                    currentLevel++;
                } else {
                    //Rebuild th graph
                    //Remove delete vertices
                    //Remove any interLevel edges asociated with them?
//                    rebuildGraph(stackNode, levelGraph);
//
//                    //Layout the nodes on this level with the stackNode as the root
//                    levelGraph.layout.layout(stackNode);
//                    //update the 3D positions of all the nodes
//                    for (Node n : levelGraph.getVertices()) {
//                        n.updatePosition();
//                    }
                    removeLevel(levelGraph);
                    addLevel(stackNode);
                }
            }
        } else {
            System.out.println("Nodes not equal, so adding a new level " + currentLevel);
            //TODO deal with reconnecting?
            removeLevel(levelGraph);
            addLevel(stackNode);
        }

    }

    private void buildEdges() {
        for (HeapGraphLevel levelGraph : levels) {
            //Build the edges in 3D space
            //We want to do this after the nodes are created and positioned since the lines are expensive
            for (Node n : levelGraph.getVertices()) {
                Collection<HeapEdge> outEdges = levelGraph.layout.getGraph().getOutEdges(n);
                for (HeapEdge edge : outEdges) {
                    Node child = levelGraph.layout.getGraph().getOpposite(n, edge);
                    if (child.getGeometry() != null) {
                        edge.connect(n, child, new Colour(1, 1, 1), _renderEngine);
                    } else {
                        System.err.println("Can't add null edge");
                    }
                }
                //TODO add interlevel edges
                Collection<HeapEdge> edges = interLevelGraph.getOutEdges(n);
                if (edges != null) {
                    for (HeapEdge edge : edges) {
                        Node from = interLevelGraph.getSource(edge);
                        Node to = interLevelGraph.getDest(edge);
                        if (from.getGeometry() == null || to.getGeometry() == null) {
                            System.out.println("Some interlevel node was null");
                        } else {
                            System.out.println("Creating 3D edge between levels");
                            edge.connect(from, to, new Colour(1, 1, 1), _renderEngine);
                        }
                    }
                }
            }
        }
    }

    /**
     * Adds all the *NEW* nodes reachable from root to level
     * Adds edges between nodes in level
     */
    private void buildGraph(StackNode root, HeapGraphLevel level) {
        buildNodes(root, level);

        for (Node node : level.getVertices()) {
            for (Node child : node.getReferences()) {
                assert allHeapNodes.containsKey(child);
                if (child.getLevel() == node.getLevel()) {
                    level.addEdge(new HeapEdge(), node, child);
                }
            }
        }
    }

    /**
     * Adds new nodes to the level
     * Removes nodes which no longer exist
     * Adds new edges between nodes in level
     */
    private void rebuildGraph(StackNode root, HeapGraphLevel level) {
        buildNodes(root, level);

        for (Node node : level.getVertices()) {
            for (Node child : node.getReferences()) {
                assert allHeapNodes.containsKey(child);
                if (child.getLevel() == node.getLevel()) {
                    level.addEdge(new HeapEdge(), node, child);
                }
            }
        }
    }

    private void buildNodes(Node n, HeapGraphLevel level) {
        if (!allHeapNodes.containsKey(n)) {
            allHeapNodes.put(n, n);
            level.buildNode(n, _renderEngine);
            java.util.List<Node> references = n.getReferences();
            for (Node child : references) {
                if (allHeapNodes.containsKey(child)) {
                    //Use the existing child instead of the new one
                    child = allHeapNodes.get(child);
                    //Adds nodes and edge to graph
                    System.out.println(String.format("Adding Interlevel: %s to %s", n, child));
                    interLevelGraph.addEdge(new HeapEdge(), n, child);
                } else {
                    buildNodes(child, level);
                }
            }
        } else {
            //The node already exits in all nodes
            //System.out.println("Already have node: " + n);
        }

    }

    private void removeLevel(HeapGraphLevel levelGraph) {
        System.out.println("Removing level " + levelGraph.id);
        for (Node n : levelGraph.getVertices()) {
            try {
                removeNode(n, levelGraph);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        this.levels.remove(levelGraph.id);
    }

    private void removeNode(Node n, HeapGraphLevel levelGraph) {
        for (HeapEdge edge : levelGraph.getOutEdges(n)) {
            _renderEngine.removeFrom3DSpace(edge.getLine());
//            removeShapeFrom3DSpace(edge.getLine());
        }
        _renderEngine.removeFrom3DSpace(n.getGeometry());
//        removeShapeFrom3DSpace(n.getGeometry());
        synchronized (interLevelGraph) {
            Collection<HeapEdge> outEdges = interLevelGraph.getOutEdges(n);
            if (outEdges != null) {
                for (HeapEdge edge : outEdges) {
                    _renderEngine.removeFrom3DSpace(edge.getLine());
//                    removeShapeFrom3DSpace(edge.getLine());
                    interLevelGraph.removeEdge(edge);
                    System.out.println("Removing interlevel outedge " + edge);
                }
            }

            Collection<HeapEdge> inEdges = interLevelGraph.getInEdges(n);
            if (inEdges != null) {
                for (HeapEdge edge : inEdges) {
                    System.out.println("Removing interlevel inedge " + edge);
                    _renderEngine.removeFrom3DSpace(edge.getLine());
//                    removeShapeFrom3DSpace(edge.getLine());
                }
            }


            interLevelGraph.removeVertex(n);
        }
        allHeapNodes.remove(n);
    }

    public void giveNodes(Collection<StackNode> nodesToBe) {
        LOCK.lock();
        _nodesToBe = nodesToBe;
        LOCK.unlock();
    }

    public boolean receiveNodes() {
        boolean receivedNewNodes;
        LOCK.lock();
        receivedNewNodes = _nodesToBe != null;
        if(receivedNewNodes)
            _stackNodes = _nodesToBe;
        _nodesToBe = null;
        LOCK.unlock();

        return receivedNewNodes;
    }

    public void initialise() {
        _renderEngine.clear3DSpace();
    }

    public void dispose() {
        _eventBus.unregister(this);
    }
}
