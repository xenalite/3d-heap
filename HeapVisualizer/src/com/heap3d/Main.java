package com.heap3d;


import com.heap3d.layout.CircularLayout;
import com.heap3d.layout.FRLayout;
import com.heap3d.layout.ISOLayout;
import com.heap3d.layout.IterativeSpringBasedLayout;

public class Main  {

    public static void main(String[] args)
    {
        HeapGraph h = new HeapGraph(new IterativeSpringBasedLayout());
    }
}
