package com.imperial.heap3d.entry;

import com.google.common.eventbus.EventBus;
import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Line;
import com.graphics.shapes.Pyramid;
import com.imperial.heap3d.implementations.factories.ControllerFactory;
import com.imperial.heap3d.implementations.factories.ProcessFactory;
import com.imperial.heap3d.implementations.factories.ThreadBuilder;
import com.imperial.heap3d.implementations.factories.VirtualMachineProvider;
import com.imperial.heap3d.implementations.layout.HeapGraph;
import com.imperial.heap3d.implementations.layout.RenderEngineAdapter;
import com.imperial.heap3d.implementations.viewmodels.ApplicationTabViewModel;
import com.imperial.heap3d.implementations.viewmodels.BreakpointsTabViewModel;
import com.imperial.heap3d.implementations.viewmodels.HeapInfoTabViewModel;
import com.imperial.heap3d.implementations.viewmodels.SidebarViewModel;
import com.imperial.heap3d.ui.controllers.*;
import com.imperial.heap3d.utilities.GeometryUtils;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector3f;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.behaviors.OptInCaching;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.picocontainer.Characteristics.CACHE;

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
        _injector.as(CACHE).addComponent(VirtualMachineProvider.class);
        _injector.as(CACHE).addComponent(EventBus.class);
        _injector.addComponent(ProcessFactory.class);

        _injector.addComponent(ApplicationTabController.class);
        _injector.addComponent(BreakpointsTabController.class);
        _injector.addComponent(BottomPanelController.class);
        _injector.addComponent(HeapInfoTabController.class);
        _injector.addComponent(SidebarController.class);

        _injector.as(CACHE).addComponent(ApplicationTabViewModel.class);
        _injector.addComponent(BreakpointsTabViewModel.class);
        _injector.addComponent(HeapInfoTabViewModel.class);
        _injector.addComponent(SidebarViewModel.class);

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
        Canvas canvas = new Canvas();
        canvas.setSize(MIN_CANVAS_WIDTH, MIN_CANVAS_HEIGHT);
        canvas.setVisible(true);

        RenderEngineAdapter renderEngine = new RenderEngineAdapter(canvas);
        HeapGraph heapGraph = new HeapGraph(renderEngine);

//        final Shape[] logo = new Shape[1];
        List<Runnable> beforeCommands = new ArrayList<>();
        beforeCommands.add(() -> {
//            logo[0] = renderEngine.createShapeFromModel("res/models/logo.obj", 0, 0, 80, 1, Colour.AQUA);
//            renderEngine.addTo3DSpace(logo[0]);

            renderEngine.setBackgroundColour(0.1f, 0.1f, 0.1f, 1f);
        });
        renderEngine.before(beforeCommands);

        List<Runnable> duringCommands = new ArrayList<>();
//        duringCommands.add(() -> logo[0].getEntity().increaseRotation(0, 1, 0));
        duringCommands.add(heapGraph::inLoop);
        renderEngine.during(duringCommands);

        _injector.as(CACHE).addComponent(renderEngine);

        JFrame frame = new JFrame();
        JSplitPane pane = new JSplitPane();
        JSplitPane leftPane = new JSplitPane();

        JFXPanel fxSidebar = new JFXPanel();
        Platform.runLater(() -> fxSidebar.setScene(initFXSideBar()));

        JFXPanel fxBottomPanel = new JFXPanel();
        Platform.runLater(() -> fxBottomPanel.setScene(initFXBottomPanel()));

        leftPane.setTopComponent(canvas);
        leftPane.setBottomComponent(fxBottomPanel);
        leftPane.setDividerSize(0);
        leftPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        pane.setLeftComponent(leftPane);
        pane.setRightComponent(fxSidebar);
        pane.setDividerSize(0);

        frame.add(pane);
        frame.setSize(MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT);
        frame.setTitle(WINDOW_TITLE);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ExecutorService service = ThreadBuilder.createService("lwjgl");
        service.submit(() -> {
            try {
                renderEngine.run();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        service.shutdown();
    }
}