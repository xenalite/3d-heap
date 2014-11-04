package com.heap3d.ui;

/**
 * Created by oskar on 04/11/14.
 */
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class BreakpointsTabController {

    @FXML
    private Button addWatchpointButton;

    @FXML
    private TextField breakpointMethodName;

    @FXML
    private TextField watchpointClassName;

    @FXML
    private TextField breakpointClassName;

    @FXML
    private ListView<?> breakpoints;

    @FXML
    private Button addBreakpointButton;

    @FXML
    private ListView<?> watchpoints;

    @FXML
    private TextField watchpointFieldName;

    @FXML
    void addBreakpoint(ActionEvent event) {

    }

    @FXML
    void addWatchpoint(ActionEvent event) {

    }

}

