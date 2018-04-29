package pcd.ass02.ex3.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

import pcd.ass02.ex1.model.SearchFileResult;
import pcd.ass02.ex3.view.RegexpBindingView;

/**
 * This class represents the main thread for the pattern matches research.
 *
 */
public class SearchMatchesService extends Thread {

	private final Path startingPath;
	private final Pattern pattern;
	private final int maxDepth;
	private final ExecutorService executor;
	private final RegexpBindingView view;
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
	 * @param executor
	 * 		the executor service on which submit tasks
	 * @param view
	 * 		the application view
	 */
	public SearchMatchesService(final Path startingPath, final Pattern pattern, final int maxDepth,
			final ExecutorService executor, final RegexpBindingView view) {
		Objects.requireNonNull(startingPath);
		Objects.requireNonNull(pattern);
		Objects.requireNonNull(executor);
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
		this.executor = executor;
		this.view = view;
		this.queue = new LinkedBlockingQueue<>();
	}
	
	@Override
	public void run() {
		// Starts master computation
		new Master(this.startingPath, this.pattern, this.maxDepth, this.executor, this.queue, this.view).compute();
	}

}
