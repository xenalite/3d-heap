package com.heap3d.implementations.viewmodels;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.heap3d.implementations.events.NodeEvent;
import com.heap3d.implementations.events.NodeSelectionEvent;
import com.heap3d.implementations.events.ProcessEvent;
import com.heap3d.implementations.node.Node;
import com.heap3d.implementations.node.StackNode;
import com.heap3d.interfaces.viewmodels.IHeapInfoTabViewModel;
import com.heap3d.utilities.Pair;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HeapInfoTabViewModel implements IHeapInfoTabViewModel {

    private EventBus _eventBus;

    private Map<Node, TreeItem<Node>> nodeToTreeItem = new HashMap<>();

    @Override
    public Property<String> HeapInfoProperty() {
        return _HeapInfo;
    }

    private Property<String> _HeapInfo;

    private SimpleObjectProperty<TreeItem<Node>> _TreeView;

    @Override
    public SimpleObjectProperty<TreeItem<Node>> TreeViewProperty() {
        return _TreeView;
    }

    private SimpleObjectProperty<TreeItem<Node>> _selectedNode;

    @Override
    public SimpleObjectProperty<TreeItem<Node>> selectedNodeProperty() {
        return _selectedNode;
    }

    public HeapInfoTabViewModel(EventBus eventBus) {
        if (eventBus == null)
            throw new IllegalArgumentException("eventBus");
        _eventBus = eventBus;
        _eventBus.register(this);

        _HeapInfo = new SimpleStringProperty(this, "", "");
        _TreeView = new SimpleObjectProperty<>(this, "", new TreeItem<>());
        _selectedNode = new SimpleObjectProperty<>(this, "", new TreeItem<>());
    }

    @Override
    @Subscribe
    public void handleNodeSelectionEvent(NodeSelectionEvent nodeSelectionEvent) {
        _selectedNode.set(nodeToTreeItem.get(nodeSelectionEvent.getNode()));
    }

    @Override
    @Subscribe
    public void handleControlEvent(ProcessEvent processEvent) {
        switch (processEvent.type) {
            case SELECT:
                Platform.runLater(() -> _HeapInfo.setValue(processEvent.message));
                break;

        }
    }

    @Override
    @Subscribe
    public void handleNodeEvent(NodeEvent nodeEvent) {
        Platform.runLater(() -> {
            TreeItem<Node> root = new TreeItem<>(null);
            root.setExpanded(true);
            ObservableList<TreeItem<Node>> children = root.getChildren();
            for (StackNode node : nodeEvent.nodes) {
                children.add(0, createNode(node, node.getName()));
            }
            _TreeView.setValue(root);
        });
    }

    /**
     * Creates nodes on the fly for the treeView
     * Avoids stack overflow / infinite recursion by lazy loading
     * cycles can are represented like in a normal debugger
     *
     * @param n The given node.
     * @return  A TreeItem corresponding to the given StackNode.
     */
    private TreeItem<Node> createNode(final Node n, final String name) {
        TreeItem<Node> treeItem = new TreeItem<Node>(n) {
            private boolean isLeaf;
            private boolean isFirstTimeChildren = true;
            private boolean isFirstTimeLeaf = true;

            @Override
            public ObservableList<TreeItem<Node>> getChildren() {
                if (isFirstTimeChildren) {
                    isFirstTimeChildren = false;
                    super.getChildren().setAll(buildChildren(this));
                }
                return super.getChildren();
            }

            @Override
            public boolean isLeaf() {
                if (isFirstTimeLeaf) {
                    isFirstTimeLeaf = false;
                    Node node = getValue();
                    isLeaf = node.getReferences().isEmpty();
                }

                return isLeaf;
            }

            private ObservableList<TreeItem<Node>> buildChildren(TreeItem<Node> TreeItem) {
                Node node = TreeItem.getValue();
                if (node != null) {
                    List<Pair<Node, String>> references = node.getReferences();
                    if (references != null && !references.isEmpty()) {
                        ObservableList<TreeItem<Node>> children = FXCollections.observableArrayList();

                        children.addAll(references.stream().map(child -> createNode(child.first, child.second)).collect(Collectors.toList()));
                        children.stream().forEach(item -> nodeToTreeItem.put(item.getValue(), item));

                        return children;
                    }
                }

                return FXCollections.emptyObservableList();
            }
        };


        treeItem.getValue().setTreeName(name);
        nodeToTreeItem.put(n, treeItem);
        return treeItem;
    }
}