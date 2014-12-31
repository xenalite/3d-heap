package com.perf;

import com.graphics.RenderEngine;
import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Line;
import com.graphics.shapes.Pyramid;
import com.graphics.text.Text3D;

/**
 * Created by costica1234 on 26/11/14.
 */
public class SimpleTest extends RenderEngine {

	Text3D text;
	
    public static void main(String[] args) {
        Thread t = new Thread(new SimpleTest(), "LwjglThread");
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public SimpleTest() {
        super("Simple Test", 1280, 720, false);
        super.setBackgroundColour(0.5f, 0.5f, 0.5f, 1f);
    }
    
    @Override
    protected void beforeLoop() {
        Colour col1 = new Colour(1, 0, 0);
        setRayPickDebugLines(true);
        
        float x = 10;
        float y = 10;
        float z = -10;
        
        float rotx = 0f;//(float) Math.toDegrees((float) Math.atan2( y, z ));
        float roty = (float) Math.toDegrees(Math.atan2( x * Math.cos(rotx), z ));
        float rotz = (float) Math.toDegrees(Math.atan2( Math.cos(rotx), Math.sin(rotx) * Math.sin(roty) ));
        
        rotx = (float) Math.atan2( y, z );
        if (z >= 0) {
           roty = (float) -Math.atan2( x * Math.cos(rotx), z );
        }else{
           roty = (float) Math.atan2( x * Math.cos(rotx), -z );
        }
        
        System.out.println(rotx);
        System.out.println(roty);
        System.out.println(rotz);
        /*
        if (z >= 0) {
            roty = -(float) Math.toDegrees(Math.atan2( x * Math.cos(rotx), z ));
         }else{
            roty = (float) Math.toDegrees(Math.atan2( x * Math.cos(rotx), -z ));
         }
       */
        
        Pyramid c1 = new Pyramid(x, y, z, rotx, roty, rotz, 1f, col1);
        Line l1 = new Line(0, 0, 0, x, y, z, Colour.GREEN);
        Cube c2 = new Cube(0, 1, 110, 0, 0, 0, 1, Colour.AQUA);
        addShapeTo3DSpace(c1);
        addShapeTo3DSpace(c2);
        addShapeTo3DSpace(l1);
        
        text = getText3D(Colour.GREEN);
        try {
        	text.print(0, 0, 0, 0, 0, 0, 1, "{...}");
        }catch (Exception e) {
			e.printStackTrace();
		}
		
    }

    int count = 0;
    
    @Override
    protected void inLoop() {
    	
    	if (count == 0 || count == 2000){
    		super.setCameraPositionSmooth(0, 0, 0);
    		
    	}
    	
    	if (count == 1000 || count == 3000){
    		super.setCameraPositionSmooth(0, 0, 200);
    		
    	}
    		
    		System.out.println(count);
    		count ++;
    }

    @Override
    protected void afterLoop() {

    }
}
