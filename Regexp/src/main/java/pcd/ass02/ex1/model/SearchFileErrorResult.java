package pcd.ass02.ex1.model;

import java.nio.file.Path;

public class SearchFileErrorResult extends SearchFileResult {

	private final String errorMessage;
	
	public SearchFileErrorResult(final Path path, final String errorMessage) {
		super(path);
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return this.errorMessage;
	}

}
