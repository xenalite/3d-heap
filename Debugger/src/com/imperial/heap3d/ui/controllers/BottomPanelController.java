package com.imperial.heap3d.ui.controllers;

import com.imperial.heap3d.ui.viewmodels.ApplicationTabViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by oskar on 23/11/14.
 */

public class BottomPanelController implements Initializable {

    @FXML
    private Button pauseButton;

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button resumeButton;

    @FXML
    private Button stepOverButton;

    @FXML
    private Button stepOutButton;

    @FXML
    private Button stepIntoButton;

    @FXML
    void startButtonPressed() {
        _viewModel.getStartActionCommand().execute();
    }

    @FXML
    void stopButtonPressed() {
        _viewModel.getStopActionCommand().execute();
    }

    @FXML
    void pauseButtonPressed() {
        _viewModel.getPauseActionCommand().execute();
    }

    @FXML
    void resumeButtonPressed() {
        _viewModel.getResumeActionCommand().execute();
    }

    @FXML
    void stepOverButtonPressed() {
        _viewModel.getStepOverActionCommand().execute();
    }

    @FXML
    void stepIntoButtonPressed() {
        _viewModel.getStepIntoActionCommand().execute();
    }

    @FXML
    void stepOutButtonPressed() {
        _viewModel.getStepOutActionCommand().execute();
    }

    private final ApplicationTabViewModel _viewModel;

    public BottomPanelController(ApplicationTabViewModel viewModel) {
        _viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startButton.disableProperty().bind(_viewModel.getStartActionCommand().canExecute().not());
        stopButton.disableProperty().bind(_viewModel.getStopActionCommand().canExecute().not());
        pauseButton.disableProperty().bind(_viewModel.getPauseActionCommand().canExecute().not());
        resumeButton.disableProperty().bind(_viewModel.getResumeActionCommand().canExecute().not());
        stepOverButton.disableProperty().bind(_viewModel.getStepOverActionCommand().canExecute().not());
        stepIntoButton.disableProperty().bind(_viewModel.getStepIntoActionCommand().canExecute().not());
        stepOutButton.disableProperty().bind(_viewModel.getStepOutActionCommand().canExecute().not());
    }
}

