package view;

import controller.RegexpController;

public interface RegexpView {

	/**
	 * Set the controller for the view
	 * @param controller
	 * 		controller for the view
	 */
	public void setController(RegexpController controller);
	
	/**
	 * Load and show the window for the user
	 */
	public void show();

}
