package com.heap3d.layout;

import java.util.Map;
import com.heap3d.Node;


public interface LayoutService {
    public <T extends Spatial & Node> void layout(Map<String, T> nodes);
    public <T extends Spatial & Node> void layout(Map<String, T> nodes, String rootNodeId);
}
