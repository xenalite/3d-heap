package com.heap3d.layout;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.*;

import java.awt.*;
import java.awt.geom.Point2D;


public class ISOLayout<V,E> extends JungLayout<V,E>{

    ISOMLayout sl;
    private V root;

    public ISOLayout(edu.uci.ics.jung.graph.Graph<V,E> graph)
    {
        sl = new ISOMLayout<V,E>(graph);
        this.layout = sl;
        setSize(new Dimension(1000,1000));
    }

    @Override
    protected void run() {
        int i=0;
        while (i<100 && !sl.done())
        {
            sl.step();
            i++;
        }
    }

    @Override
    public void layout(V root) {
        super.layout(root);
        this.root = root;
    }

    @Override
    protected Point2D convertToCoord(Point2D location) {
        if(root == null)
        {
            return location;
        } else
        {
            //Convert to a position relative to the root
            double rootx = layout.transform(root).getX();
            double rooty = layout.transform(root).getY();
            return new Point2D.Double(location.getX()+rootx, location.getY()+rooty);
        }
    }

    @Override
    protected Point2D convertFromCoord(Point2D location) {
        if(root == null)
        {
            return location;
        } else
        {
            //Convert from a position relative to the root
            double rootx = layout.transform(root).getX();
            double rooty = layout.transform(root).getY();
            return new Point2D.Double(location.getX()-rootx, location.getY()-rooty);
        }
    }


}
