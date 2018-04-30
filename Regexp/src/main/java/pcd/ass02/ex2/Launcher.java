package pcd.ass02.ex2;

import javafx.application.Application;
import javafx.stage.Stage;
import pcd.ass02.common.controller.RegexpController;
import pcd.ass02.common.model.RegexpResearchData;
import pcd.ass02.common.model.RegexpResearchDataImpl;
import pcd.ass02.common.view.ViewConst;
import pcd.ass02.ex1.view.RegexpView;
import pcd.ass02.ex1.view.RegexpViewImpl;

/**
 * This is the launcher class for the regexp searcher, based on Vert.x.
 */
public class Launcher extends Application {
	
	@Override
	public void start(final Stage primaryStage) {
		final RegexpResearchData model = new RegexpResearchDataImpl();
		final RegexpView view = new RegexpViewImpl(primaryStage, ViewConst.VERTX_TITLE.getTitle());
		final RegexpController controller = new RegexpControllerImpl(model, view);
		view.setController(controller);
		view.show();
	}
	
	public static void main(final String[] args) {
		launch(args);	
	}

}
