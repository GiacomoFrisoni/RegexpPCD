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
