package org.debugger.ui.controllers;

import org.debugger.ui.viewmodels.ProcessTabViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by oskar on 29/10/14.
 */
public class ProcessTabController implements Initializable {

    @FXML
    private Button stopButton;
    @FXML
    private Button startButton;
    @FXML
    private TextField status;
    @FXML
    private TextField port;
    @FXML
    private TextField className;
    @FXML
    private TextField classPath;
    @FXML
    private TextField jdkPath;
    @FXML
    private TextArea sourceCode;

    private final ProcessTabViewModel _viewModel;;

    public ProcessTabController(ProcessTabViewModel viewModel) {
        _viewModel = viewModel;
    }

    public void start() {
        _viewModel.startAction();
    }

    public void stop() {
        _viewModel.stopAction();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sourceCode.textProperty().bindBidirectional(_viewModel.getSourceCodeProperty());

        jdkPath.textProperty().bindBidirectional(_viewModel.getJdkPathProperty());
        className.textProperty().bindBidirectional(_viewModel.getClassNameProperty());
        classPath.textProperty().bindBidirectional(_viewModel.getClassPathProperty());
        status.textProperty().bindBidirectional(_viewModel.getStatusProperty());
        port.textProperty().bindBidirectional(_viewModel.getPortProperty(), new NumberStringConverter());
        startButton.disableProperty().bindBidirectional(_viewModel.getDisableStart());
        stopButton.disableProperty().bind(_viewModel.getDisableStart().not());
    }
}