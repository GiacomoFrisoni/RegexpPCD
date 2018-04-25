package pcd.ass02.ex2.model;

import io.vertx.core.Vertx;
import javafx.application.Application;
import javafx.stage.Stage;
import pcd.ass02.ex1.controller.RegexpController;
import pcd.ass02.ex1.controller.RegexpControllerImpl;
import pcd.ass02.ex1.view.RegexpView;
import pcd.ass02.ex1.view.RegexpViewImpl;
import pcd.ass02.ex1.view.ViewConst;

public class Launcher extends Application {
	
	@Override
	public void start(final Stage primaryStage) {
		
		final RegexpView view = new RegexpViewImpl(primaryStage,  ViewConst.VERTX_TITLE.getTitle());
		final RegexpController controller = new RegexpControllerVertexImpl(view);
		view.setController(controller);
		view.show();
	}
	
	public static void main(final String[] args) {
		launch(args);	
	}

}
