package com.imperial.heap3d.implementations.layout;

import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Line;
import com.graphics.shapes.Pyramid;
import com.graphics.shapes.Shape;

public class HeapEdge {

    private Line line;
    private Pyramid arrow;

    public void connect(Shape from, Shape to, Colour color, IRenderEngine renderEngine) {
        if(line != null)
            renderEngine.removeFrom3DSpace(line);
        line = new Line((Cube) from, (Cube) to, color);
        float[] fromPos = from.getPosition();
        float[] toPos = to.getPosition();
        
        float dxOffset = (fromPos[0] - toPos[0])/100;
        float dyOffset = (fromPos[1] - toPos[1])/100;
        float dzOffset = (fromPos[2] - toPos[2])/100;
        
        //TODO rotation
        float rotX = 0f;
        float rotY = 0f;
        float rotZ = 0f;
        arrow = new Pyramid(toPos[0]+dxOffset*2, toPos[1]+dyOffset*2, toPos[2]+dzOffset*2, rotX, rotY, rotZ, 0.1f, color);
        renderEngine.addTo3DSpace(line);
        renderEngine.addTo3DSpace(arrow);
    }

    public Line getLine(){
        return line;
    }
    
    public Pyramid getArrow(){
        return arrow;
    }
}
