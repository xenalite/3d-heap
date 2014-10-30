package org.debugger.application;

import org.debugger.application.utilities.ControllerFactory;
import org.debugger.application.utilities.VirtualMachineProvider;
import org.debugger.ui.controllers.ActionsTabController;
import org.debugger.ui.controllers.MainWindowController;
import org.debugger.ui.controllers.ProcessTabController;
import org.debugger.ui.viewmodels.ActionTabViewModel;
import org.debugger.ui.viewmodels.MainWindowViewModel;
import org.debugger.ui.viewmodels.ProcessTabViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.picocontainer.Characteristics;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.behaviors.OptInCaching;

public class Main extends Application {

	public static void main(String[] args) {
        launch(args);
	}

    private static String WINDOW_TITLE = "3D HEAP VISUALISER";
    private static int MIN_WINDOW_HEIGHT = 510;
    private static int MIN_WINDOW_WIDTH = 705;
    private MutablePicoContainer _injector = new DefaultPicoContainer(new OptInCaching());

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
        _injector.as(Characteristics.CACHE).addComponent(VirtualMachineProvider.class);

        _injector.addComponent(MainWindowController.class);
        _injector.addComponent(ActionsTabController.class);
        _injector.addComponent(ProcessTabController.class);

        _injector.addComponent(MainWindowViewModel.class);
        _injector.addComponent(ActionTabViewModel.class);
        _injector.addComponent(ProcessTabViewModel.class);
    }
}
