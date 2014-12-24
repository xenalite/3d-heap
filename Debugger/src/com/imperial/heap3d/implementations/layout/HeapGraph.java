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
        _renderEngine.removeText();
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

    public Set<Entry<Node, Shape>> getCurrentNodes() {
        return nodeToShape.entrySet();
    }

    private void addLevel(StackNode stackNode) {
        HeapGraphLevel levelGraph = new HeapGraphLevel(currentLevel);
        levels.add(levelGraph.getId(), levelGraph);

        if (!stackNode.hasReference()) {
            if (levelGraph.addVertex(stackNode)) {
                Shape s = stackNode.createShape();
                nodeToShape.put(stackNode, s);
                _renderEngine.addTo3DSpace(s);
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

    private void updatePosition(Node node) {
        if (nodeToShape.containsKey(node)) {
            HeapGraphLevel level = node.getLevel();
            Shape s = nodeToShape.get(node);
            s.setPosition(level.getX(node), level.getY(), level.getZ(node));
        }
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

    private void buildNodes(Node n, HeapGraphLevel level) {
        if (!nodeToShape.containsKey(n)) {
            if (level.addVertex(n)) {
                Shape s = n.createShape();
                nodeToShape.put(n, s);
                _renderEngine.addTo3DSpace(s);
                updatePosition(n);
            }

            List<Node> references = n.getReferences();
            for (Node child : references) {
                if (nodeToShape.containsKey(child)) {
                    interLevelGraph.addEdge(new HeapEdge(), n, child);
                } else {
                    buildNodes(child, level);
                }
            }
        }
    }

    private void removeLevel(HeapGraphLevel levelGraph) {
        for (Node n : levelGraph.getVertices())
            removeNode(n, levelGraph);
        levels.remove(levelGraph.getId());
    }

    private void removeNode(Node n, HeapGraphLevel levelGraph) {
        removeEdges(levelGraph.getOutEdges(n));
        removeEdges(interLevelGraph.getOutEdges(n));
        removeEdges(interLevelGraph.getInEdges(n));
        Collection<HeapEdge> inEdges = interLevelGraph.getInEdges(n);
        if(inEdges == null || inEdges.size() == 0) {
            _renderEngine.removeFrom3DSpace(nodeToShape.get(n));
            interLevelGraph.removeVertex(n);
            nodeToShape.remove(n);
        } else {
            HeapEdge edge = inEdges.iterator().next();
            Node parent = interLevelGraph.getOpposite(n,edge);
            assert parent.getLevel() != n.getLevel();

            parent.getLevel().addVertex(n);
            n.getLevel().runLayout();
        }

        Shape s = nodeToShape.get(n);
        _renderEngine.removeFrom3DSpace(s);
        nodeToShape.remove(n);

        interLevelGraph.removeVertex(n);
    }

    private void updatePositions(HeapGraphLevel level) {
        level.getPositionsToUpdate().forEach(this::updatePosition);
    }

    private void removeEdges(Collection<HeapEdge> edges) {
        if (edges != null)
            for (HeapEdge edge : edges){
                _renderEngine.removeFrom3DSpace(edge.getLine());
                _renderEngine.removeFrom3DSpace(edge.getArrow());
            }
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
