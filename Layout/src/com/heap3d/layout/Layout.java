package com.heap3d.layout;


import java.awt.geom.Point2D;

public interface Layout<V,E> extends edu.uci.ics.jung.algorithms.layout.Layout<V,E> {

    public void layout();
    public void setRootVertex(V rootVertex);
    public void setRawPosition(V vertex, Point2D transform);
    public Point2D getRawPosition(V vertex);
}
