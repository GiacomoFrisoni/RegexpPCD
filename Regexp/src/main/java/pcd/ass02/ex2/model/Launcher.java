package pcd.ass02.ex2.model;

import io.vertx.core.Vertx;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {
	
	@Override
	public void start(final Stage primaryStage) {
		/*
		final RegexpView view = new RegexpViewImpl(primaryStage);
		final RegexpController controller = new RegexpControllerImpl(view);
		view.setController(controller);
		view.show();
		*/
		final Vertx vertx = Vertx.vertx();
		final MainVerticle myVerticle = new MainVerticle();
		vertx.deployVerticle(myVerticle);
	}
	
	public static void main(final String[] args) {
		launch(args);	
	}

}
