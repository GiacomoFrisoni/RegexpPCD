package main;

import controller.RegexpController;
import controller.RegexpControllerImpl;
import javafx.application.Application;
import javafx.stage.Stage;
import view.RegexpView;
import view.RegexpViewImpl;

public class RegexpMain extends Application {
	
	@Override
	public void start(Stage primaryStage) {		
		final RegexpView view = new RegexpViewImpl(primaryStage);
		final RegexpController controller = new RegexpControllerImpl(view);
		view.setController(controller);
		view.show();
	}
	
	public static void main(final String[] args) {
		launch(args);	
	}
	
}
