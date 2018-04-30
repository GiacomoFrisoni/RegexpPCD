package pcd.ass02.common.model;

import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * This interface represents the central part of the model.
 * It handles the research parameters.
 *
 */
public interface RegexpResearchData {
	
	/**
	 * Sets the starting path for the research.
	 * 
	 * @param path
	 * 		the starting path
	 * @throws IllegalArgumentException
	 * 		if the path does not exists or is not a directory
	 */
	void setStartingPath(String path) throws IllegalArgumentException;
	
	/**
	 * Sets the pattern to apply and search.
	 * 
	 * @param regex
	 * 		the regex pattern
	 * @throws IllegalArgumentException
	 * 		if the specified regex is not a valid pattern
	 */
	void setPattern(String regex) throws IllegalArgumentException;
	
	/**
	 * Sets the max depth of the research.
	 * 
	 * @param maxDepth
	 * 		the max depth
	 * @throws IllegalArgumentException
	 * 		if the specified depth is not a positive number
	 */
	void setMaxDepthNavigation(int maxDepth) throws IllegalArgumentException;
	
	/**
	 * @return the starting path.
	 * It is empty if not yet specified.
	 * 
	 */
	Optional<Path> getStartingPath();
	
	/**
	 * @return the regex pattern.
	 * It is empty if not yet specified.
	 */
	Optional<Pattern> getPattern();
	
	/**
	 * @return the max depth navigation
	 */
	int getMaxDepth();
	
	/**
	 * Resets all data.
	 */
	void reset();
	 
}
