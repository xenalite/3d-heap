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
        if (lines != null)
            for (Line l : lines)
                renderEngine.removeFrom3DSpace(l);
        else
            lines = new ArrayList<Line>();

        float[] fromPos = from.getPosition();
        float[] toPos = to.getPosition();
        float dxOffset = 0;
        float dyOffset = 0;
        float dzOffset = 0;

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
            dxOffset = 0.15f;
        } else {
            Line line = new Line((Cube) from, (Cube) to, color);
            lines.add(line);
            dxOffset = (fromPos[0] - toPos[0]) / 100;
            dyOffset = (fromPos[1] - toPos[1]) / 100;
            dzOffset = (fromPos[2] - toPos[2]) / 100;
        }

        //TODO rotation
        float rotX = 0f;
        float rotY = 0f;
        float rotZ = 0f;
        Vector3f intersection = GeometryUtils.getIntersectionPoint(new Vector3f(fromPos[0], fromPos[1], fromPos[2]),
                new Vector3f(toPos[0], toPos[1], toPos[2]), 0.5F);
        arrow = new Pyramid(intersection.x, intersection.y, intersection.z, rotX, rotY, rotZ, 0.1f, color);

        for (Line l : lines)
            renderEngine.addTo3DSpace(l);
        renderEngine.addTo3DSpace(arrow);
    }

    public List<Line> getLines(){
        return lines;
    }
    
    public Pyramid getArrow(){
        return arrow;
    }
}
