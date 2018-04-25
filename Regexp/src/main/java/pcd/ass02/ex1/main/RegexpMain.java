package pcd.ass02.ex1.main;

import javafx.application.Application;
import javafx.stage.Stage;
import pcd.ass02.ex1.controller.RegexpController;
import pcd.ass02.ex1.controller.RegexpControllerImpl;
import pcd.ass02.ex1.view.RegexpView;
import pcd.ass02.ex1.view.RegexpViewImpl;
import pcd.ass02.ex1.view.ViewConst;

/**
 * This is the launcher class for the regexp searcher with MVC implementation.
 */
public class RegexpMain extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		final RegexpView view = new RegexpViewImpl(primaryStage, ViewConst.EXECUTOR_TITLE.getTitle());
		final RegexpController controller = new RegexpControllerImpl(view);
		view.setController(controller);
		view.show();
	}
	
	public static void main(final String[] args) {
		launch(args);
	}
	
}
