package pcd.ass02.ex1.view;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pcd.ass02.ex1.controller.RegexpController;

public class RegexpViewImpl implements RegexpView {
	
	private static final String WINDOW_TITLE = "RegExp - Giacomo Frisoni & Marcin Pabich";
	
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


	@Override
	public void showLeastOneMatchPercentage(final double percentage) {
		this.mainFrame.showLeastOneMatchPercentage(percentage);
	}
	
	@Override
	public void showMeanNumberOfMatches(final double mean) {
		this.mainFrame.showMeanNumberOfMatches(mean);
	}


	@Override
	public void setNumberOfScannedFiles(final int scanned) {
		this.mainFrame.setNumberOfScannedFiles(scanned);
	}


	@Override
	public void setTotalFilesToScan(final int total) {
		this.mainFrame.setTotalFilesToScan(total);
	}


	@Override
	public void addResult(final String path, final int matches) {
		this.mainFrame.addResult(path, matches);
	}


	@Override
	public void addResult(final String path, final String error) {
		this.mainFrame.addResult(path, error);
	}
	
	@Override
	public void showInputError(final String message) {
		this.mainFrame.setError(message);
	}

	@Override
	public void showThreadException(final String message, final Exception e) {
		MessageUtils.showThreadExcpetion(message, e);
	}
	
}
