package com.imperial.heap3d.implementations.layout;

import com.heap3d.layout.FRLayout;
import com.heap3d.layout.Graph;
import com.heap3d.layout.GraphImpl;
import com.heap3d.layout.Layout;
import com.imperial.heap3d.implementations.snapshot.Node;
import com.imperial.heap3d.implementations.snapshot.StackNode;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class HeapGraphLevel {

	private final static float SPACING = 10f;
	private int _id;
	private Graph<Node, HeapEdge> _graph;
	private Layout<Node, HeapEdge> _layout;
	public static float levelOffset = 5f;
	private StackNode root;
	private boolean updated = false;

	public HeapGraphLevel(int id) {
		_id = id;
		_graph = new GraphImpl<>();
		_layout = new FRLayout<>(_graph, 0.9f, 0.01f);
		_layout.setSize(new Dimension(200, 200));
	}

	public StackNode getRoot() {
		return root;
	}

	public boolean addVertex(Node vertex) {
		if(_graph.addVertex(vertex)) {
			vertex.setLevel(this);
			if (vertex instanceof StackNode)
				root = (StackNode) vertex;

			return true;
		}
		return false;
	}

	public void runLayout()
	{
		if(getRoot() != null)
		{
			_layout.layout(getRoot());
			updated = true;
		}
	}

	public Iterable<Node> getPositionsToUpdate()
	{
		if(updated)
		{
			updated = false;
			return getVertices();
		}
		return new ArrayList<>();
	}

	public float getX(Node n) {
		float x = (float) _layout.transform(n).getX();

		if (!isRoot(n))
			x += (x < 0) ? -SPACING : SPACING;
		return x;
	}

	public float getZ(Node n) {
		float y = (float) _layout.transform(n).getY();

		if (!isRoot(n))
			y += (y < 0) ? -SPACING : SPACING;
		return y;
	}

	public float getY() {
		return levelOffset * _id;
	}

	public boolean isRoot(Node n) {
		return n == root;
	}

	@Override
	public boolean equals(Object o) {
		return this == o || !(o == null || getClass() != o.getClass())
				&& _id == ((HeapGraphLevel) o)._id;
	}

	@Override
	public int hashCode() {
		return _id;
	}

	public int getId() {
		return _id;
	}

	public Layout<Node,HeapEdge> getLayout() {
		return _layout;
	}

	public Iterable<Node> getVertices() {
		return _graph.getVertices();
	}

	public void addEdge(HeapEdge heapEdge, Node node, Node child) {
		_graph.addEdge(heapEdge, node, child);
	}

	public Collection<HeapEdge> getOutEdges(Node n) {
		return _graph.getOutEdges(n);
	}
}
