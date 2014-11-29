package com.imperial.heap3d.ui.viewmodels;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.imperial.heap3d.events.*;
import com.imperial.heap3d.snapshot.Node;
import com.imperial.heap3d.snapshot.ObjectNode;
import com.imperial.heap3d.snapshot.StackNode;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.util.*;

import static com.imperial.heap3d.events.EventType.BREAKPOINT;
import static com.imperial.heap3d.events.EventType.WATCHPOINT;

public class HeapInfoTabViewModel {

    private EventBus _eventBus;


    public StringProperty HeapInfoProperty() {
        return _HeapInfo;
    }

    private StringProperty _HeapInfo;

    private SimpleObjectProperty<TreeItem<Node>> _TreeView;
    public  SimpleObjectProperty<TreeItem<Node>> TreeViewProperty() {
        return _TreeView;
    }


    public HeapInfoTabViewModel(EventBus eventBus) {
        if(eventBus == null)
            throw new IllegalArgumentException("eventBus");
        _eventBus = eventBus;
        _eventBus.register(this);

        _HeapInfo = new SimpleStringProperty(this, "", "Data from the ViewModel");
        _TreeView = new SimpleObjectProperty<>(this,"", new TreeItem<>());
    }


    @Subscribe
    public void handleControlEvent(ProcessEvent pe) {
        try {
            switch (pe.type) {
                case SELECT:
                    Platform.runLater( () -> {
                        _HeapInfo.set(pe.message);
                    })  ;

                    break;

            }
        }
        catch(IllegalStateException e) { System.out.println(e); }
    }


    @Subscribe
    public void handleNodeEvent(NodeEvent ne) {
        Platform.runLater( () -> {
            TreeItem<Node> root = new TreeItem<>(null);
            root.setExpanded(true);
            ObservableList<TreeItem<Node>> children = root.getChildren();
            for(StackNode node : ne.nodes)
            {
                children.add(0,buildTree(node));
            }
            _TreeView.setValue(root);
        })  ;
    }

    private TreeItem<Node> buildTree(Node n)
    {
        TreeItem<Node> root = new TreeItem<>(n);
        root.setExpanded(true);
        java.util.List<Node> references = n.getReferences();
        ObservableList<TreeItem<Node>> children = root.getChildren();
        for(Node child : references)
        {
            children.add(buildTree(child));
        }

        return root;
    }


}