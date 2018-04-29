package pcd.ass02.ex3.main;

import javafx.application.Application;
import javafx.stage.Stage;
import pcd.ass02.ex1.controller.RegexpController;
import pcd.ass02.ex1.model.RegexpResearchData;
import pcd.ass02.ex1.model.RegexpResearchDataImpl;
import pcd.ass02.ex1.view.ViewConst;
import pcd.ass02.ex3.controller.RegexpControllerImpl;
import pcd.ass02.ex3.view.RegexpBindingView;
import pcd.ass02.ex3.view.RegexpBindingViewImpl;

/**
 * This is the launcher class for the regexp searcher with MVC implementation.
 */
public class RegexpMain extends Application {
	
	@Override
	public void start(final Stage primaryStage) {
		final RegexpResearchData model = new RegexpResearchDataImpl();
		final RegexpBindingView view = new RegexpBindingViewImpl(primaryStage, ViewConst.EXECUTOR_TITLE.getTitle());
		final RegexpController controller = new RegexpControllerImpl(model, view);
		view.setController(controller);
		view.show();
	}
	
	public static void main(final String[] args) {
		launch(args);
	}
	
}
