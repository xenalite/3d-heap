package com.imperial.heap3d.implementations.layout;

import com.graphics.shapes.Colour;
import com.graphics.shapes.Shape;

import java.util.List;

/**
 * Created by oskar on 05/12/14.
 */
public interface IRenderEngine {

    public void clear3DSpace();

    public void addTo3DSpace(Shape shape);

    public void removeFrom3DSpace(Shape shape);

    public Shape createShapeFromModel(String path, int a, int b, int c, int d, Colour colour);

    public Shape getSelectedShape();

    public void setBackgroundColour(float a, float b, float c, float d);

    public void before(List<Runnable> commands);

    public void during(List<Runnable> commands);

    public void after(List<Runnable> commands);
}
