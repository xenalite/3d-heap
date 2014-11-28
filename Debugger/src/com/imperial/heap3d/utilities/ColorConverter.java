package com.imperial.heap3d.utilities;


import com.graphics.shapes.Colour;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ColorConverter {

    public static Paint convertToPaint(Colour colour)
    {
        return new Color(colour.getR(),colour.getG(),colour.getB(),1.0 );
    }

    public static Colour randomColour()
    {
        float r = (float)Math.random();
        float g = (float)Math.random();
        float b = (float)Math.random();
        return new Colour(r,g,b);
    }
}
