package com.heap3d.layout;

import java.util.Map;
import com.heap3d.Node;


public interface LayoutService {
    public Map<String,LayoutNode> layout(Map<String,Node> nodes);
    public Map<String,LayoutNode> layout(Map<String,Node> nodes, String rootNodeId);
}
