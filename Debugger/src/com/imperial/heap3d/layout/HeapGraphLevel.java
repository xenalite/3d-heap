package com.imperial.heap3d.layout;

import com.graphics.shapes.Colour;
import com.heap3d.layout.FRLayout;
import com.heap3d.layout.GraphImpl;
import com.heap3d.layout.Layout;
import com.heap3d.layout.SpringLayout;
import com.imperial.heap3d.snapshot.Node;
import com.imperial.heap3d.snapshot.StackNode;

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
		this.layout = new SpringLayout<>(this);
	}

	@Override
	public boolean addVertex(Node vertex) {
		boolean add = super.addVertex(vertex);
		if (vertex instanceof StackNode) {
			root = (StackNode) vertex;
		}

		vertex.setLevel(this);

		return add;
	}

	public boolean buildNode(Node n, HeapGraph r) {
		if (addVertex(n)) {
			float x = getX(n);
			float y = getY(n);
			float z = getZ(n);

			if (!isRoot(n)) {
				n.buildGeometry(x, y, z, getScale(n));
			}else{
				n.buildGeometry(x, y, z, getScale(n), new Colour((float)Math.random(), (float)Math.random(), (float)Math.random()));
			}
			System.out.println("Building Node: "+n.toString());
			r.addShapeTo3DSpace(n.getGeometry());
			return true;
		} else
		{
			return false;
		}
	}

	protected float spacing = 10;
	public float getX(Node n) {
		float x = (float)layout.transform(n).getX() / 10;
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
		float y = (float) layout.transform(n).getY() / 10;
		if (!isRoot(n)) {
			if (y < 0) {
				y -= spacing;
			} else {
				y += spacing;
			}
		}
		return y;
	}

	public float getY(Node n) {
		return levelOffset * id;
//				isRoot(n) ? levelOffset * id
//				: ((float) Math.random() * levelOffset) + id * levelOffset;
	}

	public float getScale(Node n) {
		return isRoot(n) ? 10 : 1;
	}

	public Boolean isRoot(Node n) {
		//TODO improve 
		return n == root;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		HeapGraphLevel that = (HeapGraphLevel) o;

		if (id != that.id)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return id;
	}

}
