package pcd.ass02.ex1.main;

import javafx.application.Application;
import javafx.stage.Stage;
import pcd.ass02.common.controller.RegexpController;
import pcd.ass02.common.model.RegexpResearchData;
import pcd.ass02.common.model.RegexpResearchDataImpl;
import pcd.ass02.common.view.ViewConst;
import pcd.ass02.ex1.controller.RegexpControllerImpl;
import pcd.ass02.ex1.view.RegexpView;
import pcd.ass02.ex1.view.RegexpViewImpl;

/**
 * This is the launcher class for the regexp searcher with MVC implementation,
 * based on tasks and executor.
 */
public class RegexpMain extends Application {
	
	@Override
	public void start(final Stage primaryStage) {
		final RegexpResearchData model = new RegexpResearchDataImpl();
		final RegexpView view = new RegexpViewImpl(primaryStage, ViewConst.EXECUTOR_TITLE.getTitle());
		final RegexpController controller = new RegexpControllerImpl(model, view);
		view.setController(controller);
		view.show();
	}
	
	public static void main(final String[] args) {
		launch(args);
	}
	
}
