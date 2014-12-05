package com.imperial.heap3d.implementations.layout;

import com.graphics.RenderEngine;
import com.graphics.shapes.Colour;
import com.graphics.shapes.Shape;

import java.awt.*;

/**
 * Created by oskar on 05/12/14.
 */
public class RenderEngineAdapter extends RenderEngine implements IRenderEngine {

    private Runnable _before;
    private Runnable _during;
    private Runnable _after;

    public RenderEngineAdapter(Canvas canvas) {
        super(canvas);
    }

    //region RenderEngine
    @Override
    public void clear3DSpace() {
        clearShapesFrom3DSpace();
    }

    @Override
    public void addTo3DSpace(Shape shape) {
        addShapeTo3DSpace(shape);
    }

    @Override
    public void removeFrom3DSpace(Shape shape) {
        removeShapeFrom3DSpace(shape);
    }

    @Override
    public Shape createShapeFromModel(String path, int a, int b, int c, int d, Colour colour) {
        return modelToShape(path, a, b, c, d, colour);
    }

    @Override
    public Shape getSelectedShape() {
        return super.getSelectedShape();
    }

    @Override
    public void setBackgroundColour(float a, float b, float c, float d) {
        super.setBackgroundColour(a, b, c, d);
    }
    //endregion

    //region RenderEngine-Loop
    @Override
    protected void beforeLoop() {
        _before.run();
    }

    @Override
    protected void inLoop() {
        _during.run();
    }

    @Override
    protected void afterLoop() {
        _after.run();
    }
    //endregion

    //region IRenderEngine-Command-Hooks
    @Override
    public void before(Runnable command) {
        _before = command;
    }

    @Override
    public void during(Runnable command) {
        _during = command;
    }

    @Override
    public void after(Runnable command) {
        _after = command;
    }
    //endregion
}
