package com.imperial.heap3d.implementations.layout;

import java.util.ArrayList;
import java.util.List;

import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Line;
import com.graphics.shapes.Pyramid;
import com.graphics.shapes.Shape;
import com.imperial.heap3d.utilities.GeometryUtils;
import org.lwjgl.util.vector.Vector3f;

public class HeapEdge {

    private List<Line> lines;
    private Pyramid arrow;

    public void connect(Shape from, Shape to, Colour color, IRenderEngine renderEngine) {
        if (lines != null) {
            lines.forEach(renderEngine::removeFrom3DSpace);
            renderEngine.removeFrom3DSpace(arrow);
        } else
            lines = new ArrayList<>();

        float[] fromPos = from.getPosition();
        float[] toPos = to.getPosition();
        Vector3f fromVector = new Vector3f(fromPos[0], fromPos[1], fromPos[2]);
        Vector3f toVector = new Vector3f(toPos[0], toPos[1], toPos[2]);

        if (from == to) {
            float offset = 2f;
            Line line1 = new Line(fromPos[0], fromPos[1], fromPos[2], fromPos[0], fromPos[1], fromPos[2] + offset, color);
            Line line2 = new Line(fromPos[0], fromPos[1], fromPos[2] + offset, fromPos[0] + offset, fromPos[1], fromPos[2] + offset, color);
            Line line3 = new Line(fromPos[0] + offset, fromPos[1], fromPos[2] + offset, fromPos[0] + offset, fromPos[1], fromPos[2], color);
            Line line4 = new Line(fromPos[0] + offset, fromPos[1], fromPos[2], fromPos[0], fromPos[1], fromPos[2], color);
            lines.add(line1);
            lines.add(line2);
            lines.add(line3);
            lines.add(line4);
        } else {
            Line line = new Line((Cube) from, (Cube) to, color);
            lines.add(line);
        }
        Vector3f intersection = GeometryUtils.getIntersectionPoint(fromVector, toVector, GeometryUtils.HEAP_NODE_SCALE);
        Vector3f rotation = GeometryUtils.getPyramidOrientation(fromVector, toVector);

        arrow = new Pyramid(intersection.x, intersection.y, intersection.z, 0, 0, 0, 0.1f, color);
        arrow.setRotation(rotation.x, rotation.y, rotation.z);

        lines.forEach(renderEngine::addTo3DSpace);
        renderEngine.addTo3DSpace(arrow);
    }

    public void removeShapes(IRenderEngine renderEngine) {
        if(lines != null) {
            lines.forEach(renderEngine::removeFrom3DSpace);
            renderEngine.removeFrom3DSpace(arrow);
        }
    }

    public List<Line> getLines() {
        return lines;
    }

    public Pyramid getArrow() {
        return arrow;
    }
}