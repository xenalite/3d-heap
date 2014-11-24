package com.imperial.heap3d.entry;

import com.imperial.heap3d.factories.ControllerFactory;
import com.imperial.heap3d.utilities.TypeRegistry;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by oskar on 22/11/14.
 */
public class SwingWrappedApplication {
    private static String WINDOW_TITLE = "3D HEAP VISUALISER";

    private static final int MIN_FRAME_HEIGHT = 700;
    private static final int MIN_SIDEBAR_HEIGHT = MIN_FRAME_HEIGHT;
    private static final int MIN_BOTTOM_PANEL_HEIGHT = 100;
    private static final int MIN_CANVAS_HEIGHT = MIN_FRAME_HEIGHT - MIN_BOTTOM_PANEL_HEIGHT;

    private static final int MIN_FRAME_WIDTH = 1250;
    private static final int MIN_SIDEBAR_WIDTH = 400;
    private static final int MIN_BOTTOM_PANEL_WIDTH = MIN_FRAME_WIDTH - MIN_SIDEBAR_WIDTH;
    private static final int MIN_CANVAS_WIDTH = MIN_FRAME_WIDTH - MIN_SIDEBAR_WIDTH;

    private final TypeRegistry _typeRegistry = new TypeRegistry();

    // TODO FACTORISE
    public static Canvas CANVAS;

    public static void main(String[] args) {
        new SwingWrappedApplication().run();
    }

    private void run() {
        SwingUtilities.invokeLater(this::initFrame);
    }

    private Scene initFXSideBar() {
        try {
            String dir = System.getProperty("user.dir");
            File f = new File(dir + "/src/com/imperial/heap3d/ui/views/Sidebar.fxml");
            FXMLLoader loader = new FXMLLoader(f.toURI().toURL());
            loader.setControllerFactory(new ControllerFactory(_typeRegistry.getInjector()));
            Parent root = loader.load();
            return new Scene(root, MIN_SIDEBAR_WIDTH, MIN_SIDEBAR_HEIGHT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("loader sidebar");
    }

    private Scene initFXBottomPanel() {
        try {
            String dir = System.getProperty("user.dir");
            File f = new File(dir + "/src/com/imperial/heap3d/ui/views/BottomPanel.fxml");
            FXMLLoader loader = new FXMLLoader(f.toURI().toURL());
            loader.setControllerFactory(new ControllerFactory(_typeRegistry.getInjector()));
            Parent root = loader.load();
            return new Scene(root, MIN_BOTTOM_PANEL_WIDTH, MIN_BOTTOM_PANEL_HEIGHT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("loader bottompanel");
    }

    private void initFrame() {
        JFrame frame = new JFrame();
        JSplitPane pane = new JSplitPane();
        JSplitPane leftPane = new JSplitPane();

        JFXPanel fxSidebar = new JFXPanel();
        Platform.runLater(() -> fxSidebar.setScene(initFXSideBar()));

        JFXPanel fxBottomPanel = new JFXPanel();
        Platform.runLater(() -> fxBottomPanel.setScene(initFXBottomPanel()));

        CANVAS = new Canvas();
        CANVAS.setSize(MIN_CANVAS_WIDTH, MIN_CANVAS_HEIGHT);
        CANVAS.setVisible(true);

        leftPane.setTopComponent(CANVAS);
        leftPane.setBottomComponent(fxBottomPanel);
        leftPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        pane.setLeftComponent(leftPane);
        pane.setRightComponent(fxSidebar);

        frame.add(pane);
        frame.setSize(MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT);
        frame.setTitle(WINDOW_TITLE);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}
