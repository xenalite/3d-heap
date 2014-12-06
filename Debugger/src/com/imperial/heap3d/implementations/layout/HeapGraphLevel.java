package com.imperial.heap3d.implementations.layout;

import com.heap3d.layout.*;
import com.imperial.heap3d.implementations.snapshot.Node;
import com.imperial.heap3d.implementations.snapshot.StackNode;

import java.awt.*;

public class HeapGraphLevel extends GraphImpl<Node, HeapEdge> {

	int id;
	Layout<Node, HeapEdge> layout;
	public static float levelOffset = 10f;

	public StackNode getRoot() {
		return root;
	}

	protected StackNode root;

	public HeapGraphLevel(int id) {
		this.id = id;
		this.layout = new FRLayout<>(this,0.9f, 0.01f);
		layout.setSize(new Dimension(200,200));
	}

	@Override
	public boolean addVertex(Node vertex) {
		if (vertex instanceof StackNode) {
			root = (StackNode) vertex;
		}

		boolean add = super.addVertex(vertex);
		if(add)
		{
			vertex.setLevel(this);
		}

		return add;
	}

	public boolean buildNode(Node n, IRenderEngine r) {
		if (addVertex(n)) {
			float x = getX(n);
			float y = getY();
			float z = getZ(n);

			n.buildGeometry(x, y, z, getScale(n));

			r.addTo3DSpace(n.getGeometry());
			return true;
		} else
		{
			return false;
		}
	}

	protected float spacing = 10;
	public float getX(Node n) {
		float x = (float)layout.transform(n).getX() ;
		if (!isRoot(n)) {
			if (x < 0) {
				x -= spacing;
			} else {
				x += spacing;
			}
		}
			return x;
	}

	public float getZ(Node n) {
		float y = (float) layout.transform(n).getY() ;
		if (!isRoot(n)) {
			if (y < 0) {
				y -= spacing;
			} else {
				y += spacing;
			}
		}
		return y;
	}

	public float getY() {
		return levelOffset * id;
	}

	public float getScale(Node n) {
		return isRoot(n) ? 10 : 1;
	}

	public Boolean isRoot(Node n) {
		return n == root;
	}

	@Override
	public boolean equals(Object o) {
		return this == o || !(o == null || getClass() != o.getClass())
				&& id == ((HeapGraphLevel) o).id;
	}

	@Override
	public int hashCode() {
		return id;
	}

}
