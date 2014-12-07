package com.imperial.heap3d.ui.controllers;

import com.graphics.shapes.Colour;
import com.imperial.heap3d.implementations.snapshot.Node;
import com.imperial.heap3d.implementations.viewmodels.HeapInfoTabViewModel;
import com.imperial.heap3d.utilities.ColorConverter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;


import java.net.URL;
import java.util.ResourceBundle;



public class HeapInfoTabController implements Initializable {

    @FXML
    private TreeView<Node> treeView;

    @FXML
    private TextArea infoText;


    private final HeapInfoTabViewModel _viewModel;

    public HeapInfoTabController(HeapInfoTabViewModel viewModel) {
        _viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        infoText.textProperty().bind(_viewModel.HeapInfoProperty());
        treeView.rootProperty().bindBidirectional(_viewModel.TreeViewProperty());
        //This sets the color of the treeNode
        treeView.setCellFactory(treeView -> {
            final Label label = new Label();
            final Label anotherLabel = new Label("\u25A0"); //this is a box in unicode

            //a box to store the labels in
            final HBox hbox = new HBox(5, anotherLabel, label);
            TreeCell<Node> cell =  new TreeCell<Node>() {

                @Override
                protected void updateItem(Node item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty ) {
                        setGraphic(null);
                    } else {

                        if(item != null) {
                            // TODO -- this is silly, but will need to send more information. perhaps entry(node,shape)
                            anotherLabel.setTextFill(ColorConverter.convertToPaint(Colour.RED));
//                                    item.getColour()));
                        }
                            setGraphic(hbox);

                    }
                }
            };
            cell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            cell.itemProperty().addListener((obs, oldItem, newItem) ->
                    label.setText(newItem != null ? newItem.getName()+": "+String.valueOf(newItem) : ""));
            return cell ;
        });
    }
}
