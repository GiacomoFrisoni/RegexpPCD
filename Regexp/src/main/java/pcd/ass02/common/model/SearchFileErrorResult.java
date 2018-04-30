package pcd.ass02.common.model;

import java.nio.file.Path;

/**
 * This class represents an error result occurred during a pattern research in a file.
 *
 */
public class SearchFileErrorResult extends SearchFileResult {

	private final String errorMessage;
	
	/**
	 * Constructs a new error result.
	 * 
	 * @param path
	 * 		the path of the file
	 * @param errorMessage
	 * 		the description of the error
	 */
	public SearchFileErrorResult(final Path path, final String errorMessage) {
		super(path);
		this.errorMessage = errorMessage;
	}
	
	/**
	 * @return the description of the error
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}

}
