package pcd.ass02.ex1.model;

import java.nio.file.Path;

public class SearchFileSuccessfulResult extends SearchFileResult {

	private final int nMatches;
	private final long elapsedTime;
	
	public SearchFileSuccessfulResult(final Path path, final int nMatches, final long elapsedTime) {
		super(path);
		this.nMatches = nMatches;
		this.elapsedTime = elapsedTime;
	}
	
	public int getNumberOfMatches() {
		return this.nMatches;
	}
	
	public long getElapsedTime() {
		return this.elapsedTime;
	}

}
