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

    public static Canvas CANVAS;
    private static String WINDOW_TITLE = "3D HEAP VISUALISER";
    private static int MIN_WINDOW_HEIGHT = 690;
    private static int MIN_WINDOW_WIDTH = 906;
    private final TypeRegistry _typeRegistry = new TypeRegistry();

    public static void main(String[] args) {
        new SwingWrappedApplication().run();
    }

    private void run() {
        SwingUtilities.invokeLater(this::initGui);
    }

    private Scene initFx() {
        try {
            String dir = System.getProperty("user.dir");
            File f = new File(dir + "/src/com/imperial/heap3d/ui/views/MainWindow.fxml");
            FXMLLoader loader = new FXMLLoader(f.toURI().toURL());
            loader.setControllerFactory(new ControllerFactory(_typeRegistry.getInjector()));
            Parent root = loader.load();
            return new Scene(root, MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("loader");
    }

    private void initGui() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JFXPanel fxPanel = new JFXPanel();
        Platform.runLater(() -> fxPanel.setScene(initFx()));
        CANVAS = new Canvas();
        CANVAS.setSize(100, 100);
        CANVAS.setVisible(true);
        frame.setSize(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT);
        frame.setTitle(WINDOW_TITLE);
        panel.add(CANVAS);
        panel.add(fxPanel);
        frame.add(panel);
        frame.setVisible(true);
    }
}
