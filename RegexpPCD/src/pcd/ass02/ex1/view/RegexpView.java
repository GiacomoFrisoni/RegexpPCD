package pcd.ass02.ex1.view;

import pcd.ass02.ex1.controller.RegexpController;

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
