package view;

import controller.RegexpController;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class RegexpViewImpl implements RegexpView {
	
	private static final String WINDOW_TITLE = "RegExp - Frisoni & Pabich";
	
	private final Stage stage;
	private RegexpController controller;
	private MainFrame mainFrame;

	/**
	 * A JavaFX implementation of the view for RegeXP. Require the stage passed
	 * from the entry point of the application and the window title.
	 * @param stage
	 * 		stage passed from the entry point of JavaFX application (usually primaryStage)
	 */
	public RegexpViewImpl (final Stage stage) {
		this.stage = stage;
	}

	
	@Override
	public void setController(RegexpController controller) {
		this.controller = controller;
	}
	
	@Override
	public void show() {
		mainFrame = new MainFrame(this.controller, this.stage);		
		final Scene scene = new Scene(mainFrame);
		
		this.stage.setOnCloseRequest(e -> {
			this.stage.close();
	        Platform.exit();
	        System.exit(0);
		});

		this.stage.setTitle(WINDOW_TITLE);
		this.stage.setScene(scene);
		this.stage.setResizable(false);
		this.stage.getIcons().addAll(
				new Image(("file:res/icon16x16.png")),
				new Image(("file:res/icon32x32.png")),
				new Image(("file:res/icon64x64.png")));
		this.stage.show();	
	}


	
	
}
