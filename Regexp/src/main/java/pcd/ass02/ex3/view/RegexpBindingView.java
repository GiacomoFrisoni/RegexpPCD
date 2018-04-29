package pcd.ass02.ex3.view;

import pcd.ass02.ex1.controller.RegexpController;
import pcd.ass02.ex1.view.MessageUtils.ExceptionType;

public interface RegexpBindingView {

	/**
	 * Sets the controller for the view.
	 * 
	 * @param controller
	 * 		controller for the view
	 */
	void setController(RegexpController controller);
	
	/**
	 * Loads and show the window for the user.
	 */
	void show();
	
	/**
	 * Shows a file pattern research result.
	 * 
	 * @param path
	 * 		path to the scanned file
	 * @param nMatches
	 * 		number of matches in the file
	 * @param time
	 * 		time elapsed to parse the file
	 */
	void showResult(String path, int nMatches, long time);
	
	/**
	 * Shows a file pattern research result.
	 * 
	 * @param path
	 * 		path to the scanned file
	 * @param error
	 * 		error occurred during the scanning process
	 */
	void showResult(String path, String error);
	
	/**
	 * When computation is done, set the view to finished state
	 */
	void setFinish();
	
	/**
	 * Shows an exception when something went wrong
	 * 
	 * @param exceptionType
	 * 		the type of the occurred exception
	 * @param message
	 * 		message to show
	 * @param e
	 * 		exception
	 */
	void showException(ExceptionType exceptionType, String message, Exception e);
	
	/**
	 * Shows a small non-invasive error message if the input was incorrect.
	 * 
	 * @param message
	 * 		short message to show on the screen
	 */
	void showInputError(String message);

}
