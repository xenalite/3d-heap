package com.heap3d.entry;

import com.google.common.eventbus.EventBus;
import com.heap3d.implementations.factories.ControllerFactory;
import com.heap3d.implementations.factories.ProcessFactory;
import com.heap3d.implementations.factories.ThreadBuilder;
import com.heap3d.implementations.factories.VirtualMachineProvider;
import com.heap3d.implementations.layout.HeapGraph;
import com.heap3d.implementations.layout.RenderEngineAdapter;
import com.heap3d.implementations.viewmodels.ApplicationTabViewModel;
import com.heap3d.implementations.viewmodels.BreakpointsTabViewModel;
import com.heap3d.implementations.viewmodels.HeapInfoTabViewModel;
import com.heap3d.interfaces.viewmodels.IApplicationTabViewModel;
import com.heap3d.interfaces.viewmodels.IBreakpointsTabViewModel;
import com.heap3d.interfaces.viewmodels.IHeapInfoTabViewModel;
import com.heap3d.ui.controllers.ApplicationTabController;
import com.heap3d.ui.controllers.BottomPanelController;
import com.heap3d.ui.controllers.BreakpointsTabController;
import com.heap3d.ui.controllers.HeapInfoTabController;
import com.heap3d.utilities.PathUtils;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private static final int MIN_BOTTOM_PANEL_HEIGHT = 150;
    private static final int MIN_CANVAS_HEIGHT = MIN_FRAME_HEIGHT - MIN_BOTTOM_PANEL_HEIGHT;

    private static final int MIN_FRAME_WIDTH = 1250;
    private static final int MIN_SIDEBAR_WIDTH = 400;
    private static final int MIN_CANVAS_WIDTH = MIN_FRAME_WIDTH - MIN_SIDEBAR_WIDTH;

    private final MutablePicoContainer _injector = new DefaultPicoContainer(new OptInCaching());

    public static void main(String[] args) {
        new SwingWrappedApplication().run();
    }

    private void run() {
        registerTypes();
        SwingUtilities.invokeLater(this::initialiseFrame);
    }

    private void initialiseFrame() {
        Canvas canvas = new Canvas();
        canvas.setSize(MIN_CANVAS_WIDTH, MIN_CANVAS_HEIGHT);

        RenderEngineAdapter renderEngine = new RenderEngineAdapter(canvas);
        initialiseRenderEngine(renderEngine);
        _injector.as(CACHE).addComponent(renderEngine);

        JFrame frame = new JFrame();
        JSplitPane pane = new JSplitPane();
        JSplitPane leftPane = new JSplitPane();

        JFXPanel jfxSidebar = new JFXPanel();
        JFXPanel jfxBottomPanel = new JFXPanel();

        Platform.runLater(() -> {
            jfxSidebar.setScene(initialiseJFXElement("Sidebar.fxml"));
            jfxBottomPanel.setScene(initialiseJFXElement("BottomPanel.fxml"));
        });

        leftPane.setTopComponent(canvas);
        leftPane.setBottomComponent(jfxBottomPanel);
        leftPane.setDividerSize(0);
        leftPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        pane.setLeftComponent(leftPane);
        pane.setRightComponent(jfxSidebar);
        pane.setDividerSize(0);

        frame.add(pane);
        frame.setSize(MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT);
        frame.setTitle(WINDOW_TITLE);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        runRenderEngine(renderEngine);
    }

    private void runRenderEngine(Runnable renderEngine) {
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

    private void registerTypes() {
        /* Components */
        _injector.as(CACHE).addComponent(VirtualMachineProvider.class);
        _injector.as(CACHE).addComponent(EventBus.class);
        _injector.addComponent(ProcessFactory.class);

        /* Controllers */
        _injector.addComponent(ApplicationTabController.class);
        _injector.addComponent(BreakpointsTabController.class);
        _injector.addComponent(BottomPanelController.class);
        _injector.addComponent(HeapInfoTabController.class);

        /* View Models */
        _injector.as(CACHE).addComponent(IApplicationTabViewModel.class, ApplicationTabViewModel.class);
        _injector.addComponent(IBreakpointsTabViewModel.class, BreakpointsTabViewModel.class);
        _injector.addComponent(IHeapInfoTabViewModel.class, HeapInfoTabViewModel.class);
    }

    private Scene initialiseJFXElement(String filename) {
        try {
            File f = new File(PathUtils.FXML_DIR + filename);
            FXMLLoader loader = new FXMLLoader(f.toURI().toURL());
            loader.setControllerFactory(new ControllerFactory(_injector));
            Parent root = loader.load();
            return new Scene(root);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    private void initialiseRenderEngine(RenderEngineAdapter renderEngine) {
        HeapGraph heapGraph = new HeapGraph(renderEngine);

//        final Shape[] logo = new Shape[1];
        List<Runnable> beforeCommands = new ArrayList<>();
        beforeCommands.add(() -> {
//            logo[0] = renderEngine.createShapeFromModel("res/models/logo.obj", 0, 0, 80, 1, Colour.AQUA);
//            renderEngine.addTo3DSpace(logo[0]);

            renderEngine.setBackgroundColour(0.1f, 0.1f, 0.1f, 1f);
        });

        List<Runnable> duringCommands = new ArrayList<>();
//        duringCommands.add(() -> logo[0].getEntity().increaseRotation(0, 1, 0));
        duringCommands.add(heapGraph::inLoop);

        renderEngine.before(beforeCommands);
        renderEngine.during(duringCommands);
    }
}