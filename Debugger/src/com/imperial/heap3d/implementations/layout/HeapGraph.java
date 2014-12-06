package com.imperial.heap3d.implementations.layout;

import com.graphics.shapes.Colour;
import com.heap3d.layout.GraphImpl;
import com.imperial.heap3d.implementations.layout.animation.Animation;
import com.imperial.heap3d.implementations.layout.animation.IAnimation;
import com.imperial.heap3d.implementations.layout.animation.NullAnimation;
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
    private Set<Node> allHeapNodes = new HashSet<>();
    private GraphImpl<Node, HeapEdge> interLevelGraph = new GraphImpl<>();

    private IAnimation animation = new NullAnimation();

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

    public void inLoop() {
        if (animation.executeStepAndCheckIfDone()) {
            buildEdges();
            animation = new NullAnimation();
        }

        boolean newNodes = receiveNodes();
        if (newNodes) {
            Set<Node> oldHeapNodes = allHeapNodes.stream().collect(Collectors.toSet());
            resetStack();
            animation.finalise();
            animation = new Animation(oldHeapNodes, allHeapNodes.stream().collect(Collectors.toSet()));
        }
    }

    public Collection<Node> getCurrentNodes() {
        return allHeapNodes;
    }

    private void addLevel(StackNode stackNode) {
        HeapGraphLevel levelGraph = new HeapGraphLevel(currentLevel);
        levels.add(levelGraph.id, levelGraph);

        if (!stackNode.hasReference()) {
            levelGraph.buildNode(stackNode, _renderEngine);
            levelGraph.runLayout();
            stackNode.updatePosition();
            currentLevel++;
        } else {
            buildGraph(stackNode, levelGraph);
            levelGraph.runLayout();
            levelGraph.updatePositions();
            currentLevel++;
        }
    }

    private void updateCurrentLevel(StackNode stackNode) {
        NodesComparator comparator = new NodesComparator();

        HeapGraphLevel levelGraph = levels.get(currentLevel);
        StackNode oldStackNode = levelGraph.getRoot();

        if (oldStackNode.equals(stackNode)) {
            if (comparator.compare(oldStackNode, stackNode)) {
                levelGraph.updatePositions();
                currentLevel++;
            } else {
                if (!stackNode.hasReference()) {
                    currentLevel++;
                } else {
                    //copy oldStackNode color to newStackNode
                    removeLevel(levelGraph);
                    addLevel(stackNode);
                }
            }
        } else {
            removeLevel(levelGraph);
            addLevel(stackNode);
        }
    }

    private void buildEdges() {
        for (HeapGraphLevel levelGraph : levels) {
            for (Node n : levelGraph.getVertices()) {
                Collection<HeapEdge> outEdges = levelGraph.getOutEdges(n);
                for (HeapEdge edge : outEdges) {
                    Node child = levelGraph.getOpposite(n, edge);
                    if (child.getGeometry() != null) {
                        edge.connect(n, child, new Colour(1, 1, 1), _renderEngine);
                    }
                }
                Collection<HeapEdge> edges = interLevelGraph.getOutEdges(n);
                if (edges != null) {
                    for (HeapEdge edge : edges) {
                        Node from = interLevelGraph.getSource(edge);
                        Node to = interLevelGraph.getDest(edge);
                        if (from.getGeometry() != null && to.getGeometry() != null) {
                            edge.connect(from, to, new Colour(1, 1, 1), _renderEngine);
                        }
                    }
                }
            }
        }
    }

    private void buildGraph(StackNode root, HeapGraphLevel level) {
        buildNodes(root, level);

        for (Node node : level.getVertices()) {
            for (Node child : node.getReferences()) {
                assert allHeapNodes.contains(child);
                if (child.getLevel() == node.getLevel()) {
                    level.addEdge(new HeapEdge(), node, child);
                }
            }
        }
    }

    private void buildNodes(Node n, HeapGraphLevel level) {
        if (!allHeapNodes.contains(n)) {
            allHeapNodes.add(n);
            level.buildNode(n, _renderEngine);
            List<Node> references = n.getReferences();
            for (Node child : references) {
                if (allHeapNodes.contains(child)) {

                    Iterator<Node> iterator = allHeapNodes.iterator();
                    while(iterator.hasNext())
                    {
                        Node node = iterator.next();
                        if(node.equals(child))
                        {
                            child = node;
                            break;
                        }
                    }

                    interLevelGraph.addEdge(new HeapEdge(), n, child);
                } else {
                    buildNodes(child, level);
                }
            }
        }
    }

    private void removeLevel(HeapGraphLevel levelGraph) {
        for (Node n : levelGraph.getVertices()) {
            removeNode(n, levelGraph);
        }
        this.levels.remove(levelGraph.id);
    }

    private void removeNode(Node n, HeapGraphLevel levelGraph) {
        removeEdges(levelGraph.getOutEdges(n));
        removeEdges(interLevelGraph.getOutEdges(n));
        Collection<HeapEdge> inEdges = interLevelGraph.getInEdges(n);

        if(inEdges == null || inEdges.size() == 0)
        {
            _renderEngine.removeFrom3DSpace(n.getGeometry());
            interLevelGraph.removeVertex(n);
            allHeapNodes.remove(n);
        } else
        {
            HeapEdge edge = inEdges.iterator().next();
            Node parent = interLevelGraph.getOpposite(n,edge);
            assert parent.getLevel() != n.getLevel();

            parent.getLevel().addVertex(n);
            n.getLevel().runLayout();
        }

    }

    private void removeEdges(Collection<HeapEdge> edges) {
        if (edges != null)
            for (HeapEdge edge : edges)
                _renderEngine.removeFrom3DSpace(edge.getLine());
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

    public void dispose() {
        _renderEngine.clear3DSpace();
    }
}
