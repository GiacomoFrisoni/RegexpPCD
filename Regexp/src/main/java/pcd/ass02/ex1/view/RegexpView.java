package pcd.ass02.ex1.view;

import pcd.ass02.ex1.controller.RegexpController;

public interface RegexpView {

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
	 * Sets the total number of files to scan.
	 * 
	 * @param total
	 * 		number of files to scan
	 */
	void setTotalFilesToScan(int total);
	
	/**
	 * Sets the number of files that has been successfully scanned at this moment.
	 * 
	 * @param scanned
	 * 		number of files successfully scanned
	 */
	void setNumberOfScannedFiles(int scanned);
	
	/**
	 * Displays the percentage of files that have at least one match.
	 * 
	 * @param percentage
	 * 		percentage to show (0-100)
	 */
	void showLeastOneMatchPercentage(double percentage);
	
	/**
	 * Displays the mean number of matches among file with matches.
	 * 
	 * @param mean
	 * 		the mean number of matches
	 */
	void showMeanNumberOfMatches(double mean);
	
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
	 * Shows an exception when something went wrong with a thread.
	 * 
	 * @param message
	 * 		message to show
	 * @param e
	 * 		exception that occurred
	 */
	void showThreadException(String message, Exception e);
	
	/**
	 * Shows a small non-invasive error message if the input was incorrect.
	 * 
	 * @param message
	 * 		short message to show on the screen
	 */
	void showInputError(String message);
	
	/**
	 * Reset all components to make a new compututaion
	 */
	void reset();

}
