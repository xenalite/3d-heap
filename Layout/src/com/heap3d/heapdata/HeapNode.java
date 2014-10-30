package com.heap3d.heapdata;

public interface HeapNode extends com.heap3d.Node{
   public  <T extends HeapData> T getHeapData();
}
