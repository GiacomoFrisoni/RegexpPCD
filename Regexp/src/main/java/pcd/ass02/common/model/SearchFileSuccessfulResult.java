package pcd.ass02.common.model;

import java.nio.file.Path;

/**
 * This class represents a successful result for a pattern research in a file.
 *
 */
public class SearchFileSuccessfulResult extends SearchFileResult {

	private final int nMatches;
	private final long elapsedTime;
	
	/**
	 * Constructs a new successful result.
	 * 
	 * @param path
	 * 		the path of the file
	 * @param nMatches
	 * 		the number of pattern matches
	 * @param elapsedTime
	 * 		the elapsed time for the computation
	 */
	public SearchFileSuccessfulResult(final Path path, final int nMatches, final long elapsedTime) {
		super(path);
		this.nMatches = nMatches;
		this.elapsedTime = elapsedTime;
	}
	
	/**
	 * @return the number of pattern matches
	 */
	public int getNumberOfMatches() {
		return this.nMatches;
	}
	
	/**
	 * @return the elapsed computation time
	 */
	public long getElapsedTime() {
		return this.elapsedTime;
	}

}
