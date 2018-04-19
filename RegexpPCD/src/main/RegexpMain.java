package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.RegexpView;
import view.RegexpViewImpl;

public class RegexpMain extends Application {
	
	@Override
	public void start(Stage primaryStage) {		
		final RegexpView view = new RegexpViewImpl(primaryStage);
		//TODO controller and eventually model
		
		view.setController(null);
		view.show();
		
	}
	
	public static void main(final String[] args) {
		launch(args);	
	}
}
