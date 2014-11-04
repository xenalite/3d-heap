package com.heap3d.ui.controllers;

/**
 * Created by oskar on 04/11/14.
 */
import com.heap3d.ui.viewmodels.BreakpointsTabViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class BreakpointsTabController implements Initializable {

    @FXML
    private Button addWatchpointButton;

    @FXML
    private TextField breakpointMethodName;

    @FXML
    private TextField watchpointClassName;

    @FXML
    private TextField breakpointClassName;

    @FXML
    private ListView<String> breakpoints;

    @FXML
    private Button addBreakpointButton;

    @FXML
    private ListView<String> watchpoints;

    @FXML
    private TextField watchpointFieldName;

    @FXML
    void addBreakpoint() {
        _viewModel.addBreakpointAction();
    }

    @FXML
    void addWatchpoint() {
        _viewModel.addWatchpointAction();
    }

    private final BreakpointsTabViewModel _viewModel;

    public BreakpointsTabController(BreakpointsTabViewModel viewModel) {
        _viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        breakpoints.itemsProperty().bindBidirectional(_viewModel.getBreakpointsProperty());
        watchpoints.itemsProperty().bindBidirectional(_viewModel.getWatchpointsProperty());

        breakpointClassName.textProperty().bindBidirectional(_viewModel.getBreakpointProperty());
        watchpointClassName.textProperty().bindBidirectional(_viewModel.getWatchpointProperty());
    }
}

