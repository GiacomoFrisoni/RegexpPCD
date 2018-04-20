package model;

import java.nio.file.Path;
import java.util.Optional;

public class SearchFileResult {
	
	private final Path path;
	private final Optional<Integer> numberOfMatches;
	private final Optional<String> message;
	
	public SearchFileResult(final Path path, final Integer numberOfMatches) {
		this.path = path;
		this.numberOfMatches = Optional.of(numberOfMatches);
		this.message = Optional.empty();
	}

	public SearchFileResult(final Path path, final String message) {
		this.path = path;
		this.numberOfMatches = Optional.empty();
		this.message = Optional.of(message);
	}
	
	public Path getPath() {
		return this.path;
	}
	
	public Optional<Integer> getNumberOfMatches() {
		return this.numberOfMatches;
	}
	
	public Optional<String> getMessage() {
		return this.message;
	}
	
}
