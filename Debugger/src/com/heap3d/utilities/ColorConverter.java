package com.heap3d.utilities;


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
        Color c = Color.hsb(Math.random()*360,1, 1);
        return new Colour((float)c.getRed(), (float)c.getGreen(), (float)c.getBlue());
    }

    public static Colour randomColour(long id)
    {
        Color c = Color.hsb(120, 1, 0.1 + (4-id%4)/5.0);
        return new Colour((float)c.getRed(), (float)c.getGreen(), (float)c.getBlue());
    }
}
