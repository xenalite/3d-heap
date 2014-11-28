package com.imperial.heap3d.ui.controllers;

import com.imperial.heap3d.snapshot.Node;
import com.imperial.heap3d.ui.viewmodels.BreakpointsTabViewModel;
import com.imperial.heap3d.ui.viewmodels.HeapInfoTabViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;

import java.net.URL;
import java.util.ResourceBundle;



public class HeapInfoTabController implements Initializable {

    @FXML
    private TreeView<Node> treeView;

    @FXML
    private TextArea infoText;


    private final HeapInfoTabViewModel _viewModel;

    public HeapInfoTabController(HeapInfoTabViewModel viewModel) {
        _viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        infoText.textProperty().bind(_viewModel.HeapInfoProperty());

    }
}
