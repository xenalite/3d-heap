package com.heap3d.application;

import com.heap3d.application.utilities.ControllerFactory;
import com.heap3d.application.utilities.TypeRegistry;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
        launch(args);
	}

    private static String WINDOW_TITLE = "3D HEAP VISUALISER";
    private static int MIN_WINDOW_HEIGHT = 510;
    private static int MIN_WINDOW_WIDTH = 705;
    private final TypeRegistry _typeRegistry = new TypeRegistry();

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/views/MainWindow.fxml"));
        loader.setControllerFactory(new ControllerFactory(_typeRegistry.getInjector()));
        Parent root = loader.load();

        stage.setTitle(WINDOW_TITLE);
        stage.setScene(new Scene(root, MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT));
        stage.show();
    }
}
