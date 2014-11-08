package com.heap3d.ui.controllers;

/**
 * Created by oskar on 04/11/14.
 */
import com.heap3d.ui.viewmodels.ProcessTabViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ProcessTabController implements Initializable {

    @FXML
    private TextArea jvmArgs;

    @FXML
    private TextArea jdkPath;

    @FXML
    private Button pauseButton;

    @FXML
    private Button startButton;

    @FXML
    private TextArea debugeeOut;

    @FXML
    private TextArea classPath;

    @FXML
    private Button stopButton;

    @FXML
    private Button resumeButton;

    @FXML
    private TextArea debuggerOut;

    @FXML
    private Button stepButton;

    @FXML
    private TextField className;

    @FXML
    private TextField status;

    @FXML
    void start() {
        _viewModel.getStartActionCommand().execute();
    }

    @FXML
    void stop() {
        _viewModel.getStopActionCommand().execute();
    }

    @FXML
    void pause() {
        _viewModel.getPauseActionCommand().execute();
    }

    @FXML
    void resume() {
        _viewModel.getResumeActionCommand().execute();
    }

    @FXML
    void step() {
        _viewModel.getStepActionCommand().execute();
    }

    private final ProcessTabViewModel _viewModel;

    public ProcessTabController(ProcessTabViewModel viewModel) {
        _viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startButton.disableProperty().bind(_viewModel.getStartActionCommand().canExecute().not());
        stopButton.disableProperty().bind(_viewModel.getStopActionCommand().canExecute().not());
        pauseButton.disableProperty().bind(_viewModel.getPauseActionCommand().canExecute().not());
        resumeButton.disableProperty().bind(_viewModel.getResumeActionCommand().canExecute().not());
        stepButton.disableProperty().bind(_viewModel.getStepActionCommand().canExecute().not());

        classPath.textProperty().bindBidirectional(_viewModel.getClassPath());
        className.textProperty().bindBidirectional(_viewModel.getClassName());
        jdkPath.textProperty().bindBidirectional(_viewModel.getJavaPath());
        jvmArgs.textProperty().bindBidirectional(_viewModel.getJvmArgs());
        status.textProperty().bindBidirectional(_viewModel.getStatus());
        debuggerOut.textProperty().bindBidirectional(_viewModel.getDebuggerOutput());
    }
}


