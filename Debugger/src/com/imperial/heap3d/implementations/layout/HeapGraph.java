package com.imperial.heap3d.implementations.layout;

import com.graphics.shapes.Colour;
import com.graphics.shapes.Line;
import com.graphics.shapes.Shape;
import com.heap3d.layout.GraphImpl;
import com.imperial.heap3d.implementations.layout.animation.Animation;
import com.imperial.heap3d.implementations.layout.animation.IAnimation;
import com.imperial.heap3d.implementations.layout.animation.NullAnimation;
import com.imperial.heap3d.implementations.snapshot.Node;
import com.imperial.heap3d.implementations.snapshot.StackNode;
import com.imperial.heap3d.utilities.NodesComparator;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class HeapGraph {

    private List<HeapGraphLevel> _levels = new LinkedList<>();
    private GraphImpl<Node, HeapEdge> interLevelGraph = new GraphImpl<>();

    private Collection<StackNode> _stackNodes = new ArrayList<>();
    private Collection<StackNode> _nodesToBe = null;

    private Map<Node, Shape> nodeToShape = new HashMap<>();

    private IAnimation animation = new NullAnimation();

    private final IRenderEngine _renderEngine;
    private final Lock LOCK = new ReentrantReadWriteLock().writeLock();
    private List<HeapGraphLevel> levelsToUpdate = new ArrayList<>();

    public HeapGraph(IRenderEngine renderEngine) {
        _renderEngine = renderEngine;
    }

    private void resetStack() {
        int currentLevel = 0;
        _renderEngine.removeText();
        for (StackNode stackNode : _stackNodes) {
            if (currentLevel < _levels.size())
                updateLevel(stackNode, currentLevel);
            else
                addLevel(stackNode, currentLevel);
            ++currentLevel;
        }

        if (_stackNodes.size() < _levels.size()) {
            for (int i = _levels.size() - 1; i >= currentLevel; i--) {
                HeapGraphLevel level = _levels.get(i);
                removeLevel(level);
            }
        }
    }

    public void inLoop() {
        if (animation.executeStepAndCheckIfDone()) {
            buildEdges();
            Set<Entry<Node, Shape>> nodes = nodeToShape.entrySet();
            for(Entry<Node, Shape> e : nodes){
            	Node n = e.getKey();
            	Shape s = e.getValue();
            	float[] pos = s.getPosition();
                float[] rot = s.getRotation();
                if(n instanceof StackNode)
                	_renderEngine.printTo3DSpace(pos[0], pos[1], pos[2]+3, rot[0], rot[1], rot[2], 0.2f, n.getName());
                else
                	_renderEngine.printTo3DSpace(pos[0], pos[1], pos[2]+0.4f, rot[0], rot[1], rot[2], 0.05f, n.getName());
            }
            animation = new NullAnimation();
        }

        boolean newNodes = receiveNodes();
        if (newNodes) {
            Set<Entry<Node, Shape>> oldNodes = new HashSet<>(nodeToShape.entrySet());
            resetStack();
            animation.finalise();
            animation = new Animation(oldNodes, nodeToShape.entrySet());
        }
    }

    private void updatePosition(Node node) {
        if (!nodeToShape.containsKey(node))
            throw new IllegalStateException("updatePosition");

        HeapGraphLevel level = node.getLevel();
        Shape s = nodeToShape.get(node);
        s.setPosition(level.getX(node), level.getY(), level.getZ(node));
    }

    private void updateLevel(StackNode stackNode, int level) {
        NodesComparator comparator = new NodesComparator();

        HeapGraphLevel levelGraph = _levels.get(level);
        StackNode oldStackNode = levelGraph.getRoot();

        if (oldStackNode.equals(stackNode)) {
            if (comparator.compare(oldStackNode, stackNode)) {
                updatePositions(levelGraph);
            } else {
                if (stackNode.hasReference()) {
                    levelsToUpdate = removeLevel(levelGraph);
                    addLevel(stackNode, level);

                    for(HeapGraphLevel levelToUpdate : levelsToUpdate) {
                        StackNode root = levelToUpdate.getRoot();
                        removeLevel(levelToUpdate);
                        addLevel(root, levelToUpdate.getId());
                    }
                }
            }
        } else {
            levelsToUpdate = removeLevel(levelGraph);
            addLevel(stackNode, level);

            for(HeapGraphLevel levelToUpdate : levelsToUpdate) {
                StackNode root = levelToUpdate.getRoot();
                removeLevel(levelToUpdate);
                addLevel(root, levelToUpdate.getId());
            }
        }
    }

    //region add
    private void addLevel(StackNode stackNode, int level) {
        HeapGraphLevel levelGraph = new HeapGraphLevel(level);
        _levels.add(levelGraph.getId(), levelGraph);

        if (!stackNode.hasReference() && levelGraph.addVertex(stackNode))
            initialiseNewShape(stackNode);
        else
            buildGraph(stackNode, levelGraph);

        levelGraph.runLayout();
        updatePositions(levelGraph);
    }

    private void buildEdges() {
        for (HeapGraphLevel levelGraph : _levels) {
            for (Node n : levelGraph.getVertices()) {
                Collection<HeapEdge> outEdges = levelGraph.getLayout().getGraph().getOutEdges(n);
                for (HeapEdge edge : outEdges) {
                    Node child = levelGraph.getLayout().getGraph().getOpposite(n, edge);
                    edge.connect(nodeToShape.get(n), nodeToShape.get(child), new Colour(1, 1, 1), _renderEngine);
                }
                Collection<HeapEdge> edges = interLevelGraph.getOutEdges(n);
                if (edges != null) {
                    for (HeapEdge edge : edges) {
                        Node from = interLevelGraph.getSource(edge);
                        Node to = interLevelGraph.getDest(edge);
                        edge.connect(nodeToShape.get(from), nodeToShape.get(to), new Colour(1, 1, 1), _renderEngine);
                    }
                }
            }
        }
    }

    private void buildGraph(StackNode root, HeapGraphLevel level) {
        buildNodes(root, level);

        for (Node node : level.getVertices()) {
            node.getReferences().stream()
                    .filter(child -> child.getLevel() == node.getLevel())
                    .forEach(child -> level.addEdge(new HeapEdge(), node, child));
        }
    }

    private void initialiseNewShape(Node node) {
        if (nodeToShape.containsKey(node))
            throw new IllegalStateException("initialiseNewShape");

        Shape s = node.createShape();
        nodeToShape.put(node, s);
        _renderEngine.addTo3DSpace(s);
    }

    private void buildNodes(Node node, HeapGraphLevel level) {
        if (nodeToShape.containsKey(node) || !level.addVertex(node))
            throw new IllegalStateException("buildNodes");

        initialiseNewShape(node);
        updatePosition(node);

        for (Node child : node.getReferences()) {
            if (nodeToShape.containsKey(child))
                interLevelGraph.addEdge(new HeapEdge(), node, child);
            else
                buildNodes(child, level);
        }
    }
    //endregion

    //region remove
    private List<HeapGraphLevel> removeLevel(HeapGraphLevel levelGraph) {
        List<HeapGraphLevel> levels = new ArrayList<>();
        for (Node node : levelGraph.getVertices()) {
            removeLinesFrom3DSpace(node.getLevel().getOutEdges(node));
            removeLinesFrom3DSpace(interLevelGraph.getOutEdges(node));
            removeLinesFrom3DSpace(interLevelGraph.getInEdges(node));

            Collection<HeapEdge> inEdges = interLevelGraph.getInEdges(node);
            if (!isEmpty(inEdges))
                for(HeapEdge edge : inEdges)
                    levels.add(interLevelGraph.getOpposite(node, edge).getLevel());
            removeNodeFromGraphAnd3DSpace(node);
        }
        _levels.remove(levelGraph.getId());
        return levels;
    }

    private void removeNodeFromGraphAnd3DSpace(Node node) {
        Shape s = nodeToShape.get(node);
        _renderEngine.removeFrom3DSpace(s);
        interLevelGraph.removeVertex(node);
        nodeToShape.remove(node);
    }

    private <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    private void updatePositions(HeapGraphLevel level) {
        level.getPositionsToUpdate().forEach(this::updatePosition);
    }

    private void removeLinesFrom3DSpace(Collection<HeapEdge> edges) {
        if (edges != null)
            for (HeapEdge edge : edges){

                List<Line> lines = edge.getLines();
                if(lines != null) {
                    for (Line l : lines)
                        _renderEngine.removeFrom3DSpace(l);
                } else
                {
                    // The lines weren't drawn -> usually means program stepped before finishing the last animations
                }
                _renderEngine.removeFrom3DSpace(edge.getArrow());
            }
    }
    //endregion

    public Set<Entry<Node, Shape>> getCurrentNodes() {
        return nodeToShape.entrySet();
    }

    public void giveNodes(Collection<StackNode> nodesToBe) {
        LOCK.lock();
        _nodesToBe = nodesToBe;
        LOCK.unlock();
    }

    private boolean receiveNodes() {
        boolean receivedNewNodes;
        LOCK.lock();
        receivedNewNodes = _nodesToBe != null;
        if (receivedNewNodes)
            _stackNodes = _nodesToBe;
        _nodesToBe = null;
        LOCK.unlock();

        return receivedNewNodes;
    }
}
