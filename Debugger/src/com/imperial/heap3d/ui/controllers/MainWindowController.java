package com.imperial.heap3d.ui.controllers;

import com.imperial.heap3d.ui.viewmodels.MainWindowViewModel;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by oskar on 13/11/14.
 */

public class MainWindowController implements Initializable {

    @FXML
    private Group groupField;

    @FXML
    private TextArea jvmArgumentsTextArea;

    @FXML
    private Button pauseButton;

    @FXML
    private Button startButton;

    @FXML
    private TextArea variablesTextArea;

    @FXML
    private Button resumeButton;

    @FXML
    private TextField classPathTextField;

    @FXML
    private TextArea debugeeOutputTextArea;

    @FXML
    private Button stepButton;

    @FXML
    private TextArea javaPathTextArea;

    @FXML
    private TextArea debugeeInputTextArea;

    @FXML
    private Button stopButton;

    @FXML
    private TextField statusTextField;

    @FXML
    private TextField classNameTextField;

    @FXML
    void startPressed() {
        _viewModel.getStartActionCommand().execute();
    }

    @FXML
    void stopPressed() {
        _viewModel.getStopActionCommand().execute();
    }

    @FXML
    void pausePressed() {
        _viewModel.getPauseActionCommand().execute();
    }

    @FXML
    void resumePressed() {
        _viewModel.getResumeActionCommand().execute();
    }

    @FXML
    void stepPressed() {
        _viewModel.getStepActionCommand().execute();
    }

    private final MainWindowViewModel _viewModel;

    public MainWindowController(MainWindowViewModel viewModel) {
        _viewModel = viewModel;
    }

    /* -------------------- AWT STUFF ----------------- */
    public static Canvas GLOBAL_CANVAS_THIS_IS_HORRIBLE;

    private class AwtInitializerTask extends FutureTask<JPanel> {
        public AwtInitializerTask(Callable<JPanel> callable) {
            super(callable);
        }
    }

    private class CustomAwtCanvas extends Canvas {
        public CustomAwtCanvas(int width, int height) {
            setSize(width, height);
        }

        public void paint(Graphics g) {
            Graphics2D g2;
            g2 = (Graphics2D) g;
            g2.setColor(Color.GRAY);
            g2.fillRect(0, 0, (int) getSize().getWidth(), (int) getSize().getHeight());
            g2.setColor(Color.BLACK);
            g2.drawString("It is a custom canvas area", 25, 50);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startButton.disableProperty().bind(_viewModel.getStartActionCommand().canExecute().not());
        stopButton.disableProperty().bind(_viewModel.getStopActionCommand().canExecute().not());
        pauseButton.disableProperty().bind(_viewModel.getPauseActionCommand().canExecute().not());
        resumeButton.disableProperty().bind(_viewModel.getResumeActionCommand().canExecute().not());
        stepButton.disableProperty().bind(_viewModel.getStepActionCommand().canExecute().not());

        classNameTextField.textProperty().bindBidirectional(_viewModel.getClassNameProperty());
        classPathTextField.textProperty().bindBidirectional(_viewModel.getClassPathProperty());
        javaPathTextArea.textProperty().bindBidirectional(_viewModel.getJavaPathProperty());
        statusTextField.textProperty().bindBidirectional(_viewModel.getStatusProperty());
        jvmArgumentsTextArea.textProperty().bindBidirectional(_viewModel.getJvmArgumentsProperty());
        variablesTextArea.textProperty().bindBidirectional(_viewModel.getVariablesProperty());
        debugeeInputTextArea.textProperty().bindBidirectional(_viewModel.getDebugeeInputProperty());
        debugeeOutputTextArea.textProperty().bindBidirectional(_viewModel.getDebugeeOutputProperty());

        final AwtInitializerTask awtInitializerTask = new AwtInitializerTask(() -> {
            JPanel jPanel = new JPanel();
            GLOBAL_CANVAS_THIS_IS_HORRIBLE = new CustomAwtCanvas(906, 690);
            jPanel.add(GLOBAL_CANVAS_THIS_IS_HORRIBLE);

            return jPanel;
        });

        SwingUtilities.invokeLater(awtInitializerTask);

        SwingNode swingNode = new SwingNode();
        JComponent component = null;
        try {
            component = awtInitializerTask.get();
        } catch (InterruptedException | ExecutionException ignored) {}

        swingNode.setContent(component);
        groupField.getChildren().add(swingNode);
    }
}

