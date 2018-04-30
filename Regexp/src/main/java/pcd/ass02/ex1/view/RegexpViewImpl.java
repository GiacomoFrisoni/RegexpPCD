package pcd.ass02.ex1.view;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pcd.ass02.common.controller.RegexpController;
import pcd.ass02.common.view.MessageUtils;
import pcd.ass02.common.view.MessageUtils.ExceptionType;

/**
 * Implementation of {@link RegexpView}.
 *
 */
public class RegexpViewImpl implements RegexpView {
	
	private final String windowTitle;
	private final Stage stage;
	private RegexpController controller;
	private MainFrame mainFrame;

	/**
	 * A JavaFX implementation of the view for RegeXP. Require the stage passed
	 * from the entry point of the application and the window title.
	 * 
	 * @param stage
	 * 		stage passed from the entry point of JavaFX application (usually primaryStage)
	 */
	public RegexpViewImpl (final Stage stage, final String windowTitle) {
		this.stage = stage;
		this.windowTitle = windowTitle;
	}

	
	@Override
	public void setController(final RegexpController controller) {
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

		this.stage.setTitle(this.windowTitle);
		this.stage.setScene(scene);
		this.stage.getIcons().addAll(
				new Image(("file:res/icon16x16.png")),
				new Image(("file:res/icon32x32.png")),
				new Image(("file:res/icon64x64.png")));
		this.stage.show();	
	}

	@Override
	public void setTotalFilesToScan(final int total) {
		this.mainFrame.setTotalFilesToScan(total);
	}
	
	@Override
	public void setNumberOfScannedFiles(final int scanned) {
		this.mainFrame.setNumberOfScannedFiles(scanned);
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
	public void showResult(final String path, final int matches, final long time) {
		this.mainFrame.addResult(path, matches, time);
	}

	@Override
	public void showResult(final String path, final String error) {
		this.mainFrame.addResult(path, error);
	}
	
	@Override
	public void showTotalElapsedTime(final long time) {
		this.mainFrame.showTotalElapsedTime(time);
	}
	
	@Override
	public void setFinish() {
		this.mainFrame.setFinish();
	}
	
	@Override
	public void showInputError(final String message) {
		this.mainFrame.setError(message);
	}

	@Override
	public void showException(final ExceptionType exceptionType, final String message, final Exception e) {
		MessageUtils.showExcpetion(exceptionType, message, e);
	}
	
}
