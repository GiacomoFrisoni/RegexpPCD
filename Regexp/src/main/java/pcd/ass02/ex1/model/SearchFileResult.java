package pcd.ass02.ex1.model;

import java.nio.file.Path;
import java.util.Optional;

/**
 * This class represents the result of a pattern research in a file.
 *
 */
public class SearchFileResult {
	
	private final Path path;
	private final Optional<Integer> numberOfMatches;
	private final Optional<String> message;
	
	/**
	 * Constructs a new file research result.
	 * 
	 * @param path
	 * 		the path of the file
	 * @param numberOfMatches
	 * 		the number of matches
	 */
	public SearchFileResult(final Path path, final Integer numberOfMatches) {
		this.path = path;
		this.numberOfMatches = Optional.of(numberOfMatches);
		this.message = Optional.empty();
	}

	/**
	 * Constructs a new file research result.
	 * 
	 * @param path
	 * 		the path of the file
	 * @param message
	 * 		the error message
	 */
	public SearchFileResult(final Path path, final String message) {
		this.path = path;
		this.numberOfMatches = Optional.empty();
		this.message = Optional.of(message);
	}
	
	/**
	 * @return the path of the analyzed file
	 */
	public Path getPath() {
		return this.path;
	}
	
	/**
	 * @return the number of matches
	 */
	public Optional<Integer> getNumberOfMatches() {
		return this.numberOfMatches;
	}
	
	/**
	 * @return the resulted error message
	 */
	public Optional<String> getMessage() {
		return this.message;
	}
	
}
