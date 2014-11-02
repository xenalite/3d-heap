package com.heap3d.ui.controllers;

import com.heap3d.ui.viewmodels.ActionTabViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by oskar on 29/10/14.
 */
public class ActionsTabController implements Initializable {

    @FXML
    private Button add;
    @FXML
    private ListView<String> breakpoints;
    @FXML
    private TextField breakpoint;
    @FXML
    private Button resume;
    @FXML
    private Button step;
    @FXML
    private Button pause;

    private final ActionTabViewModel _viewModel;

    public ActionsTabController(ActionTabViewModel viewModel) {
        _viewModel = viewModel;
    }

    public void pause() {
        _viewModel.pauseAction();
    }

    public void resume() {
        _viewModel.resumeAction();
    }

    public void step() {
        _viewModel.stepAction();
    }

    public void add() {
        _viewModel.addAction();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        breakpoint.textProperty().bindBidirectional(_viewModel.getBreakpointProperty());
        breakpoints.itemsProperty().bindBidirectional(_viewModel.getBreakpointsProperty());
    }
}