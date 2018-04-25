package pcd.ass02.ex1.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

import pcd.ass02.ex1.model.SearchFileResult;
import pcd.ass02.ex1.view.RegexpView;

/**
 * This class represents the main thread for the pattern matches research.
 *
 */
public class SearchMatchesService extends Thread {
	
	private final Path startingPath;
	private final Pattern pattern;
	private final int maxDepth;
	private final RegexpView view;
	private final BlockingQueue<Optional<SearchFileResult>> queue;
	
	/**
	 * Constructs a new matches research service.
	 * 
	 * @param startPath
	 * 		the starter path of the research
	 * @param pattern
	 * 		the regex pattern
	 * @param maxDepth
	 * 		the max depth of the research
	 * @param view
	 * 		the application view
	 */
	public SearchMatchesService(final Path startingPath, final Pattern pattern, final int maxDepth, final RegexpView view) {
		Objects.requireNonNull(startingPath);
		Objects.requireNonNull(pattern);
		Objects.requireNonNull(view);
		if (!Files.exists(startingPath)) {
			throw new IllegalArgumentException("The starting path is not valid.");
		}
		if (maxDepth <= 0) {
			throw new IllegalArgumentException("The max depth must not be negative.");
		}
		this.startingPath = startingPath;
		this.pattern = pattern;
		this.maxDepth = maxDepth;
		this.view = view;
		this.queue = new LinkedBlockingQueue<>();
	}
	
	@Override
	public void run() {
		// Starts the results consumer on a new thread
		new Consumer(this.queue, this.view).start();
		// Starts master computation
		new Master(this.startingPath, this.pattern, this.maxDepth, this.queue, this.view).compute();
	}

}
