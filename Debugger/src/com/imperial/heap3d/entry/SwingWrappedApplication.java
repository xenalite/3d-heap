package com.imperial.heap3d.entry;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.factories.ControllerFactory;
import com.imperial.heap3d.factories.HeapGraphFactory;
import com.imperial.heap3d.factories.VirtualMachineProvider;
import com.imperial.heap3d.layout.HeapGraph;
import com.imperial.heap3d.ui.controllers.ApplicationTabController;
import com.imperial.heap3d.ui.controllers.BottomPanelController;
import com.imperial.heap3d.ui.controllers.BreakpointsTabController;
import com.imperial.heap3d.ui.viewmodels.ApplicationTabViewModel;
import com.imperial.heap3d.ui.viewmodels.BreakpointsTabViewModel;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.picocontainer.Characteristics;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.behaviors.OptInCaching;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private final MutablePicoContainer _injector = new DefaultPicoContainer(new OptInCaching());

    public static void main(String[] args) {
        new SwingWrappedApplication().run();
    }

    private void run() {
        _injector.as(Characteristics.CACHE).addComponent(VirtualMachineProvider.class);
        _injector.as(Characteristics.CACHE).addComponent(EventBus.class);

        _injector.addComponent(ApplicationTabController.class);
        _injector.addComponent(BreakpointsTabController.class);
        _injector.addComponent(BottomPanelController.class);

        _injector.as(Characteristics.CACHE).addComponent(ApplicationTabViewModel.class);
        _injector.addComponent(BreakpointsTabViewModel.class);

        SwingUtilities.invokeLater(this::initFrame);
    }

    private Scene initFXSideBar() {
        try {
            String dir = System.getProperty("user.dir");
            File f = new File(dir + "/src/com/imperial/heap3d/ui/views/Sidebar.fxml");
            FXMLLoader loader = new FXMLLoader(f.toURI().toURL());
            loader.setControllerFactory(new ControllerFactory(_injector));
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
            loader.setControllerFactory(new ControllerFactory(_injector));
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

        Canvas canvas = new Canvas();
        canvas.setSize(MIN_CANVAS_WIDTH, MIN_CANVAS_HEIGHT);
        canvas.setVisible(true);

        leftPane.setTopComponent(canvas);
        leftPane.setBottomComponent(fxBottomPanel);
        leftPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        pane.setLeftComponent(leftPane);
        pane.setRightComponent(fxSidebar);

        frame.add(pane);
        frame.setSize(MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT);
        frame.setTitle(WINDOW_TITLE);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        HeapGraphFactory factory = new HeapGraphFactory(canvas);
        _injector.as(Characteristics.CACHE).addComponent(factory);

        HeapGraph hgraph = factory.create();
        ExecutorService service = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "lwjgl");
            t.setDaemon(true);
            return t;
        });
        service.submit(hgraph);
        service.shutdown();
    }
}
