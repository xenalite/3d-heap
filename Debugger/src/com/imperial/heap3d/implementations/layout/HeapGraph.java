package com.imperial.heap3d.implementations.layout;

import com.graphics.shapes.Colour;
import com.graphics.shapes.Shape;
import com.heap3d.layout.GraphImpl;
import com.imperial.heap3d.implementations.layout.animation.Animation;
import com.imperial.heap3d.implementations.layout.animation.IAnimation;
import com.imperial.heap3d.implementations.layout.animation.NullAnimation;
import com.imperial.heap3d.implementations.snapshot.Node;
import com.imperial.heap3d.implementations.snapshot.StackNode;
import com.imperial.heap3d.utilities.NodesComparator;
import com.imperial.heap3d.utilities.Pair;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HeapGraph {

    private List<HeapGraphLevel> levels = new LinkedList<>();
    private GraphImpl<Node, HeapEdge> interLevelGraph = new GraphImpl<>();

    private Collection<StackNode> _stackNodes = new ArrayList<>();
    private Collection<StackNode> _nodesToBe = null;

    private Map<Node, Shape> nodeToShape = new HashMap<>();

    private IAnimation animation = new NullAnimation();
    private int currentLevel = 0;

    private final IRenderEngine _renderEngine;
    private final Lock LOCK = new ReentrantReadWriteLock().writeLock();

    public HeapGraph(IRenderEngine renderEngine) {
        _renderEngine = renderEngine;
    }

    private void resetStack() {
        currentLevel = 0;
        for (StackNode stackNode : _stackNodes) {
            if (currentLevel < levels.size()) {
                updateCurrentLevel(stackNode);
            } else {
                addLevel(stackNode);
            }
        }

        if (_stackNodes.size() < levels.size()) {
            for (int i = levels.size() - 1; i >= currentLevel; i--) {
                HeapGraphLevel level = levels.get(i);
                removeLevel(level);
            }
        }
    }

    private void updatePosition(Node node) {
        if (nodeToShape.containsKey(node)) {
            HeapGraphLevel level = node.getLevel();
            Shape s = nodeToShape.get(node);
            s.setPosition(level.getX(node), level.getY(), level.getZ(node));
        }
        else throw new IllegalStateException("updatePosition");
    }

    private void updateCurrentLevel(StackNode stackNode) {
        NodesComparator comparator = new NodesComparator();

        HeapGraphLevel levelGraph = levels.get(currentLevel);
        StackNode oldStackNode = levelGraph.getRoot();

        if (oldStackNode.equals(stackNode)) {
            if (comparator.compare(oldStackNode, stackNode)) {
                updatePositions(levelGraph);
                currentLevel++;
            } else {
                if (!stackNode.hasReference()) {
                    currentLevel++;
                } else {
                    removeLevel(levelGraph);
                    addLevel(stackNode);
                }
            }
        } else {
            Pair<Node,HeapGraphLevel> pair = removeLevel(levelGraph);
            removeNodeFromGraphAnd3DSpace(pair.first);
            addLevel(stackNode);
        }
    }

    //region add
    private void addLevel(StackNode stackNode) {
        HeapGraphLevel levelGraph = new HeapGraphLevel(currentLevel);
        levels.add(levelGraph.getId(), levelGraph);

        if (!stackNode.hasReference()) {
            if (levelGraph.addVertex(stackNode)) {
                initialiseNewShape(stackNode);
            }
            levelGraph.runLayout();
            updatePositions(levelGraph);
            currentLevel++;
        } else {
            buildGraph(stackNode, levelGraph);
            levelGraph.runLayout();
            updatePositions(levelGraph);
            currentLevel++;
        }
    }

    private void buildEdges() {
        for (HeapGraphLevel levelGraph : levels) {
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
        buildNewNodes(root, level);

        for (Node node : level.getVertices()) {
            node.getReferences().stream()
                    .filter(child -> child.getLevel() == node.getLevel())
                    .forEach(child -> level.addEdge(new HeapEdge(), node, child));
        }
    }

    private void initialiseNewShape(Node node) {
        if(!nodeToShape.containsKey(node)) {
            Shape s = node.createShape();
            nodeToShape.put(node, s);
            _renderEngine.addTo3DSpace(s);
        }
        else throw new IllegalStateException("initialiseNewShape");
    }

    private void buildNewNodes(Node node, HeapGraphLevel level) {
        if (nodeToShape.containsKey(node) || !level.addVertex(node))
            throw new IllegalStateException("buildNewNodes");

        initialiseNewShape(node);
        updatePosition(node);

        for (Node child : node.getReferences()) {
            if (nodeToShape.containsKey(child))
                interLevelGraph.addEdge(new HeapEdge(), node, child);
            else
                buildNewNodes(child, level);
        }
    }
    //endregion

    //region remove
    private Pair<Node,HeapGraphLevel> removeLevel(HeapGraphLevel levelGraph) {
        Pair<Node, HeapGraphLevel> pair = null;
        for (Node node : levelGraph.getVertices()) {
            removeLinesFrom3DSpace(node.getLevel().getOutEdges(node));
            removeLinesFrom3DSpace(interLevelGraph.getOutEdges(node));
            removeLinesFrom3DSpace(interLevelGraph.getInEdges(node));

            Collection<HeapEdge> inEdges = interLevelGraph.getInEdges(node);
            if (isEmpty(inEdges))
                removeNodeFromGraphAnd3DSpace(node);
            else
                pair = Pair.create(node, interLevelGraph.getOpposite(node, inEdges.iterator().next()).getLevel());
        }
        levels.remove(levelGraph.getId());
        return pair;
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
            for (HeapEdge edge : edges)
                _renderEngine.removeFrom3DSpace(edge.getLine());
    }
    //endregion

    public void inLoop() {
        if (animation.executeStepAndCheckIfDone()) {
            buildEdges();
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
