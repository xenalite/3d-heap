package com.imperial.heap3d.layout;

import com.graphics.shapes.Colour;
import com.heap3d.layout.FRLayout;
import com.heap3d.layout.GraphImpl;
import com.heap3d.layout.Layout;
import com.imperial.heap3d.snapshot.HeapNode;
import com.imperial.heap3d.snapshot.IDNode;
import com.imperial.heap3d.snapshot.Node;
import com.imperial.heap3d.snapshot.StackNode;

public class HeapGraphLevel extends GraphImpl<Node, HeapEdge> {

	int id;
	Layout<Node, HeapEdge> layout;
	private final Colour color;
	public final Colour linecolor;
	public static float levelOffset = 10f;
	protected StackNode root;

	public HeapGraphLevel(int id) {
		this.id = id;
		this.layout = new FRLayout<Node, HeapEdge>(this);
		float r = (float) Math.random();
		float g = (float) Math.random();
		float b = (float) Math.random();

		color = new Colour(r, g, b);
		float offset = r + g + b > 2 ? 0.7f : 1.3f;
		linecolor = new Colour(r * offset, g * offset, b * offset);
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

	public void buildNode(Node n, HeapGraph r) {
		if (addVertex(n)) {
			float x = getX(n);
			float y = getY(n);
			float z = getZ(n);
			float spacing = 5;
			if (!isRoot(n)) {
				if (x < 0) {
					x -= spacing;
				} else {
					x += spacing;
				}

				if (y < 0) {
					y -= spacing;
				} else {
					y += spacing;
				}
			}
			n.buildGeometry(x, y, z, getScale(n), color);
			r.addShapeTo3DSpace(n.getGeometry());
		}
	}

	public float getX(Node n) {
		return (float) layout.transform(n).getX() / 10;
	}

	public float getY(Node n) {
		return (float) layout.transform(n).getY() / 10;
	}

	public float getZ(Node n) {
		return isRoot(n) ? levelOffset * id
				: /*((float) Math.random() * levelOffset)*/ + id * levelOffset;
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