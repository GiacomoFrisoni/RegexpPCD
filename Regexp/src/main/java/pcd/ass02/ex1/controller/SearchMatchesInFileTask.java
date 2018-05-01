package pcd.ass02.ex1.controller;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileSystemException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pcd.ass02.common.controller.Chrono;
import pcd.ass02.common.model.SearchFileErrorResult;
import pcd.ass02.common.model.SearchFileResult;
import pcd.ass02.common.model.SearchFileSuccessfulResult;
import pcd.ass02.ex1.model.Document;

/**
 * This class represents a task aimed at searching matches of a given pattern
 * in a specified file.
 *
 */
public class SearchMatchesInFileTask implements Callable<Void> {

	private final static long MAX_FILE_SIZE = 262144000;
	private final static String FILE_TOO_LARGE_MESSAGE = "Too large to analyze";
	private final static String ACCESS_DENIED_MESSAGE = "Access denied";
	private final static String ALREADY_IN_USE_MESSAGE = "Already in use";
	private final static String GENERIC_ERROR_MESSAGE = "IO exception";
	
	private final Path filePath;
	private final Pattern pattern;
	private final BlockingQueue<Optional<SearchFileResult>> queue;
	
	/**
	 * Constructs a new file matcher research task.
	 * 
	 * @param filePath
	 * 		the file path to use as research input
	 * @param pattern
	 * 		the regex pattern
	 * @param queue
	 * 		the producer / consumer queue
	 */
	public SearchMatchesInFileTask(final Path filePath, final Pattern pattern,
			final BlockingQueue<Optional<SearchFileResult>> queue) {
		Objects.requireNonNull(filePath);
		Objects.requireNonNull(pattern);
		Objects.requireNonNull(queue);
		this.filePath = filePath;
		this.pattern = pattern;
		this.queue = queue;
	}
	
	@Override
	public Void call() {
		try {
			// Considers the file only if it is not too large
			if (this.filePath.toFile().length() < MAX_FILE_SIZE) {
				final Chrono cron = new Chrono();
				cron.start();
				// Gets the string content of the file and search matches in it
				final Document document = new Document(this.filePath);
				final Matcher matcher = this.pattern.matcher(document.getContent());
				int nMatches = 0;
		        while (matcher.find())
		        	nMatches++;
				cron.stop();
				// Adds a successful result in the queue
				this.queue.add(Optional.of(new SearchFileSuccessfulResult(this.filePath, nMatches, cron.getTime())));
			} else {
				// Adds an error result in the queue
				this.queue.add(Optional.of(new SearchFileErrorResult(this.filePath, FILE_TOO_LARGE_MESSAGE)));
			}
		} catch (final IOException e) {
			// Adds an error result in the queue
			if (e instanceof AccessDeniedException) {
				this.queue.add(Optional.of(new SearchFileErrorResult(this.filePath, ACCESS_DENIED_MESSAGE)));
			} else if (e instanceof FileSystemException){
				this.queue.add(Optional.of(new SearchFileErrorResult(this.filePath, ALREADY_IN_USE_MESSAGE)));
			} else {
				this.queue.add(Optional.of(new SearchFileErrorResult(this.filePath, GENERIC_ERROR_MESSAGE)));
			}		
		} 
		return null;
	}

}
