package com.imperial.heap3d.ui.controllers;

import com.imperial.heap3d.ui.viewmodels.BreakpointsViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by oskar on 13/11/14.
 */

public class BreakpointsController implements Initializable {

    @FXML
    private TextField watchpointClassNameTextField;

    @FXML
    private TextField watchpointFieldNameTextField;

    @FXML
    private TextField breakpointClassNameTextField;

    @FXML
    private ListView<String> watchpointsListView;

    @FXML
    private ListView<String> breakpointsListView;

    @FXML
    private TextField breakpointMethodNameTextField;

    @FXML
    void addBreakpointPressed() {
        _viewModel.addBreakpointAction();
    }

    @FXML
    void addWatchpointPressed() {
        _viewModel.addWatchpointAction();
    }

    private final BreakpointsViewModel _viewModel;

    public BreakpointsController(BreakpointsViewModel viewModel) {
        _viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        watchpointClassNameTextField.textProperty().bindBidirectional(_viewModel.getWatchpointClassProperty());
        watchpointFieldNameTextField.textProperty().bindBidirectional(_viewModel.getWatchpointFieldProperty());
        breakpointClassNameTextField.textProperty().bindBidirectional(_viewModel.getBreakpointClassProperty());
        breakpointMethodNameTextField.textProperty().bindBidirectional(_viewModel.getBreakpointMethodProperty());

        watchpointsListView.itemsProperty().bindBidirectional(_viewModel.getWatchpointsProperty());
        breakpointsListView.itemsProperty().bindBidirectional(_viewModel.getBreakpointsProperty());
    }
}
