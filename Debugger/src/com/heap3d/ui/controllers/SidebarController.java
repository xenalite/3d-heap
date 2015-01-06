package com.heap3d.ui.controllers;

import com.heap3d.interfaces.viewmodels.ISidebarViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by om612 on 04/12/14.
 */
public class SidebarController implements Initializable {

    @FXML
    private TabPane tabPane;

    private final ISidebarViewModel _viewModel;

    public SidebarController(ISidebarViewModel viewModel) {
        _viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
