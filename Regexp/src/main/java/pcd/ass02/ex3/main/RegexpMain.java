package pcd.ass02.ex3.main;

import javafx.application.Application;
import javafx.stage.Stage;
import pcd.ass02.common.controller.RegexpController;
import pcd.ass02.common.model.RegexpResearchData;
import pcd.ass02.common.model.RegexpResearchDataImpl;
import pcd.ass02.common.view.ViewConst;
import pcd.ass02.ex3.controller.RegexpControllerImpl;
import pcd.ass02.ex3.view.RegexpBindingView;
import pcd.ass02.ex3.view.RegexpBindingViewImpl;

/**
 * This is the launcher class for the regexp searcher with MVC implementation,
 * based on RxJava and executors.
 */
public class RegexpMain extends Application {
	
	@Override
	public void start(final Stage primaryStage) {
		final RegexpResearchData model = new RegexpResearchDataImpl();
		final RegexpBindingView view = new RegexpBindingViewImpl(primaryStage, ViewConst.RX_TITLE.getTitle());
		final RegexpController controller = new RegexpControllerImpl(model, view);
		view.setController(controller);
		view.show();
	}
	
	public static void main(final String[] args) {
		launch(args);
	}
	
}
