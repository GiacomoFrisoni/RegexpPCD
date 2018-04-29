package pcd.ass02.ex3.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pcd.ass02.ex1.controller.Chrono;
import pcd.ass02.ex1.model.Document;
import pcd.ass02.ex1.model.SearchFileErrorResult;
import pcd.ass02.ex1.model.SearchFileResult;
import pcd.ass02.ex1.model.SearchFileSuccessfulResult;

/**
 * This class represents a task aimed at searching matches of a given pattern
 * in a specified file.
 *
 */
public class SearchMatchesInFileTask implements Callable<SearchFileResult> {

	private final static long MAX_FILE_SIZE = 262144000;
	private final static String FILE_TOO_LARGE_MESSAGE = "Too large to analyze";
	
	private final Path filePath;
	private final Pattern pattern;
	
	/**
	 * Constructs a new file matches research task.
	 * 
	 * @param filePath
	 * 		the file path to use as research input
	 * @param pattern
	 * 		the regex pattern
	 */
	public SearchMatchesInFileTask(final Path filePath, final Pattern pattern) {
		Objects.requireNonNull(filePath);
		Objects.requireNonNull(pattern);
		this.filePath = filePath;
		this.pattern = pattern;
	}
	
	@Override
	public SearchFileResult call() {
		try {
			if (this.filePath.toFile().length() < MAX_FILE_SIZE) {
				final Chrono cron = new Chrono();
				cron.start();
				final Document document = new Document(this.filePath);
				final Matcher matcher = this.pattern.matcher(document.getContent());
				int nMatches = 0;
		        while (matcher.find())
		        	nMatches++;
				cron.stop();
				return new SearchFileSuccessfulResult(this.filePath, nMatches, cron.getTime());
			} else {
				return new SearchFileErrorResult(this.filePath, FILE_TOO_LARGE_MESSAGE);
			}
		} catch (final IOException e) {
			return new SearchFileErrorResult(this.filePath, e.getLocalizedMessage());
		}
	}

}
