package com.imperial.heap3d.entry;

import com.imperial.heap3d.factories.ControllerFactory;
import com.imperial.heap3d.utilities.TypeRegistry;
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
    private static int MIN_WINDOW_HEIGHT = 690;
    private static int MIN_WINDOW_WIDTH = 906;
    private final TypeRegistry _typeRegistry = new TypeRegistry();

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(System.getProperty("user.dir"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/views/MainWindow.fxml"));
        loader.setControllerFactory(new ControllerFactory(_typeRegistry.getInjector()));
        Parent root = loader.load();

        stage.setTitle(WINDOW_TITLE);
        stage.setScene(new Scene(root, MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT));
        stage.show();
    }
}
