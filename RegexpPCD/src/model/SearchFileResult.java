package model;

import java.nio.file.Path;

public class SearchFileResult {
	
	private final Path path;
	private final int numberOfMatches;
	
	public SearchFileResult(final Path path, final int numberOfMatches) {
		this.path = path;
		this.numberOfMatches = numberOfMatches;
	}
	
	public Path getPath() {
		return this.path;
	}
	
	public int getNumberOfMatches() {
		return this.numberOfMatches;
	}
	
}
