package com.heap3d.interfaces.viewmodels;

import com.heap3d.implementations.events.NodeEvent;
import com.heap3d.implementations.events.NodeSelectionEvent;
import com.heap3d.implementations.events.ProcessEvent;
import com.heap3d.implementations.snapshot.Node;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeItem;

/**
 * Created by oskar on 06/01/15.
 */
public interface IHeapInfoTabViewModel {

    public Property<String> HeapInfoProperty();
    public SimpleObjectProperty<TreeItem<Node>> TreeViewProperty();
    public SimpleObjectProperty<TreeItem<Node>> selectedNodeProperty();

    public void handleNodeSelectionEvent(NodeSelectionEvent nodeSelectionEvent);
    public void handleControlEvent(ProcessEvent processEvent);
    public void handleNodeEvent(NodeEvent nodeEvent);
}
