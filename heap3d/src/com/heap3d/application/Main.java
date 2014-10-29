package com.heap3d.application;

import com.heap3d.ui.controllers.ActionsTabController;
import com.heap3d.ui.controllers.MainWindowController;
import com.heap3d.ui.controllers.ProcessTabController;
import com.heap3d.ui.viewmodels.ActionTabViewModel;
import com.heap3d.ui.viewmodels.MainWindowViewModel;
import com.heap3d.ui.viewmodels.ProcessTabViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;

public class Main extends Application {

	public static void main(String[] args) {
        launch(args);
	}

    private static String WINDOW_TITLE = "3D HEAP VISUALISER";
    private static int MIN_WINDOW_HEIGHT = 510;
    private static int MIN_WINDOW_WIDTH = 705;
    private MutablePicoContainer _injector = new DefaultPicoContainer();

    @Override
    public void start(Stage stage) throws Exception {
        registerTypes();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/views/MainWindow.fxml"));
        loader.setControllerFactory(new ControllerFactory(_injector));
        Parent root = loader.load();

        stage.setTitle(WINDOW_TITLE);
        stage.setScene(new Scene(root, MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT));
        stage.show();
    }

    private void registerTypes() {
        _injector.addComponent(MainWindowController.class);
        _injector.addComponent(ActionsTabController.class);
        _injector.addComponent(ProcessTabController.class);

        _injector.addComponent(MainWindowViewModel.class);
        _injector.addComponent(ActionTabViewModel.class);
        _injector.addComponent(ProcessTabViewModel.class);
    }
}
