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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

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
        TreeItem<Node> nodeTreeItem = new TreeItem<>(new StackNode("Node name", 1));
        _TreeView = new SimpleObjectProperty<>(this,"", nodeTreeItem);
    }

    @Subscribe
    public void handleControlEvent(ProcessEvent pe) {
        try {
            switch (pe.type) {
                case SELECT:
                    Platform.runLater( () -> {
                        _HeapInfo.set(pe.message);
                        _TreeView.getValue().getChildren().add(new TreeItem<>(new ObjectNode(pe.message.substring(0,8),pe.message.length())));
                    })  ;

                    break;

            }
        }
        catch(IllegalStateException e) { System.out.println(e); }
    }


}