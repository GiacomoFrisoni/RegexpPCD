package pcd.ass02.ex1.model;

import java.nio.file.Path;

/**
 * This abstract class represents the common behavior of a pattern research result in a file.
 *
 */
public abstract class SearchFileResult {
	
	private final Path path;
	
	/**
	 * Constructs a new file research result.
	 * 
	 * @param path
	 * 		the path of the file
	 */
	public SearchFileResult(final Path path) {
		this.path = path;
	}

	/**
	 * @return the path of the analyzed file
	 */
	public Path getPath() {
		return this.path;
	}
	
}
