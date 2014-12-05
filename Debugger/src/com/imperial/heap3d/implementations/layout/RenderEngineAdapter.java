package com.imperial.heap3d.implementations.layout;

import com.graphics.RenderEngine;
import com.graphics.shapes.Colour;
import com.graphics.shapes.Shape;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Created by oskar on 05/12/14.
 */
public class RenderEngineAdapter extends RenderEngine implements IRenderEngine {

    private List<Runnable> _before = Collections.synchronizedList(new ArrayList<>());
    private List<Runnable> _during = Collections.synchronizedList(new ArrayList<>());
    private List<Runnable> _after = Collections.synchronizedList(new ArrayList<>());

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
        for(Runnable r : _before)
            r.run();
    }

    @Override
    protected void inLoop() {
        for(Runnable r : _during)
            r.run();
    }

    @Override
    protected void afterLoop() {
        for(Runnable r : _after)
            r.run();
    }
    //endregion

    //region IRenderEngine-Command-Hooks
    @Override
    public void before(Collection<Runnable> commands) {
        _before.clear();
        _before.addAll(commands);
    }

    @Override
    public void during(Collection<Runnable> commands) {
        _during.clear();
        _during.addAll(commands);
    }

    @Override
    public void after(Collection<Runnable> commands) {
        _after.clear();
        _after.addAll(commands);
    }
    //endregion
}
