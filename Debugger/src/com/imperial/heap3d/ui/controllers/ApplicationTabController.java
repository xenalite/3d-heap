package com.imperial.heap3d.ui.controllers;

import com.imperial.heap3d.implementations.viewmodels.ApplicationTabViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by oskar on 13/11/14.
 */

public class ApplicationTabController implements Initializable {

    @FXML
    private TextArea argumentsTextArea;

    @FXML
    private TextArea debuggerConsole;

    @FXML
    private TextArea classPathTextArea;

    @FXML
    private TextArea programConsole;

    @FXML
    private TextArea javaPathTextArea;

    @FXML
    private TextArea classNameTextArea;

    private final ApplicationTabViewModel _viewModel;

    public ApplicationTabController(ApplicationTabViewModel viewModel) {
        _viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        classNameTextArea.textProperty().bindBidirectional(_viewModel.getClassNameProperty());
        classPathTextArea.textProperty().bindBidirectional(_viewModel.getClassPathProperty());
        javaPathTextArea.textProperty().bindBidirectional(_viewModel.getJavaPathProperty());
        argumentsTextArea.textProperty().bindBidirectional(_viewModel.getArgumentsProperty());
        debuggerConsole.textProperty().bindBidirectional(_viewModel.getDebuggerConsoleProperty());
        programConsole.textProperty().bindBidirectional(_viewModel.getProcessConsoleProperty());
    }
}

