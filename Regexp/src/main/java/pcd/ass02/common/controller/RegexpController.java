package pcd.ass02.common.controller;

/**
 * This interface represents the Controller of the application.
 *
 */
public interface RegexpController {

	/**
	 * Sets the path from which research the files to consider for the application of the regex.
	 * 
	 * @param path
	 * 		the starting path
	 */
	void setStartingPath(String path);
	
	/**
	 * Sets the max depth navigation from the starter path.
	 * 
	 * @param maxDepth
	 * 		the max depth of the navigation
	 */
	void setMaxDepthNavigation(int maxDepth);
	
	/**
	 * Sets the regex pattern to apply.
	 * 
	 * @param regex
	 * 		the regex pattern
	 */
	void setPattern(String regex);
	
	/**
	 * Starts the pattern matches research.
	 * 
	 * @return true if the operation is successful, false otherwise
	 */
	boolean search();
	
	/**
	 * Resets all data to make a new research.
	 */
	void reset();
	
}
