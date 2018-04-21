package pcd.ass02.ex1.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pcd.ass02.ex1.model.Document;
import pcd.ass02.ex1.model.SearchFileResult;

/**
 * This class represents a task aimed at searching matches of a given pattern
 * in a specified file.
 *
 */
public class SearchMatchesInFileTask implements Runnable {

	private final File file;
	private final Pattern pattern;
	private final BlockingQueue<SearchFileResult> queue;
	
	/**
	 * Constructs a new file matches research task.
	 * 
	 * @param file
	 * 		the file to use as research input
	 * @param pattern
	 * 		the regex pattern
	 * @param queue
	 * 		the producer / consumer queue
	 */
	public SearchMatchesInFileTask(final File file, final Pattern pattern, final BlockingQueue<SearchFileResult> queue) {
		this.file = file;
		this.pattern = pattern;
		this.queue = queue;
	}
	
	@Override
	public void run() {
		try {
			final Document document = new Document(this.file);
			final Matcher matcher = this.pattern.matcher(document.getContent());
			int nMatches = 0;
	        while (matcher.find())
	        	nMatches++;
			this.queue.add(new SearchFileResult(this.file.toPath(), nMatches));
		} catch (final FileNotFoundException e) {
			this.queue.add(new SearchFileResult(this.file.toPath(), e.getLocalizedMessage()));
		}
	}

}
