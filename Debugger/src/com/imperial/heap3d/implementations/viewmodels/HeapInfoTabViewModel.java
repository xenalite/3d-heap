package com.imperial.heap3d.implementations.viewmodels;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.imperial.heap3d.implementations.events.*;
import com.imperial.heap3d.implementations.snapshot.Node;
import com.imperial.heap3d.implementations.snapshot.StackNode;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.util.*;

public class HeapInfoTabViewModel {

    private EventBus _eventBus;


    public StringProperty HeapInfoProperty() {
        return _HeapInfo;
    }

    private StringProperty _HeapInfo;

    private SimpleObjectProperty<TreeItem<Node>> _TreeView;

    public SimpleObjectProperty<TreeItem<Node>> TreeViewProperty() {
        return _TreeView;
    }

    private SimpleObjectProperty<TreeItem<Node>> _selectedNode;

    public SimpleObjectProperty<TreeItem<Node>> selectedNodeProperty() {
        return _selectedNode;
    }

    public HeapInfoTabViewModel(EventBus eventBus) {
        if (eventBus == null)
            throw new IllegalArgumentException("eventBus");
        _eventBus = eventBus;
        _eventBus.register(this);

        _HeapInfo = new SimpleStringProperty(this, "", "Data from the ViewModel");
        _TreeView = new SimpleObjectProperty<>(this, "", new TreeItem<>());
        _selectedNode = new SimpleObjectProperty<>(new TreeItem<>());
    }

    @Subscribe
    public void handleNodeSelectionEvent(NodeSelectionEvent event) {
        _selectedNode = new SimpleObjectProperty<>(new TreeItem<>(event.getNode()));
    }

    @Subscribe
    public void handleControlEvent(ProcessEvent pe) {
        try {
            switch (pe.type) {
                case SELECT:
                    Platform.runLater(() -> {
                        _HeapInfo.set(pe.message);
                    });

                    break;

            }
        } catch (IllegalStateException e) {
            System.out.println(e);
        }
    }


    @Subscribe
    public void handleNodeEvent(NodeEvent ne) {
        Platform.runLater(() -> {
            TreeItem<Node> root = new TreeItem<>(null);
            root.setExpanded(true);
            ObservableList<TreeItem<Node>> children = root.getChildren();
            for (StackNode node : ne.nodes) {
                children.add(0, createNode(node));
            }
            _TreeView.setValue(root);
        });
    }

    /**
     * Creates nodes on the fly for the treeView
     * Avoids stack overflow / infinite recursion by lazy loading
     * cycles can are represented like in a normal debugger
     *
     * @param n
     * @return
     */
    private TreeItem<Node> createNode(final Node n) {
        return new TreeItem<Node>(n) {
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
                    List<Node> references = node.getReferences();
                    if (references != null && !references.isEmpty()) {
                        ObservableList<TreeItem<Node>> children = FXCollections.observableArrayList();

                        for (Node child : references) {
                            children.add(createNode(child));
                        }

                        return children;
                    }
                }

                return FXCollections.emptyObservableList();
            }
        };
    }


}