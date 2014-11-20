package com.imperial.heap3d.entry;

import java.io.File;
import java.net.URL;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.events.ControlEvent;
import com.imperial.heap3d.events.EventType;
import com.imperial.heap3d.factories.ControllerFactory;
import com.imperial.heap3d.utilities.TypeRegistry;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application {

	public static void main(String[] args) {
        launch(args);
	}

    private static String WINDOW_TITLE = "3D HEAP VISUALISER";
    private static int MIN_WINDOW_HEIGHT = 690;
    private static int MIN_WINDOW_WIDTH = 906;
    private final TypeRegistry _typeRegistry = new TypeRegistry();

    @Override
    public void start(Stage stage) throws Exception {
    	String dir = System.getProperty("user.dir");
        File f = new File(dir + "/src/com/imperial/heap3d/ui/views/MainWindow.fxml");
        FXMLLoader loader = new FXMLLoader(f.toURI().toURL());
        loader.setControllerFactory(new ControllerFactory(_typeRegistry.getInjector()));
        Parent root = loader.load();

        stage.setTitle(WINDOW_TITLE);
        stage.setScene(new Scene(root, MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT));
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                EventBus c = _typeRegistry.getInjector().getComponent(EventBus.class);
                c.post(new ControlEvent(EventType.STOP,null,null));
            }
        });
    }
}
