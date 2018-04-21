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
	
	/**
	 * Displays the percentage of files that have at least one match
	 * @param percentage
	 * 		percentage to show (0-100)
	 */
	public void showLeastOneMatchPercentage(final double percentage);
	
	/**
	 * Displays the mean number of matches among file with matches
	 * @param mean
	 * 		the mean number of matches
	 */
	public void showMeanNumberOfMatches(final double mean);
	
	/**
	 * Set the number of files that has been successfully scanned at this moment
	 * @param scanned
	 * 		number of files successfully scanned
	 */
	public void setNumberOfScannedFiles(final int scanned);
	
	/**
	 * Set the total number of files to scan
	 * @param total
	 * 		number of files to scan
	 */
	public void setTotalFilesToScan(final int total);
	
	/**
	 * Add a result to the total results
	 * @param path
	 * 		path to the file scanned
	 * @param result
	 * 		result of matches
	 */
	public void addResult(final String path, final int matches);
	
	/**
	 * Add a result to the total results
	 * @param path
	 * 		path to the file scanned
	 * @param error
	 * 		error occurred during the scanning process
	 */
	public void addResult(final String path, final String error);
	
	/**
	 * Show an exception when something went wrong with a thread
	 * @param message
	 * 		message to show
	 * @param e
	 * 		exception that occurred
	 */
	public void showThreadException(final String message, final Exception e);
	
	
	/**
	 * Show a small non-invasive error message if the input was incorrect
	 * @param message
	 * 		short message to show on the screen
	 */
	public void showInputError(final String message);

}
